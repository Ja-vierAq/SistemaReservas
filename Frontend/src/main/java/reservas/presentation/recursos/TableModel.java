package reservas.presentation.recursos;

import reservas.logic.Recurso;
import reservas.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Recurso> {
    public static final int ID = 0;
    public static final int CATEGORIA = 1;
    public static final int DESCRIPCION = 2;

    public TableModel(int[] columns, List<Recurso> rows) {
        super(columns, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{"Id", "Categoria", "Descripcion"};
    }

    @Override
    protected Object getPropertyAt(Recurso recurso, int column) {
        switch (cols[column]) {
            case ID:
                return recurso.getId();
            case CATEGORIA:
                return recurso.getCategoria() == null
                        ? ""
                        : recurso.getCategoria().getDescripcion();
            case DESCRIPCION:
                return recurso.getDescripcion();
            default:
                return "";
        }
    }
}
