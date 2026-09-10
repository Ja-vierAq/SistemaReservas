package reservas.presentation.login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        //listener
        loginIngresarFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    controller.login(loginIdFld.getText(), new String(loginClaveFld.getPassword()));

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginCancelarFld.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.cancelar();
            }
        });
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
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setController(Controller controller) {this.controller = controller;}
    public void setModel(Model model) {this.model = model;}
}