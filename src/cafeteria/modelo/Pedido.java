package cafeteria.modelo;

import cafeteria.util.UtilGenerico;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class Pedido {
    public static final double PORCENTAJE_IVA = 0.15;
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
        UtilGenerico.agregarVarios(this.detalles, detalle);
    }

    public double calcularSubtotal() {
        double sub = 0;
        for (DetallePedido dp : detalles) {
            sub += dp.calcularSubtotal();
        }
        return sub;
    }

    public double calcularIva() {
        return calcularSubtotal() * PORCENTAJE_IVA;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIva();
    }

    public ArrayList<String> toLineas() {
        ArrayList<String> lineas = new ArrayList<>();
        for (DetallePedido dp : detalles) {
            lineas.add(id + ";" + fecha.toString() + ";" + cliente + ";" + dp.getProducto().getCodigo() + ";" + dp.getCantidad() + ";" + String.format(Locale.US, "%.2f", dp.getPrecioUnitario()));
        }
        return lineas;
    }

    public int getId() { 
    	return id; 
    }
    public void setId(int id) { 
    	this.id = id; 
    }
    public LocalDate getFecha() { 
    	return fecha; 
    }
    public void setFecha(LocalDate fecha) { 
    	this.fecha = fecha; 
    }
    public String getCliente() { 
    	return cliente; 
   	}
    public void setCliente(String cliente) { 
    	this.cliente = cliente; 
    }
    public ArrayList<DetallePedido> getDetalles() { 
    	return detalles; 
    }
}