package reservas.presentation.reservas;

import reservas.logic.Funcionario;
import reservas.logic.Reserva;
import reservas.logic.Service;
import reservas.presentation.Sesion;

public class Controller {
    private final View view;
    private final Model model;
    private final Funcionario funcionarioActual;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);

        funcionarioActual = resolveFuncionarioActual();
        model.setCategorias(Service.instance().findAllCategorias());
        refresh();
    }

    public void reserve(Reserva reserva) throws Exception {
        reserva.setFuncionario(funcionarioActual);
        Service.instance().create(reserva);
        clear();
        refresh();
    }

    public void cancelSelected() throws Exception {
        if (model.getSelected() == null) {
            throw new Exception("Debe seleccionar una reserva");
        }
        Service.instance().cancel(model.getSelected());
        model.setSelected(null);
        refresh();
    }

    public void select(int row) {
        if (row >= 0 && row < model.getList().size()) {
            model.setSelected(model.getList().get(row));
        }
    }

    public void clear() {
        model.setCurrent(new Reserva());
        model.setSelected(null);
    }

    public void extractIA(String frase) throws Exception {
        // funcion de ia no implementada aun
    }

    public void print() throws Exception {
        // generacion de PDF no implementada aun
    }

    private void refresh() {
        try {
            model.setList(Service.instance().findReservas(funcionarioActual));
        } catch (Exception e) {
            throw new IllegalStateException("No se pudieron cargar las reservas del funcionario", e);
        }
    }

    private Funcionario resolveFuncionarioActual() {
        if (Sesion.getUsuario() == null) {
            throw new IllegalStateException("No hay un usuario autenticado");
        }

        String usuarioId = Sesion.getUsuario().getId();

        return Service.instance().findAllFuncionarios().stream()
                .filter(f -> f.getUsuario() != null)
                .filter(f -> usuarioId.equals(f.getUsuario().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "El usuario autenticado no tiene un funcionario asociado"));
    }
}
