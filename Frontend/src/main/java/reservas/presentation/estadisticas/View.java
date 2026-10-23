package reservas.presentation.estadisticas;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class View implements PropertyChangeListener {
    private JTextField estRecDesdeFld;
    private JTextField estRecHastaFld;
    private JTextField estActDesdeFld;
    private JTextField estActHastaFld;
    private JButton estRecCargarFld;
    private JButton estActCargarFld;
    private JTable estadisticasRecursosTbl;
    private JTable estadisticasActividadesTbl;
    private JPanel graficoRecursosPnl;
    private JPanel graficoActividadesPnl;
    private JPanel panel;

    private Controller controller;
    private Model model;

    public View() {
        LocalDate hoy = LocalDate.now();

        estRecDesdeFld.setText(hoy.minusMonths(1).toString());
        estRecHastaFld.setText(hoy.toString());
        estActDesdeFld.setText(hoy.minusMonths(1).toString());
        estActHastaFld.setText(hoy.toString());

        graficoRecursosPnl.setLayout(new BorderLayout());
        graficoActividadesPnl.setLayout(new BorderLayout());

        estRecCargarFld.addActionListener(e -> loadRecursos());
        estActCargarFld.addActionListener(e -> loadActividades());
    }

    private LocalDate takeFecha(JTextField field) throws Exception {
        try {
            return LocalDate.parse(field.getText().trim());
        } catch (Exception e) {
            throw new Exception("Fecha invalida. Use AAAA-MM-DD");
        }
    }

    private void validate(LocalDate desde, LocalDate hasta) throws Exception {
        if (desde.isAfter(hasta)) {
            throw new Exception("La fecha desde no puede ser posterior a hasta");
        }
    }

    private void loadRecursos() {
        run(() -> {
            LocalDate desde = takeFecha(estRecDesdeFld);
            LocalDate hasta = takeFecha(estRecHastaFld);

            validate(desde, hasta);
            controller.loadRecursos(desde, hasta);
        });
    }

    private void loadActividades() {
        run(() -> {
            LocalDate desde = takeFecha(estActDesdeFld);
            LocalDate hasta = takeFecha(estActHastaFld);

            validate(desde, hasta);
            controller.loadActividades(desde, hasta);
        });
    }

    private void run(Action action) {
        try {
            action.run();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    panel,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (Model.RECURSOS.equals(event.getPropertyName())) {
            actualizarRecursos();
        }

        if (Model.ACTIVIDADES.equals(event.getPropertyName())) {
            actualizarActividades();
        }
    }

    private void actualizarRecursos() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Categoria", "Cantidad"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Integer> entry : model.getRecursos().entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            dataset.addValue(entry.getValue(), "Reservas", entry.getKey());
        }

        estadisticasRecursosTbl.setModel(tableModel);

        JFreeChart chart = ChartFactory.createBarChart(
                "Recursos reservados por categoria",
                "Categoria",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        mostrarGrafico(graficoRecursosPnl, chart);
    }

    private void actualizarActividades() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[]{"Semana", "Actividades"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<LocalDate, Integer> entry : model.getActividades().entrySet()) {
            String semana = entry.getKey().format(formatter);
            tableModel.addRow(new Object[]{semana, entry.getValue()});
            dataset.addValue(entry.getValue(), "Actividades", semana);
        }

        estadisticasActividadesTbl.setModel(tableModel);

        JFreeChart chart = ChartFactory.createLineChart(
                "Actividades por semana",
                "Semana",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        mostrarGrafico(graficoActividadesPnl, chart);
    }

    private void mostrarGrafico(JPanel contenedor, JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);

        contenedor.removeAll();
        contenedor.add(chartPanel, BorderLayout.CENTER);
        contenedor.revalidate();
        contenedor.repaint();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        if (this.model != null) {
            this.model.removePropertyChangeListener(this);
        }

        this.model = model;
        model.addPropertyChangeListener(this);
    }

    public JPanel getPanel() {
        return panel;
    }

    private interface Action {
        void run() throws Exception;
    }
}
