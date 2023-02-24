package com.GestionGastosIngresos.Backend.Spring.Controller;

import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.ArgumentNoValidExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.ArgumentExcepcion.FechaIncorrectaException;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.BDExcepcion.AccessBDExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Controller.Excepcion.GeneralExcepcionandControllerAdvide.ListaEmptyExcepcion;
import com.GestionGastosIngresos.Backend.Spring.Entity.GastoIngreso;
import com.GestionGastosIngresos.Backend.Spring.Entity.Persona;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IPersonaService;
import com.aspose.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.aspose.pdf.internal.html.forms.InputElementType.File;

@RestController
@RequestMapping("/api/PDF")
public class CreadorPDFController {
    @Autowired
    private IPersonaService personaService;

    private File file;

    @PostMapping("/crearPdf/{id}+{fecha}")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearPDF(@PathVariable long id, @PathVariable String fecha){
        int fechaNum = validandoFechas(fecha);
        Persona persona = personaService.findById(id);

        if(persona.getGastoIngreso().isEmpty()){
            throw new ListaEmptyExcepcion("Gastos e Ingresos", "Cliente ->" +id);
        }

        Document document = new Document();
        Page pdfPage = document.getPages().add();
        Table pdfTable = new Table();

        AtomicInteger cont = new AtomicInteger(0);
        AtomicReference<Double> subtotalIngresos = new AtomicReference<>(0.0);
        AtomicReference<Double> subtotalGastos = new AtomicReference<>(0.0);

        pdfTable.setDefaultCellBorder(new BorderInfo(BorderSide.All, 1.0f, Color.getGray()));
        pdfTable.setDefaultCellPadding(new MarginInfo(5,5,5,5));

        insertarDatosEnLaTabla(persona.getGastoIngreso(), pdfTable, cont, subtotalIngresos, subtotalGastos, fechaNum);

        pdfPage.getParagraphs().add(pdfTable);

        System.out.println("Salvando.....");

        document.save("src/PDF/"+id+"/"
                + LocalDate.now()
                + LocalTime.now().getHour()
                + LocalTime.now().getMinute()
                + ".pdf");

        System.out.println("Terminado! -> Datos insertados correctamente");

    }

    private int validandoFechas(String fecha) {
        System.out.println("Validando fechas...");
        if (fecha.matches("[\\d]{4}/[\\d]{2}/[\\d]{2}")){
            throw new FechaIncorrectaException(fecha);
        }

        try {
            return Integer.parseInt(fecha.replaceAll("-", ""));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertarDatosEnLaTabla(List<GastoIngreso> gastoIngresos, Table pdfTable, AtomicInteger cont,
                                        AtomicReference<Double> subtotalIngresos, AtomicReference<Double> subtotalGastos, int fechaNum) {
        System.out.println("Insertando Datos en la Tabla....");

        creandoCampoInicialEIngreso(pdfTable);

        subtotalIngresos.set(insertandoIngresos(gastoIngresos, pdfTable, cont, subtotalIngresos, fechaNum));

        creandoCampoGasto(pdfTable, cont);

        subtotalGastos.set(insertandoGastos(gastoIngresos, pdfTable, cont, subtotalGastos, fechaNum));

        Row total = pdfTable.getRows().add();
        total.getCells().add("Total");
        total.getCells().add("" + (subtotalIngresos.get() - subtotalGastos.get()));
        total.getCells().add(" ").setColSpan(2);
    }

    private void creandoCampoInicialEIngreso(Table pdfTable) {
        System.out.println("Creando campo Reporte....");

        TextState textState = new TextState();
        textState.setFontSize(14.0f);
        textState.setHorizontalAlignment(1);

        Cell cell = pdfTable.getRows().add().getCells().add("Reporte");
        cell.setColSpan(4);
        cell.setDefaultCellTextState(textState);
        cell.setAlignment(2);

        pdfTable.getRows().add().getCells().add(" ").setColSpan(4);

        System.out.println("Creando campo Ingresos....");

        Row filaIngresoName = pdfTable.getRows().add();
        filaIngresoName.getCells().add("Ingresos").setColSpan(4);

        Row columnas = pdfTable.getRows().add();
        columnas.getCells().add("Fecha");
        columnas.getCells().add("Monto");
        columnas.getCells().add("Observaciones").setColSpan(2);

    }

    private double insertandoIngresos(List<GastoIngreso> gastoIngresos, Table pdfTable,
                                      AtomicInteger cont, AtomicReference<Double> subtotalIngresos, int fechaNum) {
        System.out.println("Insertando datos en Ingresos....");

        HashMap<String, Integer> fechasIngresosNum = new HashMap<>();

        gastoIngresos.forEach(ingreso -> fechasIngresosNum
                .put(ingreso.getFecha(), Integer.parseInt(ingreso.getFecha().replace("-", ""))));

        fechasIngresosNum.forEach((fechString, fechaInteger) -> {

            if (fechaInteger >= fechaNum) {

                double ingresoAux = gastoIngresos.stream()
                        .filter(ingreso -> ingreso.getFecha().equals(fechString))
                        .findFirst()
                        .get()
                        .getIngreso();

                Row ingresos = pdfTable.getRows().add();
                ingresos.getCells().add(fechString);
                ingresos.getCells().add("" + ingresoAux);
                ingresos.getCells().add("Observacion " + cont.incrementAndGet()).setColSpan(2);
                subtotalIngresos.set(subtotalIngresos.get() + ingresoAux);
            }
        });

        Row subTotal = pdfTable.getRows().add();
        subTotal.getCells().add("Subtotal");
        subTotal.getCells().add("" + subtotalIngresos);
        subTotal.getCells().add(" ").setColSpan(2);

        pdfTable.getRows().add().getCells().add(" ").setColSpan(4);

        return subtotalIngresos.get();
    }

    private void creandoCampoGasto(Table pdfTable, AtomicInteger cont) {
        System.out.println("Creando campo Gastos....");

        Row filaGastoName = pdfTable.getRows().add();
        filaGastoName.getCells().add("Gastos").setColSpan(4);
        cont.set(0);
    }

    private double insertandoGastos(List<GastoIngreso> gastoIngresos, Table pdfTable,
                                    AtomicInteger cont, AtomicReference<Double> subtotalGastos, int fechaNum) {
        System.out.println("Insertando datos en Gastos....");

        HashMap<String, Integer> fechasGastosNum = new HashMap<>();

        gastoIngresos.forEach(gasto -> fechasGastosNum
                .put(gasto.getFecha(), Integer.parseInt(gasto.getFecha().replace("-", ""))));

        fechasGastosNum.forEach((fechString, fechaInteger) -> {

            if (fechaInteger >= fechaNum) {

                double gastoAux = gastoIngresos.stream()
                        .filter(ingreso -> ingreso.getFecha().equals(fechString))
                        .findFirst()
                        .get()
                        .getGasto();

                Row gastos = pdfTable.getRows().add();
                gastos.getCells().add(fechString);
                gastos.getCells().add("" + gastoAux);
                gastos.getCells().add("Observacion " + cont.incrementAndGet()).setColSpan(2);
                subtotalGastos.set(subtotalGastos.get() + gastoAux);
            }
        });

        Row subT = pdfTable.getRows().add();
        subT.getCells().add("Subtotal");
        subT.getCells().add("" + subtotalGastos);
        subT.getCells().add(" ").setColSpan(2);

        pdfTable.getRows().add().getCells().add(" ").setColSpan(4);

        return subtotalGastos.get();
    }
    @GetMapping("/cargarPdf/{id}+{nombre}")
    public ResponseEntity<Resource> cargarPDF(@PathVariable long id, @PathVariable String nombre){
        if(!nombre.contains("."))
            throw new ArgumentNoValidExcepcion(nombre, "nombre");
        else if (!nombre.split("\\.",2)[1].matches("pdf")) {
            throw new ArgumentNoValidExcepcion(nombre, "nombre");
        }

        Path rutaPdf = Paths.get("src/PDF/" + id).resolve(nombre).toAbsolutePath();
        Resource recurso;

        try {
            recurso = new UrlResource(rutaPdf.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("No se pudo cargar el pdf " + nombre);
        }

        if (!recurso.exists() && !recurso.isReadable())
            throw new AccessBDExcepcion();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

        return new ResponseEntity<>(recurso, httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/verMisPdf/{id}")
    public ResponseEntity<String[]> verMisPdf(@PathVariable long id){
        file = new File("src/PDF/" + id);

            if(!file.exists())
                throw new ListaEmptyExcepcion("No contienes reportes realizados", ""+id);

        return new ResponseEntity<>(file.list(), HttpStatus.OK);
    }
}
