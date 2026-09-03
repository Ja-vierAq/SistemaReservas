package reservas.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlIDREF;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import reservas.data.LocalDateAdapter;
import reservas.data.LocalTimeAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Reserva {

    // atributos q se piden segun el enunciado

    @XmlID
    private String id;
    @XmlIDREF
    private Funcionario funcionario;
    private String actividad;
    private String estado;

    // los atributos de fechas estan en LocalDate/time
    // para hacer mas facil futuras comparaciones

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fecha;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaInicio;
    @XmlJavaTypeAdapter(LocalTimeAdapter.class)
    private LocalTime horaFin;
    // la categoria es lo que se solicitó al hacer la reserva
    // mientras que recurso es lo que el programa asigna oficialmente
    @XmlElementWrapper(name = "categorias")
    @XmlElement(name = "categoria")
    @XmlIDREF
    private List<Categoria> categorias;
    @XmlElementWrapper(name = "recursos")
    @XmlElement(name = "recurso")
    @XmlIDREF
    private List<Recurso> recursos;

    public Reserva() {
        categorias = new ArrayList<>();
        recursos = new ArrayList<>();
    }

    public Reserva(String id, Funcionario funcionario, String actividad, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

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

    // gets y sets
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Funcionario getFuncionario() {return funcionario;}
    public void setFuncionario(Funcionario funcionario) {this.funcionario = funcionario;}

    public String getActividad() {return actividad;}
    public void setActividad(String actividad) {this.actividad = actividad;}

    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha){this.fecha=fecha;}

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