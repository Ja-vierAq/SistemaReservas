package reservas.presentation.recursos;

import reservas.logic.Categoria;
import reservas.logic.Recurso;
import reservas.logic.Service;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setCategorias(Service.instance().findAllCategorias());
        model.setList(Service.instance().findAllRecursos());
    }

    public void save(Recurso recurso) throws Exception {
        if (isNew()) {
            Service.instance().create(recurso);
        } else {
            recurso.setId(model.getCurrent().getId());
            Service.instance().update(recurso);
        }
        clear();
        refresh();
    }

    public void search(Categoria categoria, String descripcion) {
        Recurso filtro = new Recurso();
        filtro.setCategoria(categoria);
        filtro.setDescripcion(descripcion);
        model.setList(Service.instance().search(filtro));
    }

    public void delete() throws Exception {
        requireCurrent("Debe seleccionar un recurso");
        Service.instance().delete(model.getCurrent());
        clear();
        refresh();
    }

    public void clear() {
        model.setCurrent(new Recurso());
    }

    public void edit(int row) {
        if (row >= 0 && row < model.getList().size()) {
            model.setCurrent(model.getList().get(row));
        }
    }

    public void print() throws Exception {
        // generacion de PDF no implementada aun
    }

    private void refresh() {
        model.setCategorias(Service.instance().findAllCategorias());
        model.setList(Service.instance().findAllRecursos());
    }

    private boolean isNew() {
        return model.getCurrent() == null
                || model.getCurrent().getId() == null
                || model.getCurrent().getId().trim().isEmpty();
    }

    private void requireCurrent(String message) throws Exception {
        if (isNew()) {
            throw new Exception(message);
        }
    }
}
