package reservas.logic;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva {
//atributos q se piden segun el enunciado
    private String id;
    private Funcionario funcionario;
    private String actividad;
    private String estado;
    //los atributos de fechas estan en LocalDate/time para hecer mas facil futuras comparaciones
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    //las listas de los recursos y categorias como arreglos
    //-la categoria es lo que se solicitó al hacer la reserva mientras
    //que recurso es lo que el programa asigna oficialmente
    private List<Categoria> categorias;
    private List<Recurso> recursos;

    public Reserva() {
        categorias = new ArrayList<>();
        recursos = new ArrayList<>();
    }
    public Reserva(String id,Funcionario funcionario,String actividad,LocalDate fecha,LocalTime horaInicio,LocalTime horaFin) {
        this.id = id;
        this.funcionario = funcionario;
        this.actividad = actividad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.categorias = new ArrayList<>();
        this.recursos = new ArrayList<>();
        this.estado = "ACTIVA";
    }

    //gets y sets
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Funcionario getFuncionario() {return funcionario;}
    public void setFuncionario(Funcionario funcionario) {this.funcionario = funcionario;}

    public String getActividad() {return actividad;}
    public void setActividad(String actividad) {this.actividad = actividad;}

    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}

    public LocalTime getHoraInicio() {return horaInicio;}
    public void setHoraInicio(LocalTime horaInicio) {this.horaInicio = horaInicio;}

    public LocalTime getHoraFin() {return horaFin;}
    public void setHoraFin(LocalTime horaFin) {this.horaFin = horaFin;}

    public List<Categoria> getCategorias() {return categorias;}
    public void setCategorias(List<Categoria> categorias) {this.categorias = categorias;}

    public List<Recurso> getRecursos() {return recursos;}
    public void setRecursos(List<Recurso> recursos) {this.recursos = recursos;}

    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}
}