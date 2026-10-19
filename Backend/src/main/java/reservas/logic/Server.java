package reservas.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final List<Worker> workers = new ArrayList<>();
    public void run() {
        IService service = Service.instance();
        try (ServerSocket servidor = new ServerSocket(Protocol.PORT)) {
            System.out.println("Backend escuchando en el puerto " + Protocol.PORT);
            while (true) {
                Socket socket = servidor.accept();
                Worker worker = new Worker(this, socket, service);
                synchronized (workers) {workers.add(worker);}
                new Thread(worker, "Worker-" + socket.getPort()).start();
            }
        } catch (IOException ex) {
            System.err.println("Error en el servidor: " + ex.getMessage());
        }
    }
    public void remove(Worker worker) {
        synchronized (workers) {
            workers.remove(worker);
        }
    }
}

