package reservas.presentation.cambioclave;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class View implements PropertyChangeListener {
    private JPasswordField ccClaveActualFld;
    private JPasswordField ccClaveNuevaFld;
    private JPasswordField ccClaveNuevaConfirmaFld;
    private JButton ccAceptarFld;
    private JButton ccCancelarFld;
    private JPanel panel;

    private Controller controller;
    private Model model;

    public View() {
        ccAceptarFld.addActionListener(e -> accept());
        ccCancelarFld.addActionListener(e -> {
            controller.cancel();
            java.awt.Window window = SwingUtilities.getWindowAncestor(panel);
            if (window != null) window.dispose();
        });
    }

    private String[] take() {
        return new String[]{
                new String(ccClaveActualFld.getPassword()),
                new String(ccClaveNuevaFld.getPassword()),
                new String(ccClaveNuevaConfirmaFld.getPassword())
        };
    }

    private void validate(String[] values) throws Exception {
        if (values[0].trim().isEmpty()) {
            throw new Exception("Debe ingresar la clave actual");
        }
        if (values[1].trim().isEmpty()) {
            throw new Exception("Debe ingresar la clave nueva");
        }
        if (!values[1].equals(values[2])) {
            throw new Exception("Las claves nuevas no coinciden");
        }
    }

    private void accept() {
        try {
            String[] values = take();
            validate(values);

            controller.changePassword(values[0], values[1], values[2]);

            JOptionPane.showMessageDialog(
                    panel,
                    "Clave actualizada correctamente"
            );
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    panel,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {
        ccClaveActualFld.setText("");
        ccClaveNuevaFld.setText("");
        ccClaveNuevaConfirmaFld.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (Model.CURRENT.equals(event.getPropertyName())) {
            clearFields();
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
}
