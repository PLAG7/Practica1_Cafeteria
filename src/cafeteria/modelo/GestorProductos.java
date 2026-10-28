package cafeteria.modelo;

import java.util.ArrayList;

public class GestorProductos {
    private ArrayList<Producto> productos;
    private GestorArchivos archivos;

    public GestorProductos(GestorArchivos archivos) {
        this.archivos = archivos;
        this.productos = archivos.cargarProductos();
    }

    public boolean registrar(Producto producto) {
        if (buscarPorCodigo(producto.getCodigo()) != null) {
            return false;
        }
        productos.add(producto);
        archivos.guardarProductos(productos);
        return true;
    }

    public boolean editar(String codigo, String nuevoNombre, double nuevoPrecio) {
        Producto p = buscarPorCodigo(codigo);
        if (p == null || nuevoPrecio <= 0 || nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return false;
        }
        p.setNombre(nuevoNombre.trim());
        p.setPrecio(nuevoPrecio);
        archivos.guardarProductos(productos);
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

    public boolean reponerStock(String codigo, int cantidad) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null && cantidad > 0) {
            p.aumentarStock(cantidad);
            archivos.guardarProductos(productos);
            return true;
        }
        return false;
    }

    public boolean hayStock(String codigo, int cantidad) {
        Producto p = buscarPorCodigo(codigo);
        return p != null && p.hayStock(cantidad);
    }

    public void descontarStock(String codigo, int cantidad) {
        Producto p = buscarPorCodigo(codigo);
        if (p != null) {
            p.reducirStock(cantidad);
            archivos.guardarProductos(productos);
        }
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void guardarCambios() {
        archivos.guardarProductos(productos);
    }
}