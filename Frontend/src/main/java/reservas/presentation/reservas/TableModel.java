package reservas.presentation.reservas;

import reservas.logic.Reserva;
import reservas.presentation.AbstractTableModel;

import java.util.List;
import java.util.stream.Collectors;

public class TableModel extends AbstractTableModel<Reserva> {
    public static final int ID = 0;
    public static final int ACTIVIDAD = 1;
    public static final int FECHA = 2;
    public static final int INICIO = 3;
    public static final int FIN = 4;
    public static final int CATEGORIAS = 5;
    public static final int ESTADO = 6;

    public TableModel(int[] columns, List<Reserva> rows) {
        super(columns, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{
                "Id",
                "Actividad",
                "Fecha",
                "Inicio",
                "Fin",
                "Categorias",
                "Estado"
        };
    }

    @Override
    protected Object getPropertyAt(Reserva reserva, int column) {
        switch (cols[column]) {
            case ID:
                return reserva.getId();
            case ACTIVIDAD:
                return reserva.getActividad();
            case FECHA:
                return reserva.getFecha();
            case INICIO:
                return reserva.getHoraInicio();
            case FIN:
                return reserva.getHoraFin();
            case CATEGORIAS:
                if (reserva.getCategorias() == null) {
                    return "";
                }
                return reserva.getCategorias()
                        .stream()
                        .map(categoria -> categoria.getDescripcion())
                        .collect(Collectors.joining(", "));
            case ESTADO:
                return reserva.getEstado();
            default:
                return "";
        }
    }
}
