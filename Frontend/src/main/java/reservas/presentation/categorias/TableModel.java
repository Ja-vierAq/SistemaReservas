package reservas.presentation.categorias;

import reservas.logic.Categoria;
import reservas.presentation.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel<Categoria> {
    public static final int ID=0, DESCRIPCION=1;
    public TableModel(int[] cols, List<Categoria> rows){ super(cols, rows); }
    protected void initColNames(){ colNames=new String[]{"Id","Descripcion"}; }
    protected Object getPropertyAt(Categoria c,int col){
        switch(cols[col]){case ID:return c.getId(); case DESCRIPCION:return c.getDescripcion(); default:return "";}
    }
}