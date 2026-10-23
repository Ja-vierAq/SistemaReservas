package reservas.presentation.login;

import reservas.logic.Usuario;

public class Model {
    private Usuario current;

    public Model() {current = new Usuario();}

    public Usuario getCurrent() {return current;}
    public void setCurrent(Usuario current) {this.current = current;}
}