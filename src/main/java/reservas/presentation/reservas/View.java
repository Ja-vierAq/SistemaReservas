package reservas.presentation.reservas;

import reservas.logic.Categoria;
import reservas.logic.Reserva;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class View implements PropertyChangeListener {

    private JPanel panel;
    private JTextField fraseFld;
    private JTextField actividadFld;
    private JTextField fechaFld;
    private JButton extraerFld;
    private JButton seleccionarFechaFld;
    private JButton reservarFld;
    private JButton cancelarReservaFld;
    private JButton limpiarReservaFld;
    private JButton imprimirReservasFld;
    private JComboBox<LocalTime> horaInicioFld;
    private JComboBox<LocalTime> horaFinFld;
    private JList<Categoria> categoriasFld;
    private JTable reservasTbl;

    private Controller controller;
    private Model model;

    public View() {

        // ---------------------------------------------------------
        // RENDER DE CATEGORIAS
        // ---------------------------------------------------------

        categoriasFld.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

                setText(
                        value instanceof Categoria
                                ? ((Categoria) value).getDescripcion()
                                : ""
                );

                return this;
            }
        });

        categoriasFld.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );

        // ---------------------------------------------------------
        // HORAS
        // ---------------------------------------------------------

        DefaultComboBoxModel<LocalTime> horasInicio =
                new DefaultComboBoxModel<>();

        DefaultComboBoxModel<LocalTime> horasFin =
                new DefaultComboBoxModel<>();

        for (int hora = 7; hora <= 20; hora++) {
            horasInicio.addElement(LocalTime.of(hora, 0));
            horasFin.addElement(LocalTime.of(hora, 0));
        }

        horaInicioFld.setModel(horasInicio);
        horaFinFld.setModel(horasFin);

        horaInicioFld.setSelectedItem(LocalTime.of(8, 0));
        horaFinFld.setSelectedItem(LocalTime.of(9, 0));

        // Temporalmente seguimos usando JTextField para fecha
        fechaFld.setText(
                LocalDate.now().plusDays(1).toString()
        );

        // ---------------------------------------------------------
        // LISTENERS
        // ---------------------------------------------------------

        reservarFld.addActionListener(e -> reserve());

        cancelarReservaFld.addActionListener(
                e -> run(() -> controller.cancelSelected())
        );

        limpiarReservaFld.addActionListener(
                e -> controller.clear()
        );

        imprimirReservasFld.addActionListener(
                e -> run(() -> controller.print())
        );

        extraerFld.addActionListener(e -> {
            try {
                controller.extractIA(
                        fraseFld.getText()
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        panel,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        seleccionarFechaFld.addActionListener(
                e -> askDate()
        );

        reservasTbl.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()
                            && reservasTbl.getSelectedRow() >= 0) {

                        int row =
                                reservasTbl.convertRowIndexToModel(
                                        reservasTbl.getSelectedRow()
                                );

                        controller.select(row);
                    }
                });
    }

    // =========================================================
    // TOMAR DATOS DEL FORMULARIO
    // =========================================================

    private Reserva take() throws Exception {

        Reserva reserva = new Reserva();

        reserva.setActividad(
                actividadFld.getText().trim()
        );

        try {
            reserva.setFecha(
                    LocalDate.parse(
                            fechaFld.getText().trim()
                    )
            );
        } catch (Exception e) {
            throw new Exception(
                    "Fecha invalida. Use AAAA-MM-DD"
            );
        }

        reserva.setHoraInicio(
                (LocalTime) horaInicioFld.getSelectedItem()
        );

        reserva.setHoraFin(
                (LocalTime) horaFinFld.getSelectedItem()
        );

        reserva.setCategorias(
                new ArrayList<>(
                        categoriasFld.getSelectedValuesList()
                )
        );

        return reserva;
    }

    // =========================================================
    // VALIDACION
    // =========================================================

    private void validate(Reserva reserva) throws Exception {

        if (reserva.getActividad().isEmpty()) {
            throw new Exception(
                    "Debe ingresar la actividad"
            );
        }

        if (reserva.getFecha() == null) {
            throw new Exception(
                    "Debe ingresar la fecha"
            );
        }

        if (reserva.getHoraInicio() == null
                || reserva.getHoraFin() == null
                || !reserva.getHoraFin()
                .isAfter(reserva.getHoraInicio())) {

            throw new Exception(
                    "La hora fin debe ser posterior a la hora inicio"
            );
        }

        if (reserva.getCategorias().isEmpty()) {
            throw new Exception(
                    "Debe seleccionar al menos una categoria"
            );
        }
    }

    // =========================================================
    // RESERVAR
    // =========================================================

    private void reserve() {

        run(() -> {

            Reserva reserva = take();

            validate(reserva);

            controller.reserve(reserva);
        });
    }

    // =========================================================
    // FECHA
    // =========================================================

    private void askDate() {

        String value = JOptionPane.showInputDialog(
                panel,
                "Fecha (AAAA-MM-DD)",
                fechaFld.getText()
        );

        if (value != null
                && !value.trim().isEmpty()) {

            fechaFld.setText(
                    value.trim()
            );
        }
    }

    // =========================================================
    // ACTUALIZAR CURRENT EN LA VISTA
    // =========================================================

    private void renderCurrent() {

        Reserva reserva = model.getCurrent();

        if (reserva == null) {
            return;
        }

        // Actividad
        actividadFld.setText(
                reserva.getActividad() == null
                        ? ""
                        : reserva.getActividad()
        );

        // Fecha
        if (reserva.getFecha() != null) {
            fechaFld.setText(
                    reserva.getFecha().toString()
            );
        }

        // Hora inicio
        if (reserva.getHoraInicio() != null) {
            horaInicioFld.setSelectedItem(
                    reserva.getHoraInicio()
            );
        }

        // Hora fin
        if (reserva.getHoraFin() != null) {
            horaFinFld.setSelectedItem(
                    reserva.getHoraFin()
            );
        }

        // Categorias
        selectCurrentCategorias();
    }

    // =========================================================
    // CARGAR TODAS LAS CATEGORIAS
    // =========================================================

    private void renderCategorias() {

        categoriasFld.setModel(
                new AbstractListModel<Categoria>() {

                    @Override
                    public int getSize() {
                        return model.getCategorias().size();
                    }

                    @Override
                    public Categoria getElementAt(int index) {
                        return model.getCategorias().get(index);
                    }
                }
        );

        /*
         * Importante:
         * cuando se vuelve a cargar el modelo del JList,
         * Swing pierde la selección anterior.
         *
         * Por eso volvemos a marcar las categorías de current.
         */
        selectCurrentCategorias();
    }

    // =========================================================
    // SELECCIONAR CATEGORIAS DE CURRENT
    // =========================================================

    private void selectCurrentCategorias() {

        categoriasFld.clearSelection();

        Reserva current = model.getCurrent();

        if (current == null
                || current.getCategorias() == null
                || current.getCategorias().isEmpty()) {

            return;
        }

        List<Integer> indices = new ArrayList<>();

        for (int i = 0;
             i < categoriasFld.getModel().getSize();
             i++) {

            Categoria disponible =
                    categoriasFld.getModel()
                            .getElementAt(i);

            for (Categoria seleccionada :
                    current.getCategorias()) {

                if (mismaCategoria(
                        disponible,
                        seleccionada
                )) {

                    indices.add(i);
                    break;
                }
            }
        }

        int[] seleccion = indices.stream()
                .mapToInt(Integer::intValue)
                .toArray();

        categoriasFld.setSelectedIndices(
                seleccion
        );
    }

    private boolean mismaCategoria(
            Categoria a,
            Categoria b
    ) {

        return a != null
                && b != null
                && a.getId() != null
                && a.getId().equals(b.getId());
    }

    // =========================================================
    // PROPERTY CHANGE
    // =========================================================

    @Override
    public void propertyChange(
            PropertyChangeEvent event
    ) {

        if (Model.CATEGORIAS.equals(
                event.getPropertyName()
        )) {

            renderCategorias();
        }

        if (Model.CURRENT.equals(
                event.getPropertyName()
        )) {

            renderCurrent();
        }

        if (Model.LIST.equals(
                event.getPropertyName()
        )) {

            reservasTbl.setModel(
                    new TableModel(
                            new int[]{
                                    TableModel.ID,
                                    TableModel.ACTIVIDAD,
                                    TableModel.FECHA,
                                    TableModel.INICIO,
                                    TableModel.FIN,
                                    TableModel.CATEGORIAS,
                                    TableModel.ESTADO
                            },
                            model.getList()
                    )
            );
        }

        if (Model.SELECTED.equals(
                event.getPropertyName()
        )) {

            cancelarReservaFld.setEnabled(
                    model.getSelected() != null
            );
        }
    }

    // =========================================================
    // MVC
    // =========================================================

    public void setController(
            Controller controller
    ) {
        this.controller = controller;
    }

    public void setModel(
            Model model
    ) {

        if (this.model != null) {
            this.model.removePropertyChangeListener(
                    this
            );
        }

        this.model = model;

        model.addPropertyChangeListener(
                this
        );
    }

    public JPanel getPanel() {
        return panel;
    }

    // =========================================================
    // AUXILIAR
    // =========================================================

    private void run(Action action) {

        try {

            action.run();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    panel,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private interface Action {
        void run() throws Exception;
    }
}