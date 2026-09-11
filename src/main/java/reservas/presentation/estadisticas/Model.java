package reservas.presentation.estadisticas;

import reservas.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Model extends AbstractModel {
    private Map<String, Integer> recursos;
    private Map<LocalDate, Integer> actividades;

    public static final String RECURSOS = "recursos";
    public static final String ACTIVIDADES = "actividades";

    public Model() {
        recursos = new LinkedHashMap<>();
        actividades = new LinkedHashMap<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(RECURSOS);
        firePropertyChange(ACTIVIDADES);
    }

    public Map<String, Integer> getRecursos() {
        return recursos;
    }

    public void setRecursos(Map<String, Integer> recursos) {
        this.recursos = recursos;
        firePropertyChange(RECURSOS);
    }

    public Map<LocalDate, Integer> getActividades() {
        return actividades;
    }

    public void setActividades(Map<LocalDate, Integer> actividades) {
        this.actividades = actividades;
        firePropertyChange(ACTIVIDADES);
    }
}
