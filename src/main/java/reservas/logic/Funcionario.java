package reservas.logic;

public class Funcionario {

    private String id;
    private String nombre;
    private String telefono;
    private Usuario usuario;

    public Funcionario() {}

    public Funcionario(String id, String nombre, String telefono, Usuario usuario) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}

    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}
}