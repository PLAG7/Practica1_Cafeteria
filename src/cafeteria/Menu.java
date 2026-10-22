package cafeteria;

import java.time.LocalDate;
import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private GestorProductos productos;
    private GestorPedidos pedidos;
    private GestorArchivos archivos;

    public Menu() {
        scanner = new Scanner(System.in);
        archivos = new GestorArchivos();
    }

    public void iniciar() {
        productos = new GestorProductos(archivos.cargarProductos());
        pedidos = new GestorPedidos(archivos.cargarPedidos(productos.getProductos()));
        mostrarMenu();
    }

    public void mostrarMenu() {
        int opcion = 0;
        do {
            System.out.println("\n===== CAFETERIA EL BUEN SABOR =====");
            System.out.println("Registrar producto --------[1]");
            System.out.println("Listar productos ----------[2]");
            System.out.println("Reponer stock -------------[3]");
            System.out.println("Registrar pedido ----------[4]");
            System.out.println("Generar reporte de ventas -[5]");
            System.out.println("Salir ---------------------[6]");
            System.out.print("Seleccione una opcion: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    opcionRegistrarProducto();
                    break;
                case 2:
                    productos.listar();
                    break;
                case 3:
                    opcionReponerStock();
                    break;
                case 4:
                    opcionRegistrarPedido();
                    break;
                case 5:
                    opcionReporte();
                    break;
                case 6:
                    archivos.guardarProductos(productos.getProductos());
                    archivos.guardarPedidos(pedidos.getPedidos());
                    System.out.println("Datos guardados. Saliendo del sistema");
                    break;
                default:
                    System.out.println("Opcion no valida. Intente de nuevo.");
            }
        } while (opcion != 6);
    }

    public void opcionRegistrarProducto() {
        try {
            System.out.print("Codigo: ");
            String codigo = scanner.nextLine();
            if (codigo.trim().isEmpty()) throw new Exception("Codigo no puede estar vacio");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            
            System.out.print("Precio: ");
            double precio = Double.parseDouble(scanner.nextLine());
            if (precio <= 0) throw new Exception("Precio debe ser mayor a 0");

            System.out.print("Stock inicial: ");
            int stock = Integer.parseInt(scanner.nextLine());
            if (stock < 0) throw new Exception("Stock no puede ser negativo");

            if (productos.registrar(new Producto(codigo, nombre, precio, stock))) {
                archivos.guardarProductos(productos.getProductos());
                System.out.println("Producto registrado con exito.");
            } else {
                System.out.println("Error: El codigo ya existe.");
            }
        } catch (Exception e) {
            System.out.println("Entrada invalida: " + e.getMessage());
        }
    }

    public void opcionReponerStock() {
        try {
            System.out.print("Codigo del producto: ");
            String codigo = scanner.nextLine();
            System.out.print("Cantidad a reponer: ");
            int cantidad = Integer.parseInt(scanner.nextLine());

            if (productos.reponerStock(codigo, cantidad)) {
                archivos.guardarProductos(productos.getProductos());
                System.out.println("Stock actualizado correctamente.");
            } else {
                System.out.println("Error: Producto no encontrado o cantidad invalida.");
            }
        } catch (Exception e) {
            System.out.println("Entrada invalida.");
        }
    }

    public void opcionRegistrarPedido() {
        System.out.print("Nombre del cliente: ");
        String cliente = scanner.nextLine();
        Pedido nuevoPedido = pedidos.crearPedido(cliente);
        boolean agregarMas = true;

        while (agregarMas) {
            productos.listar();
            System.out.print("\nIngrese el codigo del producto (o 'fin' para terminar): ");
            String codigo = scanner.nextLine();

            if (codigo.equalsIgnoreCase("fin")) {
                agregarMas = false;
                break;
            }

            Producto prod = productos.buscarPorCodigo(codigo);
            if (prod == null) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            try {
                System.out.print("Cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a 0.");
                    continue;
                }

                if (!productos.hayStock(codigo, cantidad)) {
                    System.out.println("Stock insuficiente. Stock actual: " + prod.getStock());
                    continue;
                }

                nuevoPedido.agregarDetalle(new DetallePedido(prod, cantidad, prod.getPrecio()));
                System.out.printf("Agregado. Subtotal: $%.2f\n", (cantidad * prod.getPrecio()));

            } catch (Exception e) {
                System.out.println("Cantidad invalida.");
            }
        }

        if (nuevoPedido.getDetalles().isEmpty()) {
            System.out.println("Pedido cancelado por falta de productos.");
            return;
        }

        System.out.printf("\nTOTAL A PAGAR: $%.2f\n", nuevoPedido.calcularTotal());
        System.out.print("¿Confirmar pedido? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            for (DetallePedido dp : nuevoPedido.getDetalles()) {
                productos.descontarStock(dp.getProducto().getCodigo(), dp.getCantidad());
            }
            pedidos.confirmarPedido(nuevoPedido);
            archivos.guardarProductos(productos.getProductos());
            archivos.guardarPedidos(pedidos.getPedidos());
            System.out.println("Pedido registrado con exito.");
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    public void opcionReporte() {
        pedidos.generarReporteVentas(LocalDate.now());
    }

    public static void main(String[] args) {
        Menu aplicacion = new Menu();
        aplicacion.iniciar();
    }
}