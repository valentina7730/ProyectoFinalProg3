package store.electronic.model;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import store.electronic.model.Usuario;
import store.electronic.model.Producto;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PDFGeneratorService {
    public void generarFactura(Usuario usuario) throws FileNotFoundException {
        String fileName = "factura_" + usuario.getId() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";

        PdfWriter writer = new PdfWriter(fileName);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Factura de Compra"));
        document.add(new Paragraph("ID Cliente: " + usuario.getId()));
        document.add(new Paragraph("Nombre: " + usuario.getNombre() + " " + usuario.getApellido()));
        document.add(new Paragraph("Cédula: " + usuario.getCedula()));
        document.add(new Paragraph("Email: " + usuario.getEmail()));
        document.add(new Paragraph("Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        Table table = new Table(4);
        table.addCell(new Cell().add(new Paragraph("Producto")));
        table.addCell(new Cell().add(new Paragraph("Precio")));
        table.addCell(new Cell().add(new Paragraph("Cantidad")));
        table.addCell(new Cell().add(new Paragraph("Total")));

        double total = 0;
        for (Producto producto : usuario.getCarrito()) {
            table.addCell(new Cell().add(new Paragraph(producto.getNombre())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", producto.getPrecio()))));
            table.addCell(new Cell().add(new Paragraph("1")));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f", producto.getPrecio()))));
            total += producto.getPrecio();
        }

        document.add(table);
        document.add(new Paragraph("Total: $" + String.format("%.2f", total)));
        document.close();
    }
}
