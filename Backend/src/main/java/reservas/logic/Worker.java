
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

            // usuarios
            case Protocol.USUARIO_LOGIN -> service.login((String) argumentos[0], (String) argumentos[1]);
            case Protocol.USUARIO_READ -> service.read((Usuario) argumentos[0]);
            case Protocol.USUARIO_CAMBIAR_CLAVE -> {
                service.cambiarClave((Usuario) argumentos[0], (String) argumentos[1], (String) argumentos[2]);
                yield null;
            }

            // funcionarios
            case Protocol.FUNCIONARIO_CREATE -> {
                service.create((Funcionario) argumentos[0]);
                yield null;
            }
            case Protocol.FUNCIONARIO_READ -> service.read((Funcionario) argumentos[0]);
            case Protocol.FUNCIONARIO_UPDATE -> {
                service.update((Funcionario) argumentos[0]);
                yield null;
            }
            case Protocol.FUNCIONARIO_DELETE -> {
                service.delete((Funcionario) argumentos[0]);
                yield null;
            }
            case Protocol.FUNCIONARIO_SEARCH -> service.search((Funcionario) argumentos[0]);
            case Protocol.FUNCIONARIO_FIND_ALL -> service.findAllFuncionarios();

            // categorias
            case Protocol.CATEGORIA_CREATE -> {
                service.create((Categoria) argumentos[0]);
                yield null;
            }
            case Protocol.CATEGORIA_READ -> service.read((Categoria) argumentos[0]);
            case Protocol.CATEGORIA_UPDATE -> {
                service.update((Categoria) argumentos[0]);
                yield null;
            }
            case Protocol.CATEGORIA_DELETE -> {
                service.delete((Categoria) argumentos[0]);
                yield null;
            }
            case Protocol.CATEGORIA_SEARCH -> service.search((Categoria) argumentos[0]);
            case Protocol.CATEGORIA_FIND_ALL -> service.findAllCategorias();

            // recursos
            case Protocol.RECURSO_CREATE -> {
                service.create((Recurso) argumentos[0]);
                yield null;
            }
            case Protocol.RECURSO_READ -> service.read((Recurso) argumentos[0]);
            case Protocol.RECURSO_UPDATE -> {
                service.update((Recurso) argumentos[0]);
                yield null;
            }
            case Protocol.RECURSO_DELETE -> {
                service.delete((Recurso) argumentos[0]);
                yield null;
            }
            case Protocol.RECURSO_SEARCH -> service.search((Recurso) argumentos[0]);
            case Protocol.RECURSO_FIND_ALL -> service.findAllRecursos();

            // reservas
            case Protocol.RESERVA_CREATE -> {
                service.create((Reserva) argumentos[0]);
                yield null;
            }
            case Protocol.RESERVA_READ -> service.read((Reserva) argumentos[0]);
            case Protocol.RESERVA_CANCEL -> {
                service.cancel((Reserva) argumentos[0]);
                yield null;
            }
            case Protocol.RESERVA_FIND_BY_FUNCIONARIO -> service.findReservas((Funcionario) argumentos[0]);
            case Protocol.RESERVA_FIND_ALL -> service.findAllReservas();

            // calendarios
            case Protocol.CALENDARIZACION_RECURSOS -> service.calendarizacionRecursos((java.time.LocalDate) argumentos[0], (Categoria) argumentos[1]);
            case Protocol.RESERVA_EN -> service.reservaEn((Recurso) argumentos[0], (java.time.LocalDate) argumentos[1], (java.time.LocalTime) argumentos[2]);
            case Protocol.INICIO_SEMANA -> service.inicioSemana((java.time.LocalDate) argumentos[0]);

            // actividades
            case Protocol.ACTIVIDADES_SEMANA -> service.actividadesSemana((java.time.LocalDate) argumentos[0]);
            case Protocol.ACTIVIDADES_EN -> service.actividadesEn((java.time.LocalDate) argumentos[0], (java.time.LocalTime) argumentos[1]);

            // stats
            case Protocol.ESTADISTICAS_RECURSOS -> service.estadisticasRecursos((java.time.LocalDate) argumentos[0], (java.time.LocalDate) argumentos[1]);
            case Protocol.ESTADISTICAS_ACTIVIDADES -> service.estadisticasActividades((java.time.LocalDate) argumentos[0], (java.time.LocalDate) argumentos[1]);

            // ia
            case Protocol.IA_EXTRAER -> service.extractIA((String) argumentos[0]);
            case Protocol.IA_BUSCAR_CATEGORIA -> service.findCategoriaByDescripcion((String) argumentos[0]);
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
