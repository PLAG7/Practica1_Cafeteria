package cafeteria;

import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido {
    private int id;
    private LocalDate fecha;
    private String cliente;
    private ArrayList<DetallePedido> detalles;

    public Pedido(int id, String cliente, LocalDate fecha) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetallePedido detalle) {
        detalles.add(detalle);
    }

    public double calcularTotal() {
        double total = 0;
        for (DetallePedido dp : detalles) {
            total += dp.calcularSubtotal();
        }
        return total;
    }

    public ArrayList<String> toLineas() {
        ArrayList<String> lineas = new ArrayList<>();
        for (DetallePedido dp : detalles) {
            lineas.add(id + ";" + fecha.toString() + ";" + cliente + ";" + dp.getProducto().getCodigo() + ";" + dp.getCantidad() + ";" + dp.getPrecioUnitario());
        }
        return lineas;
    }

    public int getId() { return id; }
    public LocalDate getFecha() { 
    	return fecha; 
    }
    public String getCliente() { 
    	return cliente; 
    }
    public ArrayList<DetallePedido> getDetalles() { 
    	return detalles; 
    }
}