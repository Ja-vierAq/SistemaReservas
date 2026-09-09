package reservas.presentation.login;

import reservas.logic.Service;
import reservas.logic.Usuario;
import reservas.presentation.Sesion;

public class Controller {

    private View view;
    private Model model;

    public Controller(View view, Model model) {
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
}