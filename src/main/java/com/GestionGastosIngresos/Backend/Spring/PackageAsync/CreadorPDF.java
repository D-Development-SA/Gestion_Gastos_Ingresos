package com.GestionGastosIngresos.Backend.Spring.PackageAsync;

import com.GestionGastosIngresos.Backend.Spring.Entity.GastoIngreso;
import com.GestionGastosIngresos.Backend.Spring.Service.Contratos.IGastoIngresoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreadorPDF {
    @Autowired
    private IGastoIngresoService gastoIngresoService;
    private boolean infinito = true;
    @Async("ControlHilosRunAllTimeServiceExecutor")
    public void crearPDF() throws InterruptedException {
        int mes = LocalDate.now().getMonthValue();

        while(infinito){
            if(mes != LocalDate.now().getMonthValue()) {
                List<GastoIngreso> gastoIngresos = gastoIngresoService.findAll();
                Document document = new Document();
                Page pdfPage = pdfFile.getPages().add();
                Table pdfTable = new Table();

                int cont = 0;
                double subtotalIngresos = 0;
                double subtotalGastos = 0;

                pdfTable.setDefaultCellBorder(new BorderInfo(BorderSide.All, 1.0f, Color.getGray()));

                insertarDatosEnLaTabla(gastoIngresos, pdfTable, cont, subtotalIngresos, subtotalGastos);
                pdfPage.getParagraphs().add(pdfTable);
                document.save(LocalDateTime.now() +".pdf");

                System.out.println("Terminado! -> Datos insertados correctamente");
            }

            Thread.sleep(86400000);
        }
    }

    private void insertarDatosEnLaTabla(List<GastoIngreso> gastoIngresos, Table pdfTable, int cont, double subtotalIngresos, double subtotalGastos) {
        Row columnas = creandoCampos(pdfTable);

        subtotalIngresos = insertandoIngresos(gastoIngresos, pdfTable, cont, subtotalIngresos);

        Row filaGastoName = pdfTable.getRows().add();
        columnas.getCells().add("Gastos");
        cont = 0;

        subtotalGastos = insertandoGastos(gastoIngresos, pdfTable, cont, subtotalGastos);

        Row subT = pdfTable.getRows().add();
        subT.getCells().add("Subtotal");
        subT.getCells().add(subtotalGastos);

        Row Total = pdfTable.getRows().add();
        subTotal.getCells().add("Total");
        subTotal.getCells().add(subtotalIngresos - subtotalGastos);
    }

    private double insertandoGastos(List<GastoIngreso> gastoIngresos, Table pdfTable,
                                    int cont, double subtotalGastos) {
        for (int numFila = 1; numFila < gastoIngresos.size(); numFila++)
        {
            if(gastoIngresos.get(numFila).getGasto() != null){
                Row gastos = pdfTable.getRows().add();
                gastos.getCells().add(gastoIngresos.get(numFila).getFecha());
                gastos.getCells().add(gastoIngresos.get(numFila).getGasto());
                gastos.getCells().add("Observacion " + ++cont);
                subtotalGastos += gastoIngresos.get(numFila).getGasto();
            }
        }
        return subtotalGastos;
    }

    private double insertandoIngresos(List<GastoIngreso> gastoIngresos, Table pdfTable,
                                      int cont, double subtotalIngresos) {
        for (int numFila = 1; numFila < gastoIngresos.size(); numFila++)
        {
            if(gastoIngresos.get(numFila).getIngreso() != null){
                Row ingresos = pdfTable.getRows().add();
                ingresos.getCells().add(gastoIngresos.get(numFila).getFecha());
                ingresos.getCells().add(gastoIngresos.get(numFila).getIngreso());
                ingresos.getCells().add("Observacion " + ++cont);
                subtotalIngresos += gastoIngresos.get(numFila).getIngreso();
            }
        }
        Row subTotal = pdfTable.getRows().add();
        subTotal.getCells().add("Subtotal");
        subTotal.getCells().add(subtotalIngresos);

        return subtotalIngresos;
    }

    private Row creandoCampos(Table pdfTable) {
        Row columnas = pdfTable.getRows().add();
        columnas.getCells().add("Fecha");
        columnas.getCells().add("Monto");
        columnas.getCells().add("Observaciones");

        Row filaIngresoName = pdfTable.getRows().add();
        columnas.getCells().add("Ingresos");
        return columnas;
    }

    public void setInfinito(boolean infinito) {
        this.infinito = infinito;
    }
}
