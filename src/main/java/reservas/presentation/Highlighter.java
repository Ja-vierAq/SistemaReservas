package reservas.presentation;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Resalta de color un componente cuando el mouse pasa encima y restaura
 * su color original al salir. Identica a personas.presentation.Highlighter
 * del proyecto de ejemplo; se deja en reservas.presentation porque es
 * infraestructura compartida por todas las vistas (no logica de un modulo
 * en particular).
 */
public class Highlighter extends MouseAdapter implements MouseListener {
    private Color color;
    private Color original;

    public Highlighter(Color color) {
        this.color = color;
    }

    public void mouseEntered(MouseEvent evt) {
        Component source = (Component) evt.getSource();
        original = source.getBackground();
        source.setBackground(color);
    }

    public void mouseExited(MouseEvent evt) {
        Component source = (Component) evt.getSource();
        source.setBackground(original);
    }
}
