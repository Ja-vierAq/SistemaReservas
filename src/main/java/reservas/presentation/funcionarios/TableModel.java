package reservas.presentation.funcionarios;

import reservas.logic.Funcionario;
import reservas.presentation.AbstractTableModel;

import java.util.List;

public class TableModel extends AbstractTableModel<Funcionario> {
    public static final int ID = 0;
    public static final int NOMBRE = 1;
    public static final int TELEFONO = 2;

    public TableModel(int[] columns, List<Funcionario> rows) {
        super(columns, rows);
    }

    @Override
    protected void initColNames() {
        colNames = new String[]{"Id", "Nombre", "Telefono"};
    }

    @Override
    protected Object getPropertyAt(Funcionario funcionario, int column) {
        switch (cols[column]) {
            case ID:
                return funcionario.getId();
            case NOMBRE:
                return funcionario.getNombre();
            case TELEFONO:
                return funcionario.getTelefono();
            default:
                return "";
        }
    }
}
