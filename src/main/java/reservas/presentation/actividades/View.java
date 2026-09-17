package reservas.presentation.actividades;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class View implements PropertyChangeListener {
    private JTextField actFechaReferenciaFld;
    private JButton actSeleccionarFechaFld;
    private JButton actCargarFld;
    private JButton actImprimirFld;
    private JTable actividadesTbl;
    private JPanel panel;

    private Controller controller;
    private Model model;

    public View() {
        actFechaReferenciaFld.setText(LocalDate.now().toString());

        actCargarFld.addActionListener(e -> load());
        actSeleccionarFechaFld.addActionListener(e -> askDate());
        actImprimirFld.addActionListener(e -> run(() -> controller.print()));
    }

    private LocalDate take() throws Exception {
        try {
            return LocalDate.parse(actFechaReferenciaFld.getText().trim());
        } catch (Exception e) {
            throw new Exception("Fecha invalida. Use AAAA-MM-DD");
        }
    }

    private void validate(LocalDate fecha) throws Exception {
        if (fecha == null) {
            throw new Exception("Debe indicar la fecha");
        }
    }

    private void load() {
        run(() -> {
            LocalDate fecha = take();
            validate(fecha);
            controller.load(fecha);
        });
    }

    private void askDate() {
        String value = JOptionPane.showInputDialog(
                panel,
                "Fecha (AAAA-MM-DD)",
                actFechaReferenciaFld.getText()
        );

        if (value != null) {
            actFechaReferenciaFld.setText(value.trim());
        }
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
        if (Model.SEMANA.equals(event.getPropertyName())) {
            if (model.getFechaReferencia() != null) {
                actFechaReferenciaFld.setText(
                        model.getFechaReferencia().toString()
                );
            }

            actividadesTbl.setModel(
                    new TableModel(
                            new int[]{
                                    TableModel.FECHA,
                                    TableModel.INICIO,
                                    TableModel.FIN,
                                    TableModel.ACTIVIDAD,
                                    TableModel.FUNCIONARIO
                            },
                            model.getActividades()
                    )
            );
        }
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
