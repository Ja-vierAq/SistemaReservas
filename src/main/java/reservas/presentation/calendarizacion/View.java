package reservas.presentation.calendarizacion;

import javax.swing.*;

public class View {
    private JTextField calFechaFld;
    private JButton calSeleccionarFechaFld;
    private JComboBox calCategoriaFld;
    private JButton calCargarFld;
    private JButton calImprimirFld;
    private JTable calendarizacionTbl;
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
