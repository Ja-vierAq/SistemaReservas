package reservas.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XmlPersister {

    private String path;
    private static XmlPersister theInstance;
    public static XmlPersister instance() {
        if (theInstance == null) {
            theInstance = new XmlPersister("data.xml");
        }
        return theInstance;
    }

    public XmlPersister(String path) {this.path = path;}

    public Data load() throws Exception {
        JAXBContext context = JAXBContext.newInstance(Data.class);
        FileInputStream input = new FileInputStream(path);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Data data = (Data) unmarshaller.unmarshal(input);
        input.close();
        return data;
    }

    public void store(Data data) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Data.class);
        FileOutputStream output = new FileOutputStream(path);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(data, output);
        output.flush();
        output.close();
    }
}