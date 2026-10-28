package cafeteria.modelo;

import java.util.Locale;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    public void reducirStock(int cantidad) {
        this.stock -= cantidad;
    }

    public boolean hayStock(int cantidad) {
        return this.stock >= cantidad;
    }

    public boolean estaAgotado() {
        return this.stock <= 0;
    }

    public String toLinea() {
        return codigo + ";" + nombre + ";" + String.format(Locale.US, "%.2f", precio) + ";" + stock;
    }

    public String getCodigo() { 
    	return codigo; 
    }
    public void setCodigo(String codigo) { 
    	this.codigo = codigo; 
    }
    public String getNombre() { 
    	return nombre; 
    }
    public void setNombre(String nombre) { 
    	this.nombre = nombre; 
    }
    public double getPrecio() { 
    	return precio; 
    }
    public void setPrecio(double precio) { 
    	this.precio = precio; 
    }
    public int getStock() { 
    	return stock; 
    }
    public void setStock(int stock) { 
    	this.stock = stock; 
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " ($" + String.format(Locale.US, "%.2f", precio) + ")";
    }
}