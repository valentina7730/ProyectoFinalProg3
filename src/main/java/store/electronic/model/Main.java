package store.electronic.model;

import com.github.javafaker.Faker;
import store.electronic.model.Usuario;
import store.electronic.model.Producto;
import store.electronic.model.InventarioService;
import store.electronic.model.PDFGeneratorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Locale;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventarioService inventarioService = new InventarioService();
    private static final PDFGeneratorService pdfService = new PDFGeneratorService();
    private static final List<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        generarUsuarios();

        while (true) {
            try {
                mostrarMenu();
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        mostrarProductos();
                        break;
                    case 2:
                        realizarCompra();
                        break;
                    case 3:
                        mostrarUsuarios();
                        break;
                    case 4:
                        logger.info("Saliendo del sistema...");
                        return;
                    default:
                        logger.warn("Opción no válida");
                }
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage());
            }
        }
    }

    private static void generarUsuarios() {
        Faker faker = new Faker(new Locale("es"));
        for (int i = 0; i < 20; i++) {
            String cedula = String.format("%08d", faker.number().numberBetween(10000000, 99999999));
            usuarios.add(new Usuario(
                    cedula,
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.internet().emailAddress()
            ));
        }
        logger.info("Se generaron 20 usuarios de prueba");
    }

    private static void mostrarMenu() {
        logger.info("\n=== MENÚ PRINCIPAL ===");
        logger.info("1. Ver productos disponibles");
        logger.info("2. Realizar compra");
        logger.info("3. Ver usuarios");
        logger.info("4. Salir");
        logger.info("Seleccione una opción: ");
    }

    private static void mostrarProductos() {
        logger.info("\n=== PRODUCTOS DISPONIBLES ===");
        for (Producto producto : inventarioService.listarProductos()) {
            logger.info(producto.toString());
        }
    }

    private static void mostrarUsuarios() {
        logger.info("\n=== USUARIOS REGISTRADOS ===");
        for (Usuario usuario : usuarios) {
            logger.info(String.format("ID: %s, Nombre: %s %s, Cédula: %s",
                    usuario.getId(), usuario.getNombre(), usuario.getApellido(), usuario.getCedula()));
        }
    }

    private static void realizarCompra() {
        try {
            logger.info("Ingrese la cédula del usuario: ");
            String cedula = scanner.nextLine();

            Usuario usuario = usuarios.stream()
                    .filter(u -> u.getCedula().equals(cedula))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            while (true) {
                mostrarProductos();
                logger.info("\nIngrese el ID del producto (o 'fin' para terminar): ");
                String productoId = scanner.nextLine();

                if (productoId.equalsIgnoreCase("fin")) {
                    break;
                }

                Producto producto = inventarioService.obtenerProducto(productoId);
                if (producto == null) {
                    logger.warn("Producto no encontrado");
                    continue;
                }

                if (producto.getStock() > 0) {
                    usuario.agregarAlCarrito(producto);
                    inventarioService.actualizarStock(productoId, 1);
                    logger.info("Producto agregado al carrito");
                } else {
                    logger.warn("Producto sin stock disponible");
                }
            }

            if (!usuario.getCarrito().isEmpty()) {
                pdfService.generarFactura(usuario);
                logger.info("Factura generada exitosamente");
            }
        } catch (Exception e) {
            logger.error("Error al realizar la compra: " + e.getMessage());
        }
    }
}
