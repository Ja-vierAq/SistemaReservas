package reservas.presentation.calendarizacion;

import reservas.logic.Categoria;
import reservas.logic.Recurso;
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
        model.setCategorias(Service.instance().findAllCategorias());
    }

    public void load(LocalDate fecha, Categoria categoria) throws Exception {
        if (categoria == null) {
            throw new Exception("Debe seleccionar una categoria");
        }

        Recurso filtro = new Recurso();
        filtro.setCategoria(categoria);

        List<Recurso> recursos = Service.instance().search(filtro);
        List<Reserva> reservas = Service.instance().calendarizacionRecursos(fecha, categoria);

        model.setCalendarizacion(fecha, categoria, recursos, reservas);
    }

    public void print() throws Exception {
        // generacion de PDF no implementada aun
    }
}
