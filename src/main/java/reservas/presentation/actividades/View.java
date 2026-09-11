package reservas.presentation.actividades;

import javax.swing.*;

public class View {
    private JTextField actFechaReferenciaFld;
    private JButton actSeleccionarFechaFld;
    private JButton actCargarFld;
    private JButton actImprimirFld;
    private JTable actividadesTbl;
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
