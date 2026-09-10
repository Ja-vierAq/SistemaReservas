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
                        + " ("
                        + Sesion.getUsuario().getRol()
                        + ")"
        );

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        switch (Sesion.getUsuario().getRol()) {

            case "ADMIN": {

                reservas.presentation.funcionarios.View funcionariosView =
                        new reservas.presentation.funcionarios.View();

                reservas.presentation.categorias.View categoriasView =
                        new reservas.presentation.categorias.View();

                reservas.presentation.recursos.View recursosView =
                        new reservas.presentation.recursos.View();

                reservas.presentation.calendarizacion.View calendarizacionView =
                        new reservas.presentation.calendarizacion.View();

                reservas.presentation.actividades.View actividadesView =
                        new reservas.presentation.actividades.View();

                reservas.presentation.estadisticas.View estadisticasView =
                        new reservas.presentation.estadisticas.View();


                tabbedPane.addTab("Funcionarios", funcionariosView.getPanel());
                tabbedPane.addTab("Categorias", categoriasView.getPanel());
                tabbedPane.addTab("Recursos", recursosView.getPanel());
                tabbedPane.addTab("Calendarizacion", calendarizacionView.getPanel());
                tabbedPane.addTab("Actividades", actividadesView.getPanel());
                tabbedPane.addTab("Estadisticas", estadisticasView.getPanel());

                break;
            }

            case "FUNCIONARIO": {

                reservas.presentation.reservas.View reservasView =
                        new reservas.presentation.reservas.View();

                reservas.presentation.calendarizacion.View calendarizacionView =
                        new reservas.presentation.calendarizacion.View();

                reservas.presentation.actividades.View actividadesView =
                        new reservas.presentation.actividades.View();

                reservas.presentation.estadisticas.View estadisticasView =
                        new reservas.presentation.estadisticas.View();


                tabbedPane.addTab("Reservas", reservasView.getPanel());
                tabbedPane.addTab("Calendarizacion", calendarizacionView.getPanel());
                tabbedPane.addTab("Actividades", actividadesView.getPanel());
                tabbedPane.addTab("Estadisticas", estadisticasView.getPanel());

                break;
            }
        }

        window.setSize(1000, 700);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}