package reservas.presentation.estadisticas;

import javax.swing.*;

public class View {
    private JTextField estRecDesdeFld;
    private JTextField estRecHastaFld;
    private JButton estRecCargarFld;
    private JTable estadisticasRecursosTbl;
    private JPanel graficoRecursosPnl;
    private JTextField estActDesdeFld;
    private JTextField estActHastaFld;
    private JButton estActCargarFld;
    private JTable estadisticasActividadesTbl;
    private JPanel graficoActividadesPnl;
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
