package reservas;

import reservas.presentation.Sesion;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        doLogin();
        if (Sesion.isLoggedIn()) {
            System.out.println("Login correcto: "+Sesion.getUsuario().getId());
            System.out.println("Rol: "+Sesion.getUsuario().getRol());
            doRun();
        }
        else {
            System.out.println("Aplicación cerrada sin iniciar sesión");
        }
    }

    private static void doLogin() {
        reservas.presentation.login.View view = new reservas.presentation.login.View();
        reservas.presentation.login.Model model = new reservas.presentation.login.Model();
        new reservas.presentation.login.Controller(view, model);
        view.setVisible(true);
    }
    private static void doRun() {

        JFrame window = new JFrame();

        window.setTitle(
                "Sistema de Reservas - "
                        + Sesion.getUsuario().getId()
                        + " (" + Sesion.getUsuario().getRol() + ")"
        );

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(800, 600);
        window.setLocationRelativeTo(null);

        JLabel label = new JLabel(
                "Bienvenido " + Sesion.getUsuario().getId(),
                SwingConstants.CENTER
        );

        window.add(label);

        window.setVisible(true);
    }
}