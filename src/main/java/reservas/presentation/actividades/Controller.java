package reservas.presentation.actividades;

import reservas.logic.Reserva;
import reservas.logic.Service;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void load(LocalDate fechaReferencia) throws Exception {
        LocalDate inicioSemana = Service.instance().inicioSemana(fechaReferencia);
        List<Reserva> actividades = Service.instance().actividadesSemana(fechaReferencia);
        model.setSemana(fechaReferencia, inicioSemana, actividades);
    }

    public void print() throws Exception {
        // generacion de PDF no implementada aun
    }
}
