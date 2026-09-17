package reservas.logic.ai;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaIA {
    private String actividad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private List<String> categorias;

    public ReservaIA() {categorias = new ArrayList<>();}
    public ReservaIA(String actividad, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, List<String> categorias) {
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.categorias = categorias;
    }

    public String getActividad() {return actividad;}
    public void setActividad(String actividad) {this.actividad = actividad;}

    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}

    public LocalTime getHoraInicio() {return horaInicio;}
    public void setHoraInicio(LocalTime horaInicio) {this.horaInicio = horaInicio;}

    public LocalTime getHoraFin() {return horaFin;}
    public void setHoraFin(LocalTime horaFin) {this.horaFin = horaFin;}

    public List<String> getCategorias() {return categorias;}
    public void setCategorias(List<String> categorias) {this.categorias = categorias;}
}