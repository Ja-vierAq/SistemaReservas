package reservas.presentation.funcionarios;

import reservas.logic.Funcionario;
import reservas.logic.Service;

public class Controller {
    private final View view;
    private final Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
        model.setList(Service.instance().findAllFuncionarios());
    }

    public void save(Funcionario funcionario) throws Exception {
        if (isNew()) {
            Service.instance().create(funcionario);
        } else {
            funcionario.setId(model.getCurrent().getId());
            Service.instance().update(funcionario);
        }
        clear();
        refresh();
    }

    public void search(String id, String nombre) {
        Funcionario filtro = new Funcionario();
        filtro.setId(id);
        filtro.setNombre(nombre);
        model.setList(Service.instance().search(filtro));
    }

    public void delete() throws Exception {
        requireCurrent("Debe seleccionar un funcionario");
        Service.instance().delete(model.getCurrent());
        clear();
        refresh();
    }

    public void clear() {
        model.setCurrent(new Funcionario());
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
        model.setList(Service.instance().findAllFuncionarios());
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
