package cafeteria;

import cafeteria.delegado.VentanaPrincipal;
import cafeteria.modelo.GestorArchivos;
import cafeteria.modelo.GestorPedidos;
import cafeteria.modelo.GestorProductos;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GestorArchivos gestorArchivos = new GestorArchivos();
                GestorProductos gestorProductos = new GestorProductos(gestorArchivos);
                GestorPedidos gestorPedidos = new GestorPedidos(gestorProductos, gestorArchivos);

                VentanaPrincipal ventana = new VentanaPrincipal(gestorProductos, gestorPedidos);
                ventana.setVisible(true);
            }
        });
    }
}