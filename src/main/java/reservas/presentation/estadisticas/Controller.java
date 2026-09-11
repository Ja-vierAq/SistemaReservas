package reservas.presentation.estadisticas;

import reservas.logic.Service;

import java.time.LocalDate;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void loadRecursos(LocalDate desde, LocalDate hasta) throws Exception {
        model.setRecursos(Service.instance().estadisticasRecursos(desde, hasta));
    }

    public void loadActividades(LocalDate desde, LocalDate hasta) throws Exception {
        model.setActividades(Service.instance().estadisticasActividades(desde, hasta));
    }

    public void printRecursos() throws Exception {
        // generacion de PDF no implementada aun
    }

    public void printActividades() throws Exception {
        // generacion de PDF no implementada aun
    }
}
