package cafeteria;

import java.time.LocalDate;
import java.util.ArrayList;

public class GestorPedidos {
    private ArrayList<Pedido> pedidos;
    private int siguienteId;

    public GestorPedidos(ArrayList<Pedido> cargados) {
        this.pedidos = cargados != null ? cargados : new ArrayList<>();
        this.siguienteId = 1;
        for (Pedido p : pedidos) {
            if (p.getId() >= siguienteId) {
                siguienteId = p.getId() + 1;
            }
        }
    }

    public Pedido crearPedido(String cliente) {
        return new Pedido(siguienteId++, cliente, LocalDate.now());
    }

    public void confirmarPedido(Pedido pedido) {
        if (!pedido.getDetalles().isEmpty()) {
            pedidos.add(pedido);
        }
    }

    public void listar() {
        System.out.println("\n--- LISTA DE PEDIDOS ---");
        for (Pedido p : pedidos) {
            System.out.printf("Pedido #%d | Cliente: %s | Total: $%.2f\n", p.getId(), p.getCliente(), p.calcularTotal());
        }
    }

    public void generarReporteVentas(LocalDate fecha) {
        int numPedidos = 0;
        double totalVendido = 0;

        for (Pedido p : pedidos) {
            if (p.getFecha().equals(fecha)) {
                numPedidos++;
                totalVendido += p.calcularTotal();
            }
        }

        System.out.println("\n--- REPORTE DE VENTAS (" + fecha + ") ---");
        System.out.println("Numero de pedidos: " + numPedidos);
        System.out.printf("Total vendido: $%.2f\n", totalVendido);

        String mejorProd = productoMasVendido(fecha);
        System.out.println("Producto de mayor demanda: " + (mejorProd.isEmpty() ? "Ninguno" : mejorProd));
    }

    public String productoMasVendido(LocalDate fecha) {
        ArrayList<String> codigos = new ArrayList<>();
        ArrayList<Integer> cantidades = new ArrayList<>();

        for (Pedido p : pedidos) {
            if (p.getFecha().equals(fecha)) {
                for (DetallePedido dp : p.getDetalles()) {
                    String cod = dp.getProducto().getCodigo();
                    int idx = codigos.indexOf(cod);
                    if (idx == -1) {
                        codigos.add(cod);
                        cantidades.add(dp.getCantidad());
                    } else {
                        cantidades.set(idx, cantidades.get(idx) + dp.getCantidad());
                    }
                }
            }
        }

        if (codigos.isEmpty()) return "";

        int maxCant = -1;
        String codMax = "";
        for (int i = 0; i < codigos.size(); i++) {
            if (cantidades.get(i) > maxCant) {
                maxCant = cantidades.get(i);
                codMax = codigos.get(i);
            }
        }

        return codMax + " (" + maxCant + " unidades)";
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }
}