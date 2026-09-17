package reservas.presentation.funcionarios;

import reservas.logic.Funcionario;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JTextField funBuscarIdFld;
    private JTextField funBuscarNombreFld;
    private JTextField funIdFld;
    private JTextField funNombreFld;
    private JTextField funTelefonoFld;
    private JButton funBuscarFld;
    private JButton funImprimirFld;
    private JButton funGuardarFld;
    private JButton funBorrarFld;
    private JButton funLimpiarFld;
    private JTable funcionariosTbl;
    private JPanel panel;

    private Controller controller;
    private Model model;

    public View() {
        funGuardarFld.addActionListener(e -> save());
        funBuscarFld.addActionListener(
                e -> controller.search(
                        funBuscarIdFld.getText(),
                        funBuscarNombreFld.getText()
                )
        );
        funBorrarFld.addActionListener(e -> run(() -> controller.delete()));
        funLimpiarFld.addActionListener(e -> controller.clear());
        funImprimirFld.addActionListener(e -> run(() -> controller.print()));

        funcionariosTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && funcionariosTbl.getSelectedRow() >= 0) {
                int row = funcionariosTbl.convertRowIndexToModel(
                        funcionariosTbl.getSelectedRow()
                );
                controller.edit(row);
            }
        });
    }

    private Funcionario take() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(funIdFld.getText().trim());
        funcionario.setNombre(funNombreFld.getText().trim());
        funcionario.setTelefono(funTelefonoFld.getText().trim());
        return funcionario;
    }

    private void validate(Funcionario funcionario) throws Exception {
        if (funcionario.getId().isEmpty()) {
            throw new Exception("Debe ingresar el id");
        }
        if (funcionario.getNombre().isEmpty()) {
            throw new Exception("Debe ingresar el nombre");
        }
        if (funcionario.getTelefono().isEmpty()) {
            throw new Exception("Debe ingresar el telefono");
        }
    }

    private void save() {
        run(() -> {
            Funcionario funcionario = take();
            validate(funcionario);
            controller.save(funcionario);
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
        if (Model.CURRENT.equals(event.getPropertyName())) {
            Funcionario funcionario = model.getCurrent();

            funIdFld.setText(
                    funcionario == null || funcionario.getId() == null
                            ? ""
                            : funcionario.getId()
            );
            funNombreFld.setText(
                    funcionario == null || funcionario.getNombre() == null
                            ? ""
                            : funcionario.getNombre()
            );
            funTelefonoFld.setText(
                    funcionario == null || funcionario.getTelefono() == null
                            ? ""
                            : funcionario.getTelefono()
            );

            funIdFld.setEnabled(
                    funcionario == null
                            || funcionario.getId() == null
                            || funcionario.getId().trim().isEmpty()
            );
        }

        if (Model.LIST.equals(event.getPropertyName())) {
            funcionariosTbl.setModel(
                    new TableModel(
                            new int[]{TableModel.ID, TableModel.NOMBRE, TableModel.TELEFONO},
                            model.getList()
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
