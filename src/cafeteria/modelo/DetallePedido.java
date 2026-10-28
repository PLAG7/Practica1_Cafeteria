package cafeteria.modelo;

public class DetallePedido {
    private Producto producto;
    private int cantidad;
    private double precioUnitario;

    public DetallePedido(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitario;
    }

    public Producto getProducto() { 
    	return producto; 
    }
    public void setProducto(Producto producto) { 
    	this.producto = producto; 
    }
    public int getCantidad() {
    	return cantidad; 
    }
    public void setCantidad(int cantidad) { 
    	this.cantidad = cantidad; 
    }
    public double getPrecioUnitario() { 
    	return precioUnitario; 
    }
    public void setPrecioUnitario(double precioUnitario) { 
    	this.precioUnitario = precioUnitario;
    }
}