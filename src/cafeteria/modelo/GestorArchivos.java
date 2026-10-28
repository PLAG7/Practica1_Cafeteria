package cafeteria.modelo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class GestorArchivos {
    private static final String RUTA_PRODUCTOS = "productos.txt";
    private static final String RUTA_PEDIDOS = "pedidos.txt";

    public void guardarProductos(ArrayList<Producto> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_PRODUCTOS))) {
            for (Producto p : lista) {
                pw.println(p.toLinea());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        File archivo = new File(RUTA_PRODUCTOS);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lista;
        }
        try (Scanner sc = new Scanner(archivo)) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty()) {
                    String[] datos = linea.split(";");
                    if (datos.length == 4) {
                        double precio = Double.parseDouble(datos[2].replace(",", "."));
                        int stock = Integer.parseInt(datos[3]);
                        lista.add(new Producto(datos[0], datos[1], precio, stock));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void guardarPedidos(ArrayList<Pedido> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_PEDIDOS))) {
            for (Pedido p : lista) {
                for (String linea : p.toLineas()) {
                    pw.println(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Pedido> cargarPedidos(ArrayList<Producto> catalogo) {
        ArrayList<Pedido> lista = new ArrayList<>();
        File archivo = new File(RUTA_PEDIDOS);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lista;
        }
        try (Scanner sc = new Scanner(archivo)) {
            Pedido pedidoActual = null;
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty()) {
                    String[] datos = linea.split(";");
                    if (datos.length == 6) {
                        int id = Integer.parseInt(datos[0]);
                        LocalDate fecha = LocalDate.parse(datos[1]);
                        String cliente = datos[2];
                        String codProd = datos[3];
                        int cant = Integer.parseInt(datos[4]);
                        double precioU = Double.parseDouble(datos[5].replace(",", "."));

                        if (pedidoActual == null || pedidoActual.getId() != id) {
                            pedidoActual = new Pedido(id, cliente, fecha);
                            lista.add(pedidoActual);
                        }

                        Producto prod = null;
                        for (Producto p : catalogo) {
                            if (p.getCodigo().equalsIgnoreCase(codProd)) {
                                prod = p;
                                break;
                            }
                        }
                        if (prod == null) {
                            prod = new Producto(codProd, codProd, precioU, 0);
                        }
                        pedidoActual.agregarDetalle(new DetallePedido(prod, cant, precioU));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}