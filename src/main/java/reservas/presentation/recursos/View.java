package reservas.presentation.recursos;

import reservas.logic.Categoria;
import reservas.logic.Recurso;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JComboBox<Categoria> recFiltroCategoriaFld;
    private JComboBox<Categoria> recCategoriaFld;
    private JTextField recFiltroDescripcionFld;
    private JTextField recIdFld;
    private JTextField recDescripcionFld;
    private JButton recBuscarFld;
    private JButton recImprimirFld;
    private JButton recGuardarFld;
    private JButton recBorrarFld;
    private JButton recLimpiarFld;
    private JTable recursosTbl;
    private JPanel panel;

    private Controller controller;
    private Model model;

    public View() {
        recGuardarFld.addActionListener(e -> save());
        recBuscarFld.addActionListener(
                e -> controller.search(
                        (Categoria) recFiltroCategoriaFld.getSelectedItem(),
                        recFiltroDescripcionFld.getText()
                )
        );
        recBorrarFld.addActionListener(e -> run(() -> controller.delete()));
        recLimpiarFld.addActionListener(e -> controller.clear());
        recImprimirFld.addActionListener(e -> run(() -> controller.print()));

        recursosTbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && recursosTbl.getSelectedRow() >= 0) {
                int row = recursosTbl.convertRowIndexToModel(
                        recursosTbl.getSelectedRow()
                );
                controller.edit(row);
            }
        });

        ListCellRenderer<? super Categoria> renderer = new DefaultListCellRenderer() {
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
                                : "Todas"
                );
                return this;
            }
        };

        recCategoriaFld.setRenderer(renderer);
        recFiltroCategoriaFld.setRenderer(renderer);
    }

    private Recurso take() {
        Recurso recurso = new Recurso();
        recurso.setId(recIdFld.getText().trim());
        recurso.setCategoria((Categoria) recCategoriaFld.getSelectedItem());
        recurso.setDescripcion(recDescripcionFld.getText().trim());
        return recurso;
    }

    private void validate(Recurso recurso) throws Exception {
        if (recurso.getId().isEmpty()) {
            throw new Exception("Debe ingresar el id");
        }
        if (recurso.getCategoria() == null) {
            throw new Exception("Debe seleccionar una categoria");
        }
        if (recurso.getDescripcion().isEmpty()) {
            throw new Exception("Debe ingresar la descripcion");
        }
    }

    private void save() {
        run(() -> {
            Recurso recurso = take();
            validate(recurso);
            controller.save(recurso);
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
        if (Model.CATEGORIAS.equals(event.getPropertyName())) {
            Categoria selected = (Categoria) recCategoriaFld.getSelectedItem();

            DefaultComboBoxModel<Categoria> categoriesModel =
                    new DefaultComboBoxModel<>();
            for (Categoria categoria : model.getCategorias()) {
                categoriesModel.addElement(categoria);
            }
            recCategoriaFld.setModel(categoriesModel);

            DefaultComboBoxModel<Categoria> filterModel =
                    new DefaultComboBoxModel<>();
            filterModel.addElement(null);
            for (Categoria categoria : model.getCategorias()) {
                filterModel.addElement(categoria);
            }
            recFiltroCategoriaFld.setModel(filterModel);

            if (selected != null) {
                recCategoriaFld.setSelectedItem(selected);
            }
        }

        if (Model.CURRENT.equals(event.getPropertyName())) {
            Recurso recurso = model.getCurrent();

            recIdFld.setText(
                    recurso == null || recurso.getId() == null
                            ? ""
                            : recurso.getId()
            );
            recDescripcionFld.setText(
                    recurso == null || recurso.getDescripcion() == null
                            ? ""
                            : recurso.getDescripcion()
            );
            recCategoriaFld.setSelectedItem(
                    recurso == null ? null : recurso.getCategoria()
            );
            recIdFld.setEnabled(
                    recurso == null
                            || recurso.getId() == null
                            || recurso.getId().trim().isEmpty()
            );
        }

        if (Model.LIST.equals(event.getPropertyName())) {
            recursosTbl.setModel(
                    new TableModel(
                            new int[]{
                                    TableModel.ID,
                                    TableModel.CATEGORIA,
                                    TableModel.DESCRIPCION
                            },
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
