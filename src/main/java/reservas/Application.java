package reservas;

import reservas.logic.Service;
import reservas.presentation.Sesion;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Application {
    public static void main(String[] args) {
        doLogin();
        if (Sesion.isLoggedIn()) {
            System.out.println("Login correcto: " + Sesion.getUsuario().getId());
            System.out.println("Rol: " + Sesion.getUsuario().getRol());
            doRun();
        } else {
            System.out.println("Aplicacion cerrada sin iniciar sesion");
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

        // Igual que en el ejemplo del profesor: persistir los datos al cerrar.
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Service.instance().stop();
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();
        window.setContentPane(tabbedPane);

        switch (Sesion.getUsuario().getRol()) {
            case "ADMIN": {
                reservas.presentation.funcionarios.View funcionariosView =
                        new reservas.presentation.funcionarios.View();
                reservas.presentation.funcionarios.Model funcionariosModel =
                        new reservas.presentation.funcionarios.Model();
                new reservas.presentation.funcionarios.Controller(funcionariosView, funcionariosModel);

                reservas.presentation.categorias.View categoriasView =
                        new reservas.presentation.categorias.View();
                reservas.presentation.categorias.Model categoriasModel =
                        new reservas.presentation.categorias.Model();
                new reservas.presentation.categorias.Controller(categoriasView, categoriasModel);

                reservas.presentation.recursos.View recursosView =
                        new reservas.presentation.recursos.View();
                reservas.presentation.recursos.Model recursosModel =
                        new reservas.presentation.recursos.Model();
                new reservas.presentation.recursos.Controller(recursosView, recursosModel);

                reservas.presentation.calendarizacion.View calendarizacionView =
                        new reservas.presentation.calendarizacion.View();
                reservas.presentation.calendarizacion.Model calendarizacionModel =
                        new reservas.presentation.calendarizacion.Model();
                new reservas.presentation.calendarizacion.Controller(calendarizacionView, calendarizacionModel);

                reservas.presentation.actividades.View actividadesView =
                        new reservas.presentation.actividades.View();
                reservas.presentation.actividades.Model actividadesModel =
                        new reservas.presentation.actividades.Model();
                new reservas.presentation.actividades.Controller(actividadesView, actividadesModel);

                reservas.presentation.estadisticas.View estadisticasView =
                        new reservas.presentation.estadisticas.View();
                reservas.presentation.estadisticas.Model estadisticasModel =
                        new reservas.presentation.estadisticas.Model();
                new reservas.presentation.estadisticas.Controller(estadisticasView, estadisticasModel);

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
                reservas.presentation.reservas.Model reservasModel =
                        new reservas.presentation.reservas.Model();
                new reservas.presentation.reservas.Controller(reservasView, reservasModel);

                reservas.presentation.calendarizacion.View calendarizacionView =
                        new reservas.presentation.calendarizacion.View();
                reservas.presentation.calendarizacion.Model calendarizacionModel =
                        new reservas.presentation.calendarizacion.Model();
                new reservas.presentation.calendarizacion.Controller(calendarizacionView, calendarizacionModel);

                reservas.presentation.actividades.View actividadesView =
                        new reservas.presentation.actividades.View();
                reservas.presentation.actividades.Model actividadesModel =
                        new reservas.presentation.actividades.Model();
                new reservas.presentation.actividades.Controller(actividadesView, actividadesModel);

                reservas.presentation.estadisticas.View estadisticasView =
                        new reservas.presentation.estadisticas.View();
                reservas.presentation.estadisticas.Model estadisticasModel =
                        new reservas.presentation.estadisticas.Model();
                new reservas.presentation.estadisticas.Controller(estadisticasView, estadisticasModel);

                tabbedPane.addTab("Reservas", reservasView.getPanel());
                tabbedPane.addTab("Calendarizacion", calendarizacionView.getPanel());
                tabbedPane.addTab("Actividades", actividadesView.getPanel());
                tabbedPane.addTab("Estadisticas", estadisticasView.getPanel());
                break;
            }

            default:
                JOptionPane.showMessageDialog(
                        null,
                        "Rol de usuario no reconocido: " + Sesion.getUsuario().getRol(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
        }

        window.setSize(1000, 700);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
