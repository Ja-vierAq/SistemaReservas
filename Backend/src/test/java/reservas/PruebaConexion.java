package reservas;

import reservas.logic.Protocol;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class PruebaConexion {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket(Protocol.SERVER, Protocol.PORT)) {
            ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
            salida.flush();
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            salida.writeInt(Protocol.CATEGORIA_FIND_ALL);
            salida.writeObject(new Object[0]);
            salida.flush();
            int estado = entrada.readInt();
            Object resultado = entrada.readObject();
            if (estado == Protocol.ERROR_NO_ERROR) {
                System.out.println("Categorias recibidas: " + ((List<?>) resultado).size());
                System.out.println(resultado);
            } else {
                System.err.println("Error del servidor: " + resultado);
            }
            salida.writeInt(Protocol.DISCONNECT);
            salida.flush();
        }
    }
}

