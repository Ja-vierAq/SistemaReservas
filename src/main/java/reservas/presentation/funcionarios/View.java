package reservas.presentation.funcionarios;

import javax.swing.*;

public class View {
    private JTextField funBuscarIdFld;
    private JTextField funBuscarNombreFld;
    private JButton funBuscarFld;
    private JButton funImprimirFld;
    private JTextField funIdFld;
    private JTextField funNombreFld;
    private JTextField funTelefonoFld;
    private JButton funGuardarFld;
    private JButton funBorrarFld;
    private JButton funLimpiarFld;
    private JTable funcionariosTbl;
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
