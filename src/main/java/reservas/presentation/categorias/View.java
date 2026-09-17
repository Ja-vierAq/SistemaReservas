package reservas.presentation.categorias;

import reservas.logic.Categoria;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JTextField catBuscarDescripcionFld;
    private JButton catBuscarFld;
    private JButton catImprimirFld;
    private JTextField catIdFld;
    private JTextField catDescripcionFld;
    private JButton catGuardarFld;
    private JButton catBorrarFld;
    private JButton catLimpiarFld;
    private JTable categoriasTbl;
    private JPanel panel;

    private Controller controller;
    private Model model;

    public View() {
        catGuardarFld.addActionListener(e -> save());
        catBuscarFld.addActionListener(
                e -> controller.search(catBuscarDescripcionFld.getText())
        );
        catBorrarFld.addActionListener(e -> run(() -> controller.delete()));
        catLimpiarFld.addActionListener(e -> controller.clear());
        catImprimirFld.addActionListener(e -> run(() -> controller.print()));

        categoriasTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && categoriasTbl.getSelectedRow() >= 0) {
                int row = categoriasTbl.convertRowIndexToModel(
                        categoriasTbl.getSelectedRow()
                );
                controller.edit(row);
            }
        });
    }

    private Categoria take() {
        Categoria categoria = new Categoria();
        categoria.setId(catIdFld.getText().trim());
        categoria.setDescripcion(catDescripcionFld.getText().trim());
        return categoria;
    }

    private void validate(Categoria categoria) throws Exception {
        if (categoria.getDescripcion().isEmpty()) {
            throw new Exception("Debe ingresar la descripcion");
        }
    }

    private void save() {
        run(() -> {
            Categoria categoria = take();
            validate(categoria);
            controller.save(categoria);
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
            Categoria categoria = model.getCurrent();

            catIdFld.setText(
                    categoria == null || categoria.getId() == null
                            ? ""
                            : categoria.getId()
            );
            catDescripcionFld.setText(
                    categoria == null || categoria.getDescripcion() == null
                            ? ""
                            : categoria.getDescripcion()
            );
            catIdFld.setEnabled(
                    categoria == null
                            || categoria.getId() == null
                            || categoria.getId().trim().isEmpty()
            );
        }

        if (Model.LIST.equals(event.getPropertyName())) {
            categoriasTbl.setModel(
                    new TableModel(
                            new int[]{TableModel.ID, TableModel.DESCRIPCION},
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
