package cafeteria;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class GestorArchivos {
    private final String RUTA_PRODUCTOS = "productos.txt";
    private final String RUTA_PEDIDOS = "pedidos.txt";

    public void guardarProductos(ArrayList<Producto> lista) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RUTA_PRODUCTOS))) {
            for (Producto p : lista) {
                pw.println(p.toLinea());
            }
        } catch (Exception e) {
            System.out.println("Error al guardar productos.");
        }
    }

    public ArrayList<Producto> cargarProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        File archivo = new File(RUTA_PRODUCTOS);
        if (archivo.exists()) {
            try (Scanner sc = new Scanner(archivo)) {
                while (sc.hasNextLine()) {
                    String[] datos = sc.nextLine().split(";");
                    if (datos.length == 4) {
                        lista.add(new Producto(datos[0], datos[1], Double.parseDouble(datos[2]), Integer.parseInt(datos[3])));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al cargar productos.");
            }
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
        } catch (Exception e) {
            System.out.println("Error al guardar pedidos.");
        }
    }

    public ArrayList<Pedido> cargarPedidos(ArrayList<Producto> catalogo) {
        ArrayList<Pedido> lista = new ArrayList<>();
        File archivo = new File(RUTA_PEDIDOS);
        if (archivo.exists()) {
            try (Scanner sc = new Scanner(archivo)) {
                Pedido pedidoActual = null;
                while (sc.hasNextLine()) {
                    String[] datos = sc.nextLine().split(";");
                    if (datos.length == 6) {
                        int id = Integer.parseInt(datos[0]);
                        LocalDate fecha = LocalDate.parse(datos[1]);
                        String cliente = datos[2];
                        String codProd = datos[3];
                        int cant = Integer.parseInt(datos[4]);
                        double precioU = Double.parseDouble(datos[5]);

                        if (pedidoActual == null || pedidoActual.getId() != id) {
                            pedidoActual = new Pedido(id, cliente, fecha);
                            lista.add(pedidoActual);
                        }

                        Producto pEncontrado = null;
                        for (Producto p : catalogo) {
                            if (p.getCodigo().equals(codProd)) {
                                pEncontrado = p;
                                break;
                            }
                        }

                        if (pEncontrado != null) {
                            pedidoActual.agregarDetalle(new DetallePedido(pEncontrado, cant, precioU));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al cargar pedidos.");
            }
        }
        return lista;
    }
}