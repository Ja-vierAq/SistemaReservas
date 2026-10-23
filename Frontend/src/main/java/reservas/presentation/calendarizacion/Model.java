package reservas.presentation.calendarizacion;

import reservas.logic.Categoria;
import reservas.logic.Recurso;
import reservas.logic.Reserva;
import reservas.presentation.AbstractModel;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Model extends AbstractModel {
    private LocalDate fecha;
    private Categoria categoria;
    private List<Categoria> categorias;
    private List<Recurso> recursos;
    private List<Reserva> reservas;

    public static final String CATEGORIAS = "categorias";
    public static final String CALENDARIZACION = "calendarizacion";

    public Model() {
        fecha = null;
        categoria = null;
        categorias = new ArrayList<>();
        recursos = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
        firePropertyChange(CATEGORIAS);
        firePropertyChange(CALENDARIZACION);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
        firePropertyChange(CATEGORIAS);
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setCalendarizacion(LocalDate fecha, Categoria categoria,
                                   List<Recurso> recursos, List<Reserva> reservas) {
        this.fecha = fecha;
        this.categoria = categoria;
        this.recursos = recursos;
        this.reservas = reservas;
        firePropertyChange(CALENDARIZACION);
    }
}
