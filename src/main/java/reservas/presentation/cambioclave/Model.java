package reservas.presentation.cambioclave;

import reservas.logic.Usuario;
import reservas.presentation.AbstractModel;

import java.beans.PropertyChangeListener;

public class Model extends AbstractModel {
    private Usuario current;

    public static final String CURRENT = "current";

    public Model() {
        current = new Usuario();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
    }

    public Usuario getCurrent() {
        return current;
    }

    public void setCurrent(Usuario current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }
}
