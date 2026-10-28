package cafeteria.delegado;

import cafeteria.modelo.GestorPedidos;
import cafeteria.modelo.GestorProductos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {
    private GestorProductos gestorProductos;
    private GestorPedidos gestorPedidos;
    private JPanel panelContenido;
    private CardLayout cardLayout;
    private PanelProductos panelProductos;
    private PanelPedidos panelPedidos;
    private PanelReportes panelReportes;
    private JLabel lblEstadoInferior;

    public VentanaPrincipal(GestorProductos gestorProductos, GestorPedidos gestorPedidos) {
        this.gestorProductos = gestorProductos;
        this.gestorPedidos = gestorPedidos;

        setTitle("Cafeteria El Buen Sabor - v2.0");
        setSize(860, 540);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearPanelNavegacion();

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        JPanel panelInicio = crearPanelBienvenida();
        panelProductos = new PanelProductos(gestorProductos, this);
        panelPedidos = new PanelPedidos(gestorProductos, gestorPedidos, this);
        panelReportes = new PanelReportes(gestorPedidos);

        panelContenido.add(panelInicio, "INICIO");
        panelContenido.add(panelProductos, "PRODUCTOS");
        panelContenido.add(panelPedidos, "PEDIDOS");
        panelContenido.add(panelReportes, "REPORTES");

        JPanel contenedorDerecho = new JPanel(new BorderLayout());
        contenedorDerecho.add(panelContenido, BorderLayout.CENTER);

        JPanel panelBarraEstado = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
        panelBarraEstado.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        lblEstadoInferior = new JLabel();
        lblEstadoInferior.setForeground(Color.DARK_GRAY);
        panelBarraEstado.add(lblEstadoInferior);
        contenedorDerecho.add(panelBarraEstado, BorderLayout.SOUTH);

        add(contenedorDerecho, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarYSalir();
            }
        });

        actualizarEstadoInferior();
        mostrarModulo("INICIO");
    }

    public void crearPanelNavegacion() {
        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setPreferredSize(new Dimension(180, 0));
        panelNav.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JPanel panelBotonesArriba = new JPanel(new GridLayout(3, 1, 0, 12));
        JButton btnProductos = new JButton("Productos");
        JButton btnPedidos = new JButton("Pedidos");
        JButton btnReportes = new JButton("Reportes");

        Dimension dimBoton = new Dimension(140, 38);
        btnProductos.setPreferredSize(dimBoton);
        btnPedidos.setPreferredSize(dimBoton);
        btnReportes.setPreferredSize(dimBoton);

        panelBotonesArriba.add(btnProductos);
        panelBotonesArriba.add(btnPedidos);
        panelBotonesArriba.add(btnReportes);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setPreferredSize(dimBoton);

        panelNav.add(panelBotonesArriba, BorderLayout.NORTH);
        panelNav.add(btnSalir, BorderLayout.SOUTH);

        btnProductos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarModulo("PRODUCTOS");
            }
        });

        btnPedidos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarModulo("PEDIDOS");
            }
        });

        btnReportes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarModulo("REPORTES");
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarYSalir();
            }
        });

        add(panelNav, BorderLayout.WEST);
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout(15, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JPanel cabecera = new JPanel(new GridLayout(2, 1, 5, 5));
        JLabel lblTit = new JLabel("Sistema de Gestion de Pedidos v2.0");
        lblTit.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel lblSub = new JLabel("Elija un modulo en el panel de navegacion.");
        lblSub.setForeground(Color.DARK_GRAY);
        cabecera.add(lblTit);
        cabecera.add(lblSub);
        panel.add(cabecera, BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(1, 3, 15, 15));
        tarjetas.add(crearTarjetaModulo("Productos", "Registrar, editar, listar y reponer stock"));
        tarjetas.add(crearTarjetaModulo("Pedidos", "Registrar pedidos con varios productos"));
        tarjetas.add(crearTarjetaModulo("Reportes", "Ventas del dia y productos mas vendidos"));

        JPanel envoltorio = new JPanel(new BorderLayout());
        envoltorio.add(tarjetas, BorderLayout.NORTH);
        panel.add(envoltorio, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearTarjetaModulo(String titulo, String descripcion) {
        JPanel card = new JPanel(new BorderLayout(5, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(15, 15, 25, 15)
        ));
        JLabel lblT = new JLabel(titulo);
        lblT.setFont(new Font("Arial", Font.BOLD, 15));
        JLabel lblD = new JLabel("<html>" + descripcion + "</html>");
        lblD.setForeground(Color.DARK_GRAY);
        card.add(lblT, BorderLayout.NORTH);
        card.add(lblD, BorderLayout.CENTER);
        return card;
    }

    public void mostrarModulo(String nombre) {
        if ("PRODUCTOS".equals(nombre)) {
            panelProductos.actualizarTabla();
        } else if ("PEDIDOS".equals(nombre)) {
            panelPedidos.cargarComboProductos();
            panelPedidos.actualizarTotales();
        } else if ("REPORTES".equals(nombre)) {
            panelReportes.generarReporte();
        }
        actualizarEstadoInferior();
        cardLayout.show(panelContenido, nombre);
    }

    public void actualizarEstadoInferior() {
        int numProd = gestorProductos.getProductos().size();
        int numPed = gestorPedidos.getPedidos().size();
        lblEstadoInferior.setText("Productos cargados: " + numProd + " | Pedidos registrados: " + numPed);
    }

    public void guardarYSalir() {
        gestorProductos.guardarCambios();
        gestorPedidos.guardarCambios();
        dispose();
        System.exit(0);
    }
}