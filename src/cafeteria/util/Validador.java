package cafeteria.util;

import java.time.LocalDate;
import java.util.ArrayList;

public class Validador {

    public static ArrayList<String> validarProducto(String codigo, String nombre, String precioTexto, String stockTexto, boolean validarStock) {
        ArrayList<String> errores = new ArrayList<>();

        if (codigo == null || codigo.trim().isEmpty()) {
            UtilGenerico.agregarVarios(errores, "El codigo no puede estar vacio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            UtilGenerico.agregarVarios(errores, "El nombre no puede estar vacio.");
        }

        if (precioTexto == null || precioTexto.trim().isEmpty()) {
            UtilGenerico.agregarVarios(errores, "El precio no puede estar vacio.");
        } else {
            try {
                double precio = Double.parseDouble(precioTexto.trim().replace(",", "."));
                if (precio <= 0) {
                    UtilGenerico.agregarVarios(errores, "El precio debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                UtilGenerico.agregarVarios(errores, "El precio debe ser un numero valido.");
            }
        }

        if (validarStock) {
            if (stockTexto == null || stockTexto.trim().isEmpty()) {
                UtilGenerico.agregarVarios(errores, "El stock no puede estar vacio.");
            } else {
                try {
                    int stock = Integer.parseInt(stockTexto.trim());
                    if (stock < 0) {
                        UtilGenerico.agregarVarios(errores, "El stock no puede ser negativo.");
                    }
                } catch (NumberFormatException e) {
                    UtilGenerico.agregarVarios(errores, "El stock debe ser un numero entero.");
                }
            }
        }

        return errores;
    }

    public static ArrayList<String> validarCantidad(String texto) {
        ArrayList<String> errores = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) {
            UtilGenerico.agregarVarios(errores, "La cantidad no puede estar vacia.");
            return errores;
        }
        try {
            int cant = Integer.parseInt(texto.trim());
            if (cant <= 0) {
                UtilGenerico.agregarVarios(errores, "La cantidad debe ser mayor a 0.");
            }
        } catch (NumberFormatException e) {
            UtilGenerico.agregarVarios(errores, "La cantidad debe ser un numero entero valido.");
        }
        return errores;
    }

    public static boolean validarFecha(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        try {
            LocalDate.parse(texto.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}