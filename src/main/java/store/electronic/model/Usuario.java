package store.electronic.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String cedula;
    private String nombre;
    private String apellido;
    private String email;
    private List<Producto> carrito;

    public Usuario(String cedula, String nombre, String apellido, String email) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.carrito = new ArrayList<>();
    }

    public String getId() {
        return cedula + nombre.charAt(0) + apellido.charAt(0);
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Producto> getCarrito() { return carrito; }
    public void agregarAlCarrito(Producto producto) { carrito.add(producto); }
}
