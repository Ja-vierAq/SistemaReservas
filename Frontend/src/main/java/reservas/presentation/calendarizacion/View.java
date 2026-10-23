package reservas.presentation.calendarizacion;

import reservas.logic.Categoria;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;

public class View implements PropertyChangeListener {
    private JTextField calFechaFld;
    private JButton calSeleccionarFechaFld;
    private JButton calCargarFld;
    private JButton calImprimirFld;
    private JComboBox<Categoria> calCategoriaFld;
    private JTable calendarizacionTbl;
    private JPanel panel;

    private Controller controller;
    private Model model;

    public View() {
        calFechaFld.setText(LocalDate.now().toString());

        calCargarFld.addActionListener(e -> load());
        calSeleccionarFechaFld.addActionListener(e -> askDate());
        calImprimirFld.addActionListener(e -> run(() -> controller.print()));

        calCategoriaFld.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

                setText(
                        value instanceof Categoria
                                ? ((Categoria) value).getDescripcion()
                                : ""
                );
                return this;
            }
        });
    }

    private LocalDate take() throws Exception {
        try {
            return LocalDate.parse(calFechaFld.getText().trim());
        } catch (Exception e) {
            throw new Exception("Fecha invalida. Use AAAA-MM-DD");
        }
    }

    private void validate(LocalDate fecha, Categoria categoria) throws Exception {
        if (fecha == null) {
            throw new Exception("Debe indicar la fecha");
        }
        if (categoria == null) {
            throw new Exception("Debe seleccionar una categoria");
        }
    }

    private void load() {
        run(() -> {
            LocalDate fecha = take();
            Categoria categoria = (Categoria) calCategoriaFld.getSelectedItem();

            validate(fecha, categoria);
            controller.load(fecha, categoria);
        });
    }

    private void askDate() {
        String value = JOptionPane.showInputDialog(
                panel,
                "Fecha (AAAA-MM-DD)",
                calFechaFld.getText()
        );

        if (value != null) {
            calFechaFld.setText(value.trim());
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
        if (Model.CATEGORIAS.equals(event.getPropertyName())) {
            DefaultComboBoxModel<Categoria> comboModel = new DefaultComboBoxModel<>();

            for (Categoria categoria : model.getCategorias()) {
                comboModel.addElement(categoria);
            }

            calCategoriaFld.setModel(comboModel);
        }

        if (Model.CALENDARIZACION.equals(event.getPropertyName())) {
            if (model.getFecha() != null) {
                calFechaFld.setText(model.getFecha().toString());
            }

            calendarizacionTbl.setModel(
                    new TableModel(
                            new int[]{
                                    TableModel.RECURSOS,
                                    TableModel.INICIO,
                                    TableModel.FIN,
                                    TableModel.ACTIVIDAD,
                                    TableModel.FUNCIONARIO
                            },
                            model.getReservas()
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
