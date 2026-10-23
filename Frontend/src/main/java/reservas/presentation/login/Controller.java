package reservas.presentation.login;

import reservas.logic.Service;
import reservas.logic.Usuario;
import reservas.presentation.Sesion;

public class Controller {

    private View view;
    private Model model;

    public Controller(View view, Model model) {
        //contructor que genera el view y el model que necesita el controller
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void login(String id, String clave) throws Exception {
        Usuario usuario = Service.instance().login(id, clave);
        model.setCurrent(usuario);
        Sesion.setUsuario(usuario);
        view.dispose();
    }

    public void cancelar() {
        view.dispose();
    }

    public void cambiarClave(String id, String clave) throws Exception {

        // Primero verificamos que el usuario y la clave actual sean correctos
        Usuario usuario = Service.instance().login(id, clave);

        // MVC de cambio de clave
        reservas.presentation.cambioclave.View cambioView =
                new reservas.presentation.cambioclave.View();

        reservas.presentation.cambioclave.Model cambioModel =
                new reservas.presentation.cambioclave.Model();

        new reservas.presentation.cambioclave.Controller(
                cambioView,
                cambioModel,
                usuario
        );

        // Creamos la ventana que contiene el JPanel del cambio de clave
        javax.swing.JDialog dialog =
                new javax.swing.JDialog(view, "Cambiar clave", true);

        dialog.setContentPane(cambioView.getPanel());
        dialog.pack();
        dialog.setLocationRelativeTo(view);
        dialog.setVisible(true);
    }
}