package reservas.logic.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ReservaExtractorService {

    @SystemMessage("""
        Eres un asistente especializado en extraer información
        de reservas de espacios y recursos.
        Reglas estrictas:
        1. "actividad" es el motivo o nombre de la actividad que realizará
           el usuario. Ejemplos:
           "reunión de trabajo",
           "capacitación",
           "presentación de proyecto".
        2. La fecha siempre debe ser formato ISO yyyy-MM-dd.
           Si el usuario indica una fecha relativa como "mañana",
           "el viernes" o "la próxima semana", usa la fecha de
           referencia proporcionada.
        3. Las horas deben estar en formato HH:mm de 24 horas.
        4. Para "categoriasRecurso" solo puedes usar nombres que
           aparezcan EXACTAMENTE en la lista proporcionada.
        5. No inventes categorías.
        6. Si algún dato realmente no está presente o no puede
           inferirse de la frase, devuelve null para ese dato.
        """)

    @UserMessage("""
        Fecha de referencia: {{hoy}}

        Categorias disponibles:
        {{categorias}}

        Frase del usuario:
        {{frase}}

        Extrae los datos de la reserva.
        """)

    ReservaExtraccion extraer(
            @V("frase") String frase,
            @V("categorias") String categorias,
            @V("hoy") String hoy
    );
}