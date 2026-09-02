package reservas;

import reservas.logic.Usuario;

public class Application {

    public static void main(String[] args) {
        System.out.println("================================================");
        System.out.println("Bienvenido al sistema de manejo de reservaciones");
        System.out.println("================================================");
        System.out.println("");
        Usuario user1=new Usuario("001","001","Administrador de aparcamiento");
        System.out.println(user1.mostrar());

    }

}