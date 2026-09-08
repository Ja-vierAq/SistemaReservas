package reservas.presention;

import reservas.Application;
import reservas.logic.Categoria;
import reservas.logic.Funcionario;
import reservas.logic.Recurso;
import reservas.logic.Reserva;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * View UNICA para todo el "Sistema de Reserva de Recursos" (EIF206),
 * siguiendo el mismo patron que personas.presentation.personas.View del
 * proyecto de ejemplo (constructor solo con listeners, Highlighter,
 * validate()/take() por formulario, propertyChange() reaccionando a las
 * propiedades del Model). Aqui, en vez de una pantalla, son las 9 pestañas
 * que pide el enunciado (Login y Cambiar Clave por separado, cuentan como
 * la funcionalidad 1):
 *
 *   Tab 1 - Login                    (funcionalidad 1)
 *   Tab 2 - Cambiar Clave            (funcionalidad 1)
 *   Tab 3 - Reservas                 (funcionalidad 2, solo Funcionario)
 *   Tab 4 - Funcionarios             (funcionalidad 3, solo Administrador)
 *   Tab 5 - Categorias               (funcionalidad 4, solo Administrador)
 *   Tab 6 - Recursos                 (funcionalidad 5, solo Administrador)
 *   Tab 7 - Calendarizacion          (funcionalidad 6, ambos roles)
 *   Tab 8 - Actividades              (funcionalidad 7, ambos roles)
 *   Tab 9 - Estadisticas             (funcionalidad 8, ambos roles)
 *
 * IMPORTANTE (igual que con la version anterior de solo Reservas): esta
 * clase depende de un Controller y un Model que aun no existen, y de un
 * TableModel por cada tabla (ReservaTableModel, FuncionarioTableModel,
 * CategoriaTableModel, RecursoTableModel, CalendarizacionTableModel,
 * ActividadesTableModel, EstadisticaRecursoTableModel,
 * EstadisticaActividadTableModel). Todo eso esta documentado en
 * VIEW_README.md - aqui SOLO va la Vista, tal como pediste.
 *
 * Mostrar/ocultar pestañas segun el rol del Usuario logueado (Reservas es
 * solo Funcionario; Funcionarios/Categorias/Recursos son solo Admin) es
 * responsabilidad del Controller, usando getTabbedPane() o simplemente
 * reaccionando a Model.USUARIO_ACTUAL (ver propertyChange mas abajo).
 */
public class View implements PropertyChangeListener {

    private JPanel panel;
    private JTabbedPane tabbedPane;

    // ===================== Tab 1: Login =====================
    private JTextField loginIdFld;
    private JPasswordField loginClaveFld;
    private JButton loginIngresarFld;
    private JButton loginCancelarFld;

    // ===================== Tab 2: Cambiar Clave =====================
    private JPasswordField ccClaveActualFld;
    private JPasswordField ccClaveNuevaFld;
    private JPasswordField ccClaveNuevaConfirmaFld;
    private JButton ccAceptarFld;
    private JButton ccCancelarFld;

    // ===================== Tab 3: Reservas =====================
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

    // ===================== Tab 4: Funcionarios =====================
    private JTextField funBuscarIdFld;
    private JTextField funBuscarNombreFld;
    private JButton funBuscarFld;
    private JButton funImprimirFld;
    private JTextField funIdFld;
    private JTextField funNombreFld;
    private JTextField funTelefonoFld;
    private JButton funGuardarFld;
    private JButton funBorrarFld;
    private JButton funLimpiarFld;
    private JTable funcionariosTbl;

    // ===================== Tab 5: Categorias =====================
    private JTextField catBuscarDescripcionFld;
    private JButton catBuscarFld;
    private JButton catImprimirFld;
    private JTextField catIdFld;
    private JTextField catDescripcionFld;
    private JButton catGuardarFld;
    private JButton catBorrarFld;
    private JButton catLimpiarFld;
    private JTable categoriasTbl;

    // ===================== Tab 6: Recursos =====================
    private JComboBox recFiltroCategoriaFld;
    private JTextField recFiltroDescripcionFld;
    private JButton recBuscarFld;
    private JButton recImprimirFld;
    private JTextField recIdFld;
    private JComboBox recCategoriaFld;
    private JTextField recDescripcionFld;
    private JButton recGuardarFld;
    private JButton recBorrarFld;
    private JButton recLimpiarFld;
    private JTable recursosTbl;

    // ===================== Tab 7: Calendarizacion =====================
    private JTextField calFechaFld;
    private JButton calSeleccionarFechaFld;
    private JComboBox calCategoriaFld;
    private JButton calCargarFld;
    private JButton calImprimirFld;
    private JTable calendarizacionTbl;

    // ===================== Tab 8: Actividades =====================
    private JTextField actFechaReferenciaFld;
    private JButton actSeleccionarFechaFld;
    private JButton actCargarFld;
    private JButton actImprimirFld;
    private JTable actividadesTbl;

    // ===================== Tab 9: Estadisticas =====================
    private JTextField estRecDesdeFld;
    private JTextField estRecHastaFld;
    private JButton estRecCargarFld;
    private JTable estadisticasRecursosTbl;
    private JPanel graficoRecursosPnl;
    private JTextField estActDesdeFld;
    private JTextField estActHastaFld;
    private JButton estActCargarFld;
    private JTable estadisticasActividadesTbl;
    private JPanel graficoActividadesPnl;

    public View() {

        // ---------- Login ----------
        loginIngresarFld.addActionListener(e -> {
            if (validateLogin()) {
                try {
                    controller.login(loginIdFld.getText(), new String(loginClaveFld.getPassword()));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginCancelarFld.addActionListener(e -> {
            loginIdFld.setText("");
            loginClaveFld.setText("");
        });

        // ---------- Cambiar Clave ----------
        ccAceptarFld.addActionListener(e -> {
            if (validateCambioClave()) {
                try {
                    controller.cambiarClave(new String(ccClaveActualFld.getPassword()),
                        new String(ccClaveNuevaFld.getPassword()));
                    JOptionPane.showMessageDialog(panel, "CLAVE ACTUALIZADA", "", JOptionPane.INFORMATION_MESSAGE);
                    limpiarCambioClave();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        ccCancelarFld.addActionListener(e -> limpiarCambioClave());

        // ---------- Reservas ----------
        reservarFld.addActionListener(e -> {
            if (validateReserva()) {
                try {
                    controller.reservar(takeReserva());
                    JOptionPane.showMessageDialog(panel, "RESERVA APLICADA", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelarReservaFld.addActionListener(e -> {
            int row = reservasTbl.getSelectedRow();
            if (row >= 0) {
                try {
                    controller.cancelarReserva(row);
                    JOptionPane.showMessageDialog(panel, "RESERVA CANCELADA", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione una reserva de la tabla", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        limpiarReservaFld.addActionListener(e -> controller.limpiarReserva());
        extraerFld.addActionListener(e -> {
            try {
                controller.extraerIA(fraseFld.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        imprimirReservasFld.addActionListener(e -> {
            try {
                controller.imprimirReservas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        reservasTbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = reservasTbl.getSelectedRow();
                if (row >= 0) controller.editarReserva(row);
            }
        });

        // ---------- Funcionarios ----------
        funBuscarFld.addActionListener(e ->
            controller.buscarFuncionarios(funBuscarIdFld.getText(), funBuscarNombreFld.getText()));
        funGuardarFld.addActionListener(e -> {
            if (validateFuncionario()) {
                try {
                    controller.guardarFuncionario(takeFuncionario());
                    JOptionPane.showMessageDialog(panel, "FUNCIONARIO GUARDADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        funBorrarFld.addActionListener(e -> {
            if (!funIdFld.getText().isEmpty()) {
                try {
                    controller.borrarFuncionario(funIdFld.getText());
                    JOptionPane.showMessageDialog(panel, "FUNCIONARIO BORRADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        funLimpiarFld.addActionListener(e -> controller.limpiarFuncionario());
        funImprimirFld.addActionListener(e -> {
            try {
                controller.imprimirFuncionarios();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        funcionariosTbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = funcionariosTbl.getSelectedRow();
                if (row >= 0) controller.editarFuncionario(row);
            }
        });

        // ---------- Categorias ----------
        catBuscarFld.addActionListener(e -> controller.buscarCategorias(catBuscarDescripcionFld.getText()));
        catGuardarFld.addActionListener(e -> {
            if (validateCategoria()) {
                try {
                    controller.guardarCategoria(takeCategoria());
                    JOptionPane.showMessageDialog(panel, "CATEGORIA GUARDADA", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        catBorrarFld.addActionListener(e -> {
            if (!catIdFld.getText().isEmpty()) {
                try {
                    controller.borrarCategoria(catIdFld.getText());
                    JOptionPane.showMessageDialog(panel, "CATEGORIA BORRADA", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        catLimpiarFld.addActionListener(e -> controller.limpiarCategoria());
        catImprimirFld.addActionListener(e -> {
            try {
                controller.imprimirCategorias();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        categoriasTbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = categoriasTbl.getSelectedRow();
                if (row >= 0) controller.editarCategoria(row);
            }
        });

        // ---------- Recursos ----------
        recBuscarFld.addActionListener(e -> controller.buscarRecursos(
            (Categoria) recFiltroCategoriaFld.getSelectedItem(), recFiltroDescripcionFld.getText()));
        recGuardarFld.addActionListener(e -> {
            if (validateRecurso()) {
                try {
                    controller.guardarRecurso(takeRecurso());
                    JOptionPane.showMessageDialog(panel, "RECURSO GUARDADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        recBorrarFld.addActionListener(e -> {
            if (!recIdFld.getText().isEmpty()) {
                try {
                    controller.borrarRecurso(recIdFld.getText());
                    JOptionPane.showMessageDialog(panel, "RECURSO BORRADO", "", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        recLimpiarFld.addActionListener(e -> controller.limpiarRecurso());
        recImprimirFld.addActionListener(e -> {
            try {
                controller.imprimirRecursos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        recursosTbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = recursosTbl.getSelectedRow();
                if (row >= 0) controller.editarRecurso(row);
            }
        });

        // ---------- Calendarizacion ----------
        calCargarFld.addActionListener(e -> {
            LocalDate fecha = calFechaFld.getText().isEmpty() ? null : LocalDate.parse(calFechaFld.getText());
            controller.cargarCalendarizacion(fecha, (Categoria) calCategoriaFld.getSelectedItem());
        });
        calImprimirFld.addActionListener(e -> {
            try {
                controller.imprimirCalendarizacion();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ---------- Actividades ----------
        actCargarFld.addActionListener(e -> {
            LocalDate fecha = actFechaReferenciaFld.getText().isEmpty() ? null : LocalDate.parse(actFechaReferenciaFld.getText());
            controller.cargarActividades(fecha);
        });
        actImprimirFld.addActionListener(e -> {
            try {
                controller.imprimirActividades();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ---------- Estadisticas ----------
        estRecCargarFld.addActionListener(e -> {
            LocalDate desde = estRecDesdeFld.getText().isEmpty() ? null : LocalDate.parse(estRecDesdeFld.getText());
            LocalDate hasta = estRecHastaFld.getText().isEmpty() ? null : LocalDate.parse(estRecHastaFld.getText());
            controller.cargarEstadisticasRecursos(desde, hasta);
        });
        estActCargarFld.addActionListener(e -> {
            LocalDate desde = estActDesdeFld.getText().isEmpty() ? null : LocalDate.parse(estActDesdeFld.getText());
            LocalDate hasta = estActHastaFld.getText().isEmpty() ? null : LocalDate.parse(estActHastaFld.getText());
            controller.cargarEstadisticasActividades(desde, hasta);
        });

        // ---------- Renderers de Categoria en los combos ----------
        ListCellRenderer categoriaRenderer = new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String texto = (value instanceof Categoria) ? ((Categoria) value).getDescripcion() : (value == null ? "" : String.valueOf(value));
                return super.getListCellRendererComponent(list, texto, index, isSelected, cellHasFocus);
            }
        };
        recFiltroCategoriaFld.setRenderer(categoriaRenderer);
        recCategoriaFld.setRenderer(categoriaRenderer);
        calCategoriaFld.setRenderer(categoriaRenderer);
        categoriasFld.setCellRenderer(categoriaRenderer);
        categoriasFld.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // ---------- Highlighter en los campos requeridos ----------
        reservas.presentation.Highlighter highlighter = new reservas.presentation.Highlighter(Color.green);
        actividadFld.addMouseListener(highlighter);
        fechaFld.addMouseListener(highlighter);
        funNombreFld.addMouseListener(highlighter);
        funTelefonoFld.addMouseListener(highlighter);
        catDescripcionFld.addMouseListener(highlighter);
        recDescripcionFld.addMouseListener(highlighter);
        loginIdFld.addMouseListener(highlighter);
        loginClaveFld.addMouseListener(highlighter);
    }

    public JPanel getPanel() {
        return panel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    Controller controller;
    Model model;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void setModel(Model model) {
        this.model = model;
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {

            // ----- Reservas -----
            case Model.CATEGORIAS_DISPONIBLES:
                categoriasFld.setListData(model.getCategoriasDisponibles().toArray(new Categoria[0]));
                recFiltroCategoriaFld.setModel(new DefaultComboBoxModel(model.getCategoriasDisponibles().toArray(new Categoria[0])));
                recCategoriaFld.setModel(new DefaultComboBoxModel(model.getCategoriasDisponibles().toArray(new Categoria[0])));
                calCategoriaFld.setModel(new DefaultComboBoxModel(model.getCategoriasDisponibles().toArray(new Categoria[0])));
                break;
            case Model.RESERVAS_LIST:
                int[] resCols = {ReservaTableModel.ID, ReservaTableModel.ACTIVIDAD, ReservaTableModel.FECHA, ReservaTableModel.HORA_INICIO, ReservaTableModel.HORA_FIN, ReservaTableModel.ESTADO};
                reservasTbl.setModel(new ReservaTableModel(resCols, model.getReservas()));
                break;
            case Model.RESERVA_CURRENT:
                Reserva r = model.getReservaCurrent();
                actividadFld.setText(r.getActividad() == null ? "" : r.getActividad());
                fechaFld.setText(r.getFecha() == null ? "" : r.getFecha().toString());
                horaInicioFld.setSelectedItem(r.getHoraInicio() == null ? null : r.getHoraInicio().toString());
                horaFinFld.setSelectedItem(r.getHoraFin() == null ? null : r.getHoraFin().toString());
                categoriasFld.clearSelection();
                if (r.getCategorias() != null) {
                    for (Categoria c : r.getCategorias()) {
                        int idx = indexOfEnLista(categoriasFld, c);
                        if (idx >= 0) categoriasFld.addSelectionInterval(idx, idx);
                    }
                }
                fraseFld.setText("");
                limpiarFondo(actividadFld, fechaFld, categoriasFld);
                break;

            // ----- Funcionarios -----
            case Model.FUNCIONARIOS_LIST:
                int[] funCols = {FuncionarioTableModel.ID, FuncionarioTableModel.NOMBRE, FuncionarioTableModel.TELEFONO};
                funcionariosTbl.setModel(new FuncionarioTableModel(funCols, model.getFuncionarios()));
                break;
            case Model.FUNCIONARIO_CURRENT:
                Funcionario f = model.getFuncionarioCurrent();
                funIdFld.setText(f.getId() == null ? "" : f.getId());
                funNombreFld.setText(f.getNombre() == null ? "" : f.getNombre());
                funTelefonoFld.setText(f.getTelefono() == null ? "" : f.getTelefono());
                limpiarFondo(funNombreFld, funTelefonoFld);
                break;

            // ----- Categorias -----
            case Model.CATEGORIAS_LIST:
                int[] catCols = {CategoriaTableModel.ID, CategoriaTableModel.DESCRIPCION};
                categoriasTbl.setModel(new CategoriaTableModel(catCols, model.getCategorias()));
                break;
            case Model.CATEGORIA_CURRENT:
                Categoria c = model.getCategoriaCurrent();
                catIdFld.setText(c.getId() == null ? "" : c.getId());
                catDescripcionFld.setText(c.getDescripcion() == null ? "" : c.getDescripcion());
                limpiarFondo(catDescripcionFld);
                break;

            // ----- Recursos -----
            case Model.RECURSOS_LIST:
                int[] recCols = {RecursoTableModel.ID, RecursoTableModel.CATEGORIA, RecursoTableModel.DESCRIPCION};
                recursosTbl.setModel(new RecursoTableModel(recCols, model.getRecursos()));
                break;
            case Model.RECURSO_CURRENT:
                Recurso rec = model.getRecursoCurrent();
                recIdFld.setText(rec.getId() == null ? "" : rec.getId());
                recCategoriaFld.setSelectedItem(rec.getCategoria());
                recDescripcionFld.setText(rec.getDescripcion() == null ? "" : rec.getDescripcion());
                limpiarFondo(recDescripcionFld);
                break;

            // ----- Calendarizacion -----
            case Model.CALENDARIZACION:
                calendarizacionTbl.setModel(new CalendarizacionTableModel(model.getCalendarizacion()));
                break;

            // ----- Actividades -----
            case Model.ACTIVIDADES_SEMANALES:
                actividadesTbl.setModel(new ActividadesTableModel(model.getActividadesSemanales()));
                break;

            // ----- Estadisticas -----
            case Model.ESTADISTICAS_RECURSOS:
                int[] estRecCols = {EstadisticaRecursoTableModel.CATEGORIA, EstadisticaRecursoTableModel.CANTIDAD};
                estadisticasRecursosTbl.setModel(new EstadisticaRecursoTableModel(estRecCols, model.getEstadisticasRecursos()));
                break;
            case Model.ESTADISTICAS_ACTIVIDADES:
                int[] estActCols = {EstadisticaActividadTableModel.SEMANA, EstadisticaActividadTableModel.CANTIDAD};
                estadisticasActividadesTbl.setModel(new EstadisticaActividadTableModel(estActCols, model.getEstadisticasActividades()));
                break;

            // ----- Login -----
            case Model.USUARIO_ACTUAL:
                loginIdFld.setText("");
                loginClaveFld.setText("");
                boolean esAdmin = model.getUsuarioActual() != null
                    && model.getUsuarioActual().getRol() != null
                    && model.getUsuarioActual().getRol().toUpperCase().startsWith("ADMIN");
                tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Reservas"), !esAdmin);
                tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Funcionarios"), esAdmin);
                tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Categorias"), esAdmin);
                tabbedPane.setEnabledAt(tabbedPane.indexOfTab("Recursos"), esAdmin);
                break;
        }
        this.panel.revalidate();
    }

    private int indexOfEnLista(JList lista, Categoria c) {
        ListModel lm = lista.getModel();
        for (int i = 0; i < lm.getSize(); i++) {
            Object item = lm.getElementAt(i);
            if (item instanceof Categoria && ((Categoria) item).getId().equals(c.getId())) return i;
        }
        return -1;
    }

    private void limpiarFondo(JComponent... campos) {
        for (JComponent campo : campos) {
            campo.setBackground(null);
            campo.setToolTipText(null);
        }
    }

    private void marcarError(JComponent campo, String mensaje) {
        campo.setBackground(Application.BACKGROUND_ERROR);
        campo.setToolTipText(mensaje);
    }

    private void limpiarCambioClave() {
        ccClaveActualFld.setText("");
        ccClaveNuevaFld.setText("");
        ccClaveNuevaConfirmaFld.setText("");
    }

    // ===================== take() por formulario =====================

    public Reserva takeReserva() {
        Reserva r = new Reserva();
        r.setActividad(actividadFld.getText());
        r.setFecha(fechaFld.getText().isEmpty() ? null : LocalDate.parse(fechaFld.getText()));
        r.setHoraInicio(horaInicioFld.getSelectedItem() == null ? null : LocalTime.parse((String) horaInicioFld.getSelectedItem()));
        r.setHoraFin(horaFinFld.getSelectedItem() == null ? null : LocalTime.parse((String) horaFinFld.getSelectedItem()));
        List<Categoria> seleccionadas = new ArrayList<>();
        for (Object o : categoriasFld.getSelectedValuesList()) seleccionadas.add((Categoria) o);
        r.setCategorias(seleccionadas);
        return r;
    }

    public Funcionario takeFuncionario() {
        Funcionario f = new Funcionario();
        f.setId(funIdFld.getText());
        f.setNombre(funNombreFld.getText());
        f.setTelefono(funTelefonoFld.getText());
        return f;
    }

    public Categoria takeCategoria() {
        Categoria c = new Categoria();
        c.setId(catIdFld.getText());
        c.setDescripcion(catDescripcionFld.getText());
        return c;
    }

    public Recurso takeRecurso() {
        Recurso r = new Recurso();
        r.setId(recIdFld.getText());
        r.setCategoria((Categoria) recCategoriaFld.getSelectedItem());
        r.setDescripcion(recDescripcionFld.getText());
        return r;
    }

    // ===================== validate() por formulario =====================

    private boolean validateLogin() {
        boolean valid = true;
        if (loginIdFld.getText().isEmpty()) {
            valid = false;
            marcarError(loginIdFld, "ID requerido");
        } else limpiarFondo(loginIdFld);

        if (loginClaveFld.getPassword().length == 0) {
            valid = false;
            marcarError(loginClaveFld, "Clave requerida");
        } else limpiarFondo(loginClaveFld);
        return valid;
    }

    private boolean validateCambioClave() {
        boolean valid = true;
        if (ccClaveActualFld.getPassword().length == 0) {
            valid = false;
            marcarError(ccClaveActualFld, "Clave actual requerida");
        } else limpiarFondo(ccClaveActualFld);

        if (ccClaveNuevaFld.getPassword().length == 0) {
            valid = false;
            marcarError(ccClaveNuevaFld, "Clave nueva requerida");
        } else if (!new String(ccClaveNuevaFld.getPassword()).equals(new String(ccClaveNuevaConfirmaFld.getPassword()))) {
            valid = false;
            marcarError(ccClaveNuevaConfirmaFld, "Las claves nuevas no coinciden");
        } else {
            limpiarFondo(ccClaveNuevaFld, ccClaveNuevaConfirmaFld);
        }
        return valid;
    }

    private boolean validateReserva() {
        boolean valid = true;
        if (actividadFld.getText().isEmpty()) {
            valid = false;
            marcarError(actividadFld, "Actividad requerida");
        } else limpiarFondo(actividadFld);

        if (fechaFld.getText().isEmpty()) {
            valid = false;
            marcarError(fechaFld, "Fecha requerida");
        } else limpiarFondo(fechaFld);

        if (horaInicioFld.getSelectedItem() == null || horaFinFld.getSelectedItem() == null) {
            valid = false;
            horaInicioFld.setToolTipText("Hora inicio y hora fin requeridas");
        } else {
            horaInicioFld.setToolTipText(null);
        }

        if (categoriasFld.isSelectionEmpty()) {
            valid = false;
            marcarError(categoriasFld, "Seleccione al menos una categoria");
        } else limpiarFondo(categoriasFld);
        return valid;
    }

    private boolean validateFuncionario() {
        boolean valid = true;
        if (funNombreFld.getText().isEmpty()) {
            valid = false;
            marcarError(funNombreFld, "Nombre requerido");
        } else limpiarFondo(funNombreFld);

        if (funTelefonoFld.getText().isEmpty()) {
            valid = false;
            marcarError(funTelefonoFld, "Telefono requerido");
        } else limpiarFondo(funTelefonoFld);
        return valid;
    }

    private boolean validateCategoria() {
        boolean valid = true;
        if (catDescripcionFld.getText().isEmpty()) {
            valid = false;
            marcarError(catDescripcionFld, "Descripcion requerida");
        } else limpiarFondo(catDescripcionFld);
        return valid;
    }

    private boolean validateRecurso() {
        boolean valid = true;
        if (recIdFld.getText().isEmpty()) {
            valid = false;
            marcarError(recIdFld, "Id o numero de activo requerido");
        } else limpiarFondo(recIdFld);

        if (recCategoriaFld.getSelectedItem() == null) {
            valid = false;
            recCategoriaFld.setToolTipText("Seleccione una categoria");
        } else recCategoriaFld.setToolTipText(null);

        if (recDescripcionFld.getText().isEmpty()) {
            valid = false;
            marcarError(recDescripcionFld, "Descripcion requerida");
        } else limpiarFondo(recDescripcionFld);
        return valid;
    }
}
