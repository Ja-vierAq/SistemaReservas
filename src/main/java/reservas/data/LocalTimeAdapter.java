package reservas.data;
//Exactamente lo mismo del localdate pero con el localtime
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    @Override
    public LocalTime unmarshal(String value) {
        return LocalTime.parse(value);
    }
    @Override
    public String marshal(LocalTime value) {
        return value.toString();
    }
}