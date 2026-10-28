package cafeteria.modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReporteVentas {
    private LocalDate fecha;
    private int numeroPedidos;
    private double subtotal;
    private double iva;
    private double total;
    private ArrayList<String> unidadesPorProducto;
    private ArrayList<String> productosMayorDemanda;

    public ReporteVentas(LocalDate fecha, int numeroPedidos, double subtotal, double iva, double total,
                         ArrayList<String> unidadesPorProducto, ArrayList<String> productosMayorDemanda) {
        this.fecha = fecha;
        this.numeroPedidos = numeroPedidos;
        this.subtotal = subtotal;
        this.iva = iva;
        this.total = total;
        this.unidadesPorProducto = unidadesPorProducto;
        this.productosMayorDemanda = productosMayorDemanda;
    }

    public LocalDate getFecha() { 
    	return fecha; 
    }
    public void setFecha(LocalDate fecha) { 
    	this.fecha = fecha; 
    }
    public int getNumeroPedidos() { 
    	return numeroPedidos; 
    }
    public void setNumeroPedidos(int numeroPedidos) { 
    	this.numeroPedidos = numeroPedidos; 
    }
    public double getSubtotal() { 
    	return subtotal; 
    }
    public void setSubtotal(double subtotal) { 
    	this.subtotal = subtotal; 
    }
    public double getIva() { 
    	return iva; 
    }
    public void setIva(double iva) { 
    	this.iva = iva; 
    }
    public double getTotal() { 
    	return total; 
    }
    public void setTotal(double total) { 
    	this.total = total; 
    }
    public ArrayList<String> getUnidadesPorProducto() { 
    	return unidadesPorProducto; 
    }
    public void setUnidadesPorProducto(ArrayList<String> unidadesPorProducto) { 
    	this.unidadesPorProducto = unidadesPorProducto; 
    }
    public ArrayList<String> getProductosMayorDemanda() { 
    	return productosMayorDemanda; 
    }
    public void setProductosMayorDemanda(ArrayList<String> productosMayorDemanda) { 
    	this.productosMayorDemanda = productosMayorDemanda; 
    }
}