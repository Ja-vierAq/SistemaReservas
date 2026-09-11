package reservas.presentation.reservas;

import reservas.logic.Categoria;
import reservas.logic.Reserva;
import reservas.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private Reserva current;
    private Reserva selected;
    private List<Reserva> list;
    private List<Categoria> categorias;

    public static final String CURRENT = "current";
    public static final String SELECTED = "selected";
    public static final String LIST = "list";
    public static final String CATEGORIAS = "categorias";

    public Model() {
        current = new Reserva();
        selected = null;
        list = new ArrayList<>();
        categorias = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CURRENT);
        firePropertyChange(SELECTED);
        firePropertyChange(LIST);
        firePropertyChange(CATEGORIAS);
    }

    public Reserva getCurrent() {
        return current;
    }

    public void setCurrent(Reserva current) {
        this.current = current;
        firePropertyChange(CURRENT);
    }

    public Reserva getSelected() {
        return selected;
    }

    public void setSelected(Reserva selected) {
        this.selected = selected;
        firePropertyChange(SELECTED);
    }

    public List<Reserva> getList() {
        return list;
    }

    public void setList(List<Reserva> list) {
        this.list = list;
        firePropertyChange(LIST);
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        firePropertyChange(CATEGORIAS);
    }
}
