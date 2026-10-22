package cafeteria;

import java.util.ArrayList;

public class GestorProductos {
    private ArrayList<Producto> productos;

    public GestorProductos(ArrayList<Producto> cargados) {
        this.productos = cargados != null ? cargados : new ArrayList<>();
    }

    public boolean registrar(Producto producto) {
        if (buscarPorCodigo(producto.getCodigo()) != null) {
            return false;
        }
        productos.add(producto);
        return true;
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public void listar() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        System.out.println("\n--- CATALOGO DE PRODUCTOS ---");
        for (Producto p : productos) {
            System.out.printf("Codigo: %s | %s | $%.2f | Stock: %d\n", p.getCodigo(), p.getNombre(), p.getPrecio(), p.getStock());
        }
    }

    public boolean reponerStock(String codigo, int cantidad) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null && cantidad > 0) {
            p.aumentarStock(cantidad);
            return true;
        }
        return false;
    }

    public boolean hayStock(String codigo, int cantidad) {
        Producto p = buscarPorCodigo(codigo);
        return p != null && p.getStock() >= cantidad;
    }

    public void descontarStock(String codigo, int cantidad) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.reducirStock(cantidad);
        }
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }
}