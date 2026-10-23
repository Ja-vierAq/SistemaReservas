package reservas.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;

public final class PdfReport {

    private PdfReport() {
    }

    public static void exportTable(
            Component parent,
            String title,
            TableModel tableModel
    ) throws Exception {
        if (tableModel == null || tableModel.getRowCount() == 0) {
            throw new Exception("No hay datos para imprimir");
        }

        File file = chooseFile(parent, title);
        if (file == null) {
            return;
        }

        createPdf(file, title, tableModel);
        openPdf(file);
    }

    private static File chooseFile(Component parent, String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte PDF");
        chooser.setFileFilter(
                new FileNameExtensionFilter("Archivo PDF (*.pdf)", "pdf")
        );
        chooser.setSelectedFile(new File(safeFileName(title) + ".pdf"));

        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new File(file.getParentFile(), file.getName() + ".pdf");
        }

        if (file.exists()) {
            int answer = JOptionPane.showConfirmDialog(
                    parent,
                    "El archivo ya existe. ¿Desea reemplazarlo?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (answer != JOptionPane.YES_OPTION) {
                return null;
            }
        }

        return file;
    }

    private static void createPdf(
            File file,
            String title,
            TableModel tableModel
    ) throws Exception {
        PdfWriter writer = new PdfWriter(file.getAbsolutePath());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            document.setMargins(20, 20, 20, 20);

            Paragraph heading = new Paragraph(title)
                    .setBold()
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(heading);

            int columnCount = tableModel.getColumnCount();
            Table table = new Table(UnitValue.createPercentArray(columnCount))
                    .useAllAvailableWidth();

            for (int column = 0; column < columnCount; column++) {
                table.addHeaderCell(
                        getCell(tableModel.getColumnName(column), true)
                );
            }

            for (int row = 0; row < tableModel.getRowCount(); row++) {
                for (int column = 0; column < columnCount; column++) {
                    Object value = tableModel.getValueAt(row, column);
                    table.addCell(
                            getCell(value == null ? "" : value.toString(), false)
                    );
                }
            }

            document.add(table);
        } finally {
            document.close();
        }
    }

    private static Cell getCell(String text, boolean header) {
        Paragraph paragraph = new Paragraph(text);
        Cell cell = new Cell()
                .add(paragraph)
                .setPadding(5);

        if (header) {
            paragraph.setBold();
            cell.setTextAlignment(TextAlignment.CENTER);
        }

        return cell;
    }

    private static void openPdf(File pdfFile) throws Exception {
        if (!pdfFile.exists()) {
            throw new Exception("El archivo PDF no existe");
        }

        if (!Desktop.isDesktopSupported()) {
            throw new Exception(
                    "El PDF fue generado, pero el sistema no permite abrirlo automaticamente: "
                            + pdfFile.getAbsolutePath()
            );
        }

        Desktop.getDesktop().open(pdfFile);
    }

    private static String safeFileName(String value) {
        String name = value == null ? "reporte" : value.trim().toLowerCase();
        name = name.replaceAll("[^a-z0-9áéíóúñü]+", "_");
        name = name.replaceAll("^_+|_+$", "");

        return name.isEmpty() ? "reporte" : name;
    }
}
