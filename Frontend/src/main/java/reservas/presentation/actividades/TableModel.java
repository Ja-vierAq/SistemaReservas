package reservas.presentation.actividades;

import reservas.logic.Reserva;
import reservas.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Reserva> {
    public static final int FECHA = 0;
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
                "Fecha",
                "Inicio",
                "Fin",
                "Actividad",
                "Funcionario"
        };
    }

    @Override
    protected Object getPropertyAt(Reserva reserva, int column) {
        switch (cols[column]) {
            case FECHA:
                return reserva.getFecha();
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
