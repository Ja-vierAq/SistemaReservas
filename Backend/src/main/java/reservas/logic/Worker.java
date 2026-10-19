
package reservas.logic;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Worker implements Runnable {

    private final Server server;
    private final Socket socket;
    private final IService service;
    private ObjectOutputStream salida;

    public Worker(Server server, Socket socket, IService service) {
        this.server = server;
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (socket) {

            salida = new ObjectOutputStream(socket.getOutputStream());
            salida.flush();
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            while (true) {
                int operacion = entrada.readInt();
                if (operacion == Protocol.DISCONNECT) break;

                int estado = Protocol.ERROR_NO_ERROR;
                Object resultado;
                try {
                    Object[] argumentos = (Object[]) entrada.readObject();
                    resultado = ejecutar(operacion, argumentos);

                } catch (Exception ex) {
                    estado = Protocol.ERROR_ERROR;
                    resultado = ex.getMessage() == null ? "Error procesando solicitud" : ex.getMessage();
                }
                responder(estado, resultado);
            }
        } catch (EOFException ex) {
            // el cliente cerro coneccion
        } catch (IOException ex) {
            System.err.println("Cliente desconectado: " + ex.getMessage());

        } finally {
            server.remove(this);
            System.out.println("Conexion finalizada: " + socket.getRemoteSocketAddress());
        }
    }

    private Object ejecutar(int operacion, Object[] argumentos) throws Exception {
        return switch (operacion) {
            case Protocol.USUARIO_LOGIN ->
                    service.login((String) argumentos[0], (String) argumentos[1]);
            case Protocol.CATEGORIA_FIND_ALL -> service.findAllCategorias();

            default -> throw new Exception("Operacion no implementada: " + operacion);
        };
    }

    private void responder(int estado, Object resultado)
            throws IOException {
        synchronized (salida) {
            salida.reset();
            salida.writeInt(estado);
            salida.writeObject(resultado);
            salida.flush();
        }
    }
}
