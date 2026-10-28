package cafeteria.util;

import java.util.ArrayList;

public class UtilGenerico {

    @SafeVarargs
    public static <T> ArrayList<T> crearLista(T... elementos) {
        ArrayList<T> lista = new ArrayList<>();
        for (T elemento : elementos) {
            lista.add(elemento);
        }
        return lista;
    }

    @SafeVarargs
    public static <T> void agregarVarios(ArrayList<T> lista, T... elementos) {
        if (lista == null) return;
        for (T elemento : elementos) {
            lista.add(elemento);
        }
    }
}