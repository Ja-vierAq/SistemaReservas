package reservas.presentation.calendarizacion;

import reservas.logic.Reserva;
import reservas.presentation.AbstractTableModel;

import java.util.List;
import java.util.stream.Collectors;

public class TableModel extends AbstractTableModel<Reserva> {
    public static final int RECURSOS = 0;
    public static final int INICIO = 1;
    public static final int FIN = 2;
    public static final int ACTIVIDAD = 3;
    public static final int FUNCIONARIO = 4;

    public TableModel(int[] columns, List<Reserva> rows) {
        super(columns, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{
                "Recursos",
                "Inicio",
                "Fin",
                "Actividad",
                "Funcionario"
        };
    }

    @Override
    protected Object getPropertyAt(Reserva reserva, int column) {
        switch (cols[column]) {
            case RECURSOS:
                if (reserva.getRecursos() == null) {
                    return "";
                }
                return reserva.getRecursos()
                        .stream()
                        .map(recurso -> recurso.getDescripcion())
                        .collect(Collectors.joining(", "));
            case INICIO:
                return reserva.getHoraInicio();
            case FIN:
                return reserva.getHoraFin();
            case ACTIVIDAD:
                return reserva.getActividad();
            case FUNCIONARIO:
                return reserva.getFuncionario() == null
                        ? ""
                        : reserva.getFuncionario().getNombre();
            default:
                return "";
        }
    }
}
