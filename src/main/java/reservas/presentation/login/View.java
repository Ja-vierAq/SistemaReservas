package reservas.presentation.login;

import javax.swing.*;

public class View extends JDialog {

    private JPanel panel;
    private JTextField loginIdFld;
    private JPasswordField loginClaveFld;
    private JButton loginIngresarFld;
    private JButton loginCancelarFld;
    private JButton loginCambiarFld;

    private Controller controller;
    private Model model;

    public View() {

        setContentPane(panel);
        setModal(true);
        setTitle("Sistema de Reservas - Login");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        loginIngresarFld.addActionListener(e -> login());

        loginCancelarFld.addActionListener(e ->
                controller.cancelar()
        );

        pack();
        setLocationRelativeTo(null);
    }

    private void login() {

        try {

            String id = loginIdFld.getText();
            String clave = new String(loginClaveFld.getPassword());

            if (id.isBlank() || clave.isBlank()) {
                throw new Exception("Debe ingresar ID y clave");
            }

            controller.login(id, clave);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Login",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}