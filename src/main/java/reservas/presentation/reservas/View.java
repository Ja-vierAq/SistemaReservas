package reservas.presentation.reservas;

import javax.swing.*;

public class View {

    private JPanel panel;
    private JTextField fraseFld;
    private JButton extraerFld;
    private JTextField actividadFld;
    private JTextField fechaFld;
    private JButton seleccionarFechaFld;
    private JComboBox horaInicioFld;
    private JComboBox horaFinFld;
    private JList categoriasFld;
    private JButton reservarFld;
    private JButton cancelarReservaFld;
    private JButton limpiarReservaFld;
    private JTable reservasTbl;
    private JButton imprimirReservasFld;

    public View(){

    }

    private Controller controller;
    private Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public JPanel getPanel(){
        return panel;
    }

}
