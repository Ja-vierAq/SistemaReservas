package reservas.logic;
import reservas.data.Data;
import reservas.data.XmlPersister;

public class Service {
    private static Service theInstance;
    private Data data;
    private Service() throws Exception {
        data = XmlPersister.instance().load();
    }
    public static Service instance() throws Exception {
        if (theInstance == null) {
            theInstance = new Service();
        }
        return theInstance;
    }
    public Usuario login(String id, String clave) throws Exception {
        for (Usuario usuario : data.getUsuarios()) {
            if (usuario.getId().equals(id)
                    && usuario.getClave().equals(clave)) {
                return usuario;
            }
        }
        throw new Exception("Usuario o clave incorrectos");
    }
}