package reservas.presentation.estadisticas;

import reservas.logic.Service;
import reservas.util.PdfReport;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        view.setController(this);
        view.setModel(model);
    }

    public void loadRecursos(LocalDate desde, LocalDate hasta) throws Exception {
        model.setRecursos(
                Service.instance().estadisticasRecursos(desde, hasta)
        );
    }

    public void loadActividades(LocalDate desde, LocalDate hasta) throws Exception {
        model.setActividades(
                Service.instance().estadisticasActividades(desde, hasta)
        );
    }

    public void printRecursos() throws Exception {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Categoria", "Cantidad"},
                0
        );

        for (Map.Entry<String, Integer> entry : model.getRecursos().entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }

        PdfReport.exportTable(
                view.getPanel(),
                "Estadisticas de Recursos",
                tableModel
        );
    }

    public void printActividades() throws Exception {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Semana", "Actividades"},
                0
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Map.Entry<LocalDate, Integer> entry : model.getActividades().entrySet()) {
            tableModel.addRow(
                    new Object[]{entry.getKey().format(formatter), entry.getValue()}
            );
        }

        PdfReport.exportTable(
                view.getPanel(),
                "Estadisticas de Actividades",
                tableModel
        );
    }
}
