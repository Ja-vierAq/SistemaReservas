package reservas.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class XmlPersister {

    private final String path;
    private File resolvedFile;
    private static XmlPersister theInstance;

    public static XmlPersister instance() {
        if (theInstance == null) {
            theInstance = new XmlPersister("data.xml");
        }
        return theInstance;
    }

    public XmlPersister(String path) {
        this.path = path;
    }

    private File resolveFile() {
        if (resolvedFile != null) {
            return resolvedFile;
        }

        File requested = new File(path);
        if (requested.isAbsolute()) {
            resolvedFile = requested;
            return resolvedFile;
        }

        // 1) Directorio de ejecución actual.
        File current = new File(System.getProperty("user.dir"));
        File candidate = new File(current, path);
        if (candidate.exists()) {
            resolvedFile = candidate;
            return resolvedFile;
        }

        // 2) Buscar hacia arriba desde el directorio de ejecución.
        File directory = current;
        for (int i = 0; i < 6 && directory != null; i++) {
            candidate = new File(directory, path);
            if (candidate.exists()) {
                resolvedFile = candidate;
                return resolvedFile;
            }
            directory = directory.getParentFile();
        }

        // 3) Buscar desde la ubicación donde se cargaron las clases (por ejemplo target/classes).
        //    Esto cubre configuraciones de IntelliJ cuyo Working Directory no sea el proyecto.
        try {
            File codeLocation = new File(
                    XmlPersister.class.getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            directory = codeLocation.isDirectory() ? codeLocation : codeLocation.getParentFile();
            for (int i = 0; i < 6 && directory != null; i++) {
                candidate = new File(directory, path);
                if (candidate.exists()) {
                    resolvedFile = candidate;
                    return resolvedFile;
                }
                directory = directory.getParentFile();
            }
        } catch (Exception ignored) {
            // La búsqueda por user.dir sigue siendo válida si esta estrategia no está disponible.
        }

        // Si todavía no existe, se usará el directorio actual al guardar.
        resolvedFile = new File(current, path);
        return resolvedFile;
    }

    public Data load() throws Exception {
        File file = resolveFile();
        if (!file.exists()) {
            throw new Exception("No se encontró el archivo de datos: " + file.getAbsolutePath());
        }

        JAXBContext context = JAXBContext.newInstance(Data.class);
        try (FileInputStream input = new FileInputStream(file)) {
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Data) unmarshaller.unmarshal(input);
        }
    }

    public synchronized void store(Data data) throws Exception {
        File file = resolveFile();
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new Exception("No se pudo crear la carpeta de datos: " + parent.getAbsolutePath());
        }

        File temp = new File(file.getAbsolutePath() + ".tmp");
        JAXBContext context = JAXBContext.newInstance(Data.class);

        try (FileOutputStream output = new FileOutputStream(temp)) {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(data, output);
            output.flush();
            output.getFD().sync();
        }

        try {
            Files.move(temp.toPath(), file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ex) {
            Files.move(temp.toPath(), file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } finally {
            if (temp.exists()) {
                temp.delete();
            }
        }
    }

    public String getResolvedPath() {
        return resolveFile().getAbsolutePath();
    }
}
