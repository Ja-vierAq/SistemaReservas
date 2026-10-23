package reservas.logic;

import reservas.logic.ai.ReservaIA;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

// cumple la funcion del service para el frontend pero no aplica la logica
public class Service implements IService {

    private static Service theInstance;
    public static synchronized Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    private Service() {}

    public synchronized void connect() throws IOException {
        if (socket != null && !socket.isClosed()) return;
        Socket nuevoSocket = new Socket();
        try {
            nuevoSocket.connect(new InetSocketAddress(Protocol.SERVER, Protocol.PORT), 3000);
            ObjectOutputStream nuevaSalida = new ObjectOutputStream(nuevoSocket.getOutputStream());
            nuevaSalida.flush();
            ObjectInputStream nuevaEntrada = new ObjectInputStream(nuevoSocket.getInputStream());
            socket = nuevoSocket;
            salida = nuevaSalida;
            entrada = nuevaEntrada;
        } catch (IOException ex) {
            nuevoSocket.close();
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized <T> T solicitar(int operacion, Object... argumentos) throws Exception {
        if (socket == null || socket.isClosed()) connect();
        try {
            salida.writeInt(operacion);
            salida.reset();
            salida.writeObject(argumentos);
            salida.flush();
            int estado = entrada.readInt();
            Object resultado = entrada.readObject();
            if (estado != Protocol.ERROR_NO_ERROR) {
                throw new Exception(String.valueOf(resultado));
            }
            return (T) resultado;
        } catch (IOException | ClassNotFoundException ex) {
            close();
            throw new Exception("Error de comunicación con el Backend: " + ex.getMessage(), ex);
        }
    }

    private <T> T solicitarSinChecked(int operacion, Object... argumentos) {
        try {return solicitar(operacion, argumentos);}
        catch (Exception ex) {throw new IllegalStateException(ex.getMessage(), ex);}
    }

    // Usuarios
    @Override
    public Usuario login(String id, String clave) throws Exception {
        return solicitar(Protocol.USUARIO_LOGIN, id, clave);
    }

    @Override
    public Usuario read(Usuario usuario) throws Exception {
        return solicitar(Protocol.USUARIO_READ, usuario);
    }

    @Override
    public void cambiarClave(Usuario usuario, String claveActual, String claveNueva) throws Exception {
        solicitar(Protocol.USUARIO_CAMBIAR_CLAVE, usuario, claveActual, claveNueva);
    }

    // Funcionarios
    @Override
    public void create(Funcionario funcionario) throws Exception {
        solicitar(Protocol.FUNCIONARIO_CREATE, funcionario);
    }

    @Override
    public Funcionario read(Funcionario funcionario) throws Exception {
        return solicitar(Protocol.FUNCIONARIO_READ, funcionario);
    }

    @Override
    public void update(Funcionario funcionario) throws Exception {
        solicitar(Protocol.FUNCIONARIO_UPDATE, funcionario);
    }

    @Override
    public void delete(Funcionario funcionario) throws Exception {
        solicitar(Protocol.FUNCIONARIO_DELETE, funcionario);
    }

    @Override
    public List<Funcionario> search(Funcionario filtro) {
        return solicitarSinChecked(Protocol.FUNCIONARIO_SEARCH, filtro);
    }

    @Override
    public List<Funcionario> findAllFuncionarios() {
        return solicitarSinChecked(Protocol.FUNCIONARIO_FIND_ALL);
    }

    // Categorias
    @Override
    public void create(Categoria categoria) throws Exception {
        solicitar(Protocol.CATEGORIA_CREATE, categoria);
    }

    @Override
    public Categoria read(Categoria categoria) throws Exception {
        return solicitar(Protocol.CATEGORIA_READ, categoria);
    }

    @Override
    public void update(Categoria categoria) throws Exception {
        solicitar(Protocol.CATEGORIA_UPDATE, categoria);
    }

    @Override
    public void delete(Categoria categoria) throws Exception {
        solicitar(Protocol.CATEGORIA_DELETE, categoria);
    }

    @Override
    public List<Categoria> search(Categoria filtro) {
        return solicitarSinChecked(Protocol.CATEGORIA_SEARCH, filtro);
    }

    @Override
    public List<Categoria> findAllCategorias() {
        return solicitarSinChecked(Protocol.CATEGORIA_FIND_ALL);
    }

    // recursos
    @Override
    public void create(Recurso recurso) throws Exception {
        solicitar(Protocol.RECURSO_CREATE, recurso);
    }
    @Override
    public Recurso read(Recurso recurso) throws Exception {
        return solicitar(Protocol.RECURSO_READ, recurso);
    }
    @Override
    public void update(Recurso recurso) throws Exception {
        solicitar(Protocol.RECURSO_UPDATE, recurso);
    }
    @Override
    public void delete(Recurso recurso) throws Exception {
        solicitar(Protocol.RECURSO_DELETE, recurso);
    }
    @Override
    public List<Recurso> search(Recurso filtro) {
        return solicitarSinChecked(Protocol.RECURSO_SEARCH, filtro);
    }
    @Override
    public List<Recurso> findAllRecursos() {
        return solicitarSinChecked(Protocol.RECURSO_FIND_ALL);
    }

    // reserva
    @Override
    public void create(Reserva reserva) throws Exception {
        solicitar(Protocol.RESERVA_CREATE, reserva);
    }
    @Override
    public Reserva read(Reserva reserva) throws Exception {
        return solicitar(Protocol.RESERVA_READ, reserva);
    }
    @Override
    public void cancel(Reserva reserva) throws Exception {
        solicitar(Protocol.RESERVA_CANCEL, reserva);
    }
    @Override
    public List<Reserva> findReservas(Funcionario funcionario) throws Exception {
        return solicitar(Protocol.RESERVA_FIND_BY_FUNCIONARIO, funcionario);
    }
    @Override
    public List<Reserva> findAllReservas() {
        return solicitarSinChecked(Protocol.RESERVA_FIND_ALL);
    }

    // calendario y actividades
    @Override
    public List<Reserva> calendarizacionRecursos(LocalDate fecha, Categoria categoria) throws Exception {
        return solicitar(Protocol.CALENDARIZACION_RECURSOS, fecha, categoria);
    }
    @Override
    public Reserva reservaEn(Recurso recurso, LocalDate fecha, LocalTime hora) throws Exception {
        return solicitar(Protocol.RESERVA_EN, recurso, fecha, hora);
    }
    @Override
    public LocalDate inicioSemana(LocalDate fecha) {
        return solicitarSinChecked(Protocol.INICIO_SEMANA, fecha);
    }
    @Override
    public List<Reserva> actividadesSemana(LocalDate fecha) throws Exception {
        return solicitar(Protocol.ACTIVIDADES_SEMANA, fecha);
    }
    @Override
    public List<Reserva> actividadesEn(LocalDate fecha, LocalTime hora) throws Exception {
        return solicitar(Protocol.ACTIVIDADES_EN, fecha, hora);
    }

    // stats
    @Override
    public Map<String, Integer> estadisticasRecursos(LocalDate desde, LocalDate hasta) throws Exception {
        return solicitar(Protocol.ESTADISTICAS_RECURSOS, desde, hasta);
    }
    @Override
    public Map<LocalDate, Integer> estadisticasActividades(LocalDate desde, LocalDate hasta) throws Exception {
        return solicitar(Protocol.ESTADISTICAS_ACTIVIDADES, desde, hasta);
    }

    // ia
    @Override
    public ReservaIA extractIA(String frase) throws Exception {
        return solicitar(Protocol.IA_EXTRAER, frase);
    }
    @Override
    public Categoria findCategoriaByDescripcion(String descripcion) {
        return solicitarSinChecked(Protocol.IA_BUSCAR_CATEGORIA, descripcion);
    }
    public synchronized void close() {
        if (socket != null) {
            try {
                if (!socket.isClosed() && salida != null) {
                    salida.writeInt(Protocol.DISCONNECT);
                    salida.flush();
                }
            } catch (IOException ignored) {
                // El servidor ya podria estar desconectado.
            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
        socket = null;
        salida = null;
        entrada = null;
    }
}
