package store.electronic.model;

import store.electronic.model.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InventarioService {
    private Map<String, Producto> productos;

    public InventarioService() {
        this.productos = new HashMap<>();
        inicializarInventario();
    }

    private void inicializarInventario() {
        agregarProducto(new Producto("LAP001", "Laptop Dell XPS 13", 1299.99, 10, "Laptops"));
        agregarProducto(new Producto("LAP002", "MacBook Pro 14\"", 1999.99, 8, "Laptops"));
        agregarProducto(new Producto("MON001", "Monitor LG 27\"", 299.99, 15, "Monitores"));
        agregarProducto(new Producto("TEC001", "Teclado Mecánico RGB", 89.99, 20, "Periféricos"));
        agregarProducto(new Producto("MOU001", "Mouse Gaming", 49.99, 25, "Periféricos"));
    }

    public void agregarProducto(Producto producto) {
        productos.put(producto.getId(), producto);
    }

    public Producto obtenerProducto(String id) {
        return productos.get(id);
    }

    public List<Producto> listarProductos() {
        return new ArrayList<>(productos.values());
    }

    public boolean actualizarStock(String id, int cantidad) {
        Producto producto = productos.get(id);
        if (producto != null && producto.getStock() >= cantidad) {
            producto.setStock(producto.getStock() - cantidad);
            return true;
        }
        return false;
    }
}
