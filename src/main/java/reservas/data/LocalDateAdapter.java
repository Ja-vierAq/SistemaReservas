package reservas.data;
// como JAXB no maneja localdate, le creamos un adaptador
//en realidad solo convierte el localdate en un string con los datos
//para poder meterlo en el xlm. cuando lo necesita tambien hace el proceso
//contrario

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String value) {
        return LocalDate.parse(value);
    }
    @Override
    public String marshal(LocalDate value) {
        return value.toString();
    }
}