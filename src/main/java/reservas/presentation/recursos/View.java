package reservas.presentation.recursos;

import javax.swing.*;

public class View {
    private JComboBox recFiltroCategoriaFld;
    private JTextField recFiltroDescripcionFld;
    private JButton recBuscarFld;
    private JButton recImprimirFld;
    private JTextField recIdFld;
    private JComboBox recCategoriaFld;
    private JTextField recDescripcionFld;
    private JButton recGuardarFld;
    private JButton recBorrarFld;
    private JButton recLimpiarFld;
    private JTable recursosTbl;
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
