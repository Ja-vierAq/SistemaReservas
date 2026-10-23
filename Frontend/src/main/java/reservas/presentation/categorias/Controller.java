package reservas.presentation.categorias;

import reservas.logic.Categoria;
import reservas.logic.Service;
import reservas.util.PdfReport;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllCategorias());
    }

    public void save(Categoria categoria) throws Exception {
        if (isNew()) {
            // El Service autogenera el id de una categoria nueva.
            categoria.setId(null);
            Service.instance().create(categoria);
        } else {
            categoria.setId(model.getCurrent().getId());
            Service.instance().update(categoria);
        }
        clear();
        refresh();
    }

    public void search(String descripcion) {
        Categoria filtro = new Categoria();
        filtro.setDescripcion(descripcion);
        model.setList(Service.instance().search(filtro));
    }

    public void delete() throws Exception {
        requireCurrent("Debe seleccionar una categoria");
        Service.instance().delete(model.getCurrent());
        clear();
        refresh();
    }

    public void clear() {
        model.setCurrent(new Categoria());
    }

    public void edit(int row) {
        if (row >= 0 && row < model.getList().size()) {
            model.setCurrent(model.getList().get(row));
        }
    }

    public void print() throws Exception {
        PdfReport.exportTable(view.getPanel(), "Reporte de Categorías",
                new TableModel(new int[]{TableModel.ID, TableModel.DESCRIPCION}, model.getList()));
    }

    private void refresh() {
        model.setList(Service.instance().findAllCategorias());
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
