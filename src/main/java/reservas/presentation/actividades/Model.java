package reservas.presentation.actividades;

import reservas.logic.Reserva;
import reservas.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private LocalDate fechaReferencia;
    private LocalDate inicioSemana;
    private List<Reserva> actividades;

    public static final String SEMANA = "semana";

    public Model() {
        fechaReferencia = null;
        inicioSemana = null;
        actividades = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(SEMANA);
    }

    public LocalDate getFechaReferencia() {
        return fechaReferencia;
    }

    public LocalDate getInicioSemana() {
        return inicioSemana;
    }

    public List<Reserva> getActividades() {
        return actividades;
    }

    public void setSemana(LocalDate fechaReferencia, LocalDate inicioSemana, List<Reserva> actividades) {
        this.fechaReferencia = fechaReferencia;
        this.inicioSemana = inicioSemana;
        this.actividades = actividades;
        firePropertyChange(SEMANA);
    }
}
