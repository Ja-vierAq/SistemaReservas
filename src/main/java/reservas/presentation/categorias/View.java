package reservas.presentation.categorias;

import javax.swing.*;

public class View {
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

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public JPanel getPanel() {
        return panel;
    }
}
