package reservas.presentation.cambioclave;

import javax.swing.*;

public class View {
    private JPasswordField ccClaveActualFld;
    private JPasswordField ccClaveNuevaFld;
    private JPasswordField ccClaveNuevaConfirmaFld;
    private JButton ccAceptarFld;
    private JButton ccCancelarFld;
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
