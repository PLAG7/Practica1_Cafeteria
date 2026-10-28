package cafeteria.modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorPedidos {
    private ArrayList<Pedido> pedidos;
    private int siguienteId;
    private GestorProductos gestorProductos;
    private GestorArchivos archivos;

    public GestorPedidos(GestorProductos gestorProductos, GestorArchivos archivos) {
        this.gestorProductos = gestorProductos;
        this.archivos = archivos;
        this.pedidos = archivos.cargarPedidos(gestorProductos.getProductos());
        this.siguienteId = 1;
        for (Pedido p : pedidos) {
            if (p.getId() >= siguienteId) {
                siguienteId = p.getId() + 1;
            }
        }
    }

    public Pedido crearPedido(String cliente) {
        return new Pedido(siguienteId, cliente, LocalDate.now());
    }

    public boolean confirmarPedido(Pedido pedido) {
        if (pedido == null || pedido.getDetalles().isEmpty()) {
            return false;
        }
        for (DetallePedido dp : pedido.getDetalles()) {
            if (!gestorProductos.hayStock(dp.getProducto().getCodigo(), dp.getCantidad())) {
                return false;
            }
        }
        for (DetallePedido dp : pedido.getDetalles()) {
            gestorProductos.descontarStock(dp.getProducto().getCodigo(), dp.getCantidad());
        }
        pedido.setId(siguienteId++);
        pedidos.add(pedido);
        archivos.guardarProductos(gestorProductos.getProductos());
        archivos.guardarPedidos(pedidos);
        return true;
    }

    public ArrayList<Pedido> ventasDelDia(LocalDate fecha) {
        ArrayList<Pedido> delDia = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getFecha().equals(fecha)) {
                delDia.add(p);
            }
        }
        return delDia;
    }

    public ReporteVentas generarReporte(LocalDate fecha) {
        ArrayList<Pedido> delDia = ventasDelDia(fecha);
        int numPedidos = delDia.size();
        double subtotal = 0;
        double iva = 0;
        double total = 0;

        ArrayList<String> nombresProd = new ArrayList<>();
        ArrayList<Integer> cantidadesProd = new ArrayList<>();

        for (Pedido p : delDia) {
            subtotal += p.calcularSubtotal();
            iva += p.calcularIva();
            total += p.calcularTotal();

            for (DetallePedido dp : p.getDetalles()) {
                String nombre = dp.getProducto().getNombre();
                int idx = nombresProd.indexOf(nombre);
                if (idx == -1) {
                    nombresProd.add(nombre);
                    cantidadesProd.add(dp.getCantidad());
                } else {
                    cantidadesProd.set(idx, cantidadesProd.get(idx) + dp.getCantidad());
                }
            }
        }

        int maxUnidades = 0;
        for (int cant : cantidadesProd) {
            if (cant > maxUnidades) {
                maxUnidades = cant;
            }
        }

        ArrayList<String> unidadesPorProducto = new ArrayList<>();
        ArrayList<String> mayorDemanda = new ArrayList<>();

        for (int i = 0; i < nombresProd.size(); i++) {
            String nom = nombresProd.get(i);
            int cant = cantidadesProd.get(i);
            boolean esMayor = (cant == maxUnidades && maxUnidades > 0);
            unidadesPorProducto.add(nom + ";" + cant + ";" + (esMayor ? "Si" : "No"));
            if (esMayor) {
                mayorDemanda.add(nom);
            }
        }

        return new ReporteVentas(fecha, numPedidos, subtotal, iva, total, unidadesPorProducto, mayorDemanda);
    }

    public ArrayList<String> productosMayorDemanda(LocalDate fecha) {
        return generarReporte(fecha).getProductosMayorDemanda();
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void guardarCambios() {
        archivos.guardarPedidos(pedidos);
    }
}