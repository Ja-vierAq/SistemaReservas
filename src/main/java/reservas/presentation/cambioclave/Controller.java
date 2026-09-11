package reservas.presentation.cambioclave;

import reservas.logic.Service;
import reservas.presentation.Sesion;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setCurrent(Sesion.getUsuario());
    }

    public void changePassword(String claveActual, String claveNueva,
                               String confirmacion) throws Exception {
        if (claveNueva == null || !claveNueva.equals(confirmacion)) {
            throw new Exception("Las claves nuevas no coinciden");
        }

        Service.instance().cambiarClave(
                model.getCurrent(),
                claveActual,
                claveNueva
        );
    }

    public void clear() {
        model.setCurrent(Sesion.getUsuario());
    }

    public void cancel() {
        clear();
    }
}
