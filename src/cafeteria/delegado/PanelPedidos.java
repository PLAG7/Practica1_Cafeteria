package cafeteria.delegado;

import cafeteria.modelo.DetallePedido;
import cafeteria.modelo.GestorPedidos;
import cafeteria.modelo.GestorProductos;
import cafeteria.modelo.Pedido;
import cafeteria.modelo.Producto;
import cafeteria.util.UtilGenerico;
import cafeteria.util.Validador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;

public class PanelPedidos extends JPanel {
    private JTextField txtCliente;
    private JComboBox<Producto> cmbProducto;
    private JSpinner spnCantidad;
    private JTable tablaLineas;
    private DefaultTableModel modeloLineas;
    private JLabel lblSubtotal, lblIva, lblTotal;
    private JButton btnAgregarLinea, btnQuitarLinea, btnConfirmar, btnCancelar;

    private GestorProductos gestorProductos;
    private GestorPedidos gestorPedidos;
    private VentanaPrincipal ventanaPrincipal;
    private Pedido pedidoEnCurso;

    public PanelPedidos(GestorProductos gestorProductos, GestorPedidos gestorPedidos, VentanaPrincipal ventanaPrincipal) {
        this.gestorProductos = gestorProductos;
        this.gestorPedidos = gestorPedidos;
        this.ventanaPrincipal = ventanaPrincipal;
        this.pedidoEnCurso = gestorPedidos.crearPedido("");

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelNorte = new JPanel(new BorderLayout(5, 10));
        JLabel lblTitulo = new JLabel("Modulo Pedidos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtCliente = new JTextField(20);
        panelForm.add(txtCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panelForm.add(new JLabel("Producto:"), gbc);
        gbc.gridx = 1;
        cmbProducto = new JComboBox<>();
        cmbProducto.setPreferredSize(new Dimension(220, 25));
        panelForm.add(cmbProducto, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 3;
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 500, 1));
        spnCantidad.setPreferredSize(new Dimension(65, 25));
        panelForm.add(spnCantidad, gbc);

        gbc.gridx = 4;
        btnAgregarLinea = new JButton("Agregar linea");
        panelForm.add(btnAgregarLinea, gbc);

        panelNorte.add(panelForm, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        ArrayList<String> columnas = UtilGenerico.crearLista("Producto", "Cantidad", "P. unitario", "Subtotal");
        ArrayList<Integer> anchos = UtilGenerico.crearLista(220, 90, 110, 110);

        modeloLineas = new DefaultTableModel(columnas.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaLineas = new JTable(modeloLineas);
        for (int i = 0; i < anchos.size(); i++) {
            tablaLineas.getColumnModel().getColumn(i).setPreferredWidth(anchos.get(i));
        }

        add(new JScrollPane(tablaLineas), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout(10, 10));
        JPanel panelBotonesAccion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnConfirmar = new JButton("Confirmar pedido");
        btnQuitarLinea = new JButton("Quitar linea");
        btnCancelar = new JButton("Cancelar");

        panelBotonesAccion.add(btnConfirmar);
        panelBotonesAccion.add(btnQuitarLinea);
        panelBotonesAccion.add(btnCancelar);

        JPanel panelTotales = new JPanel(new GridLayout(3, 2, 15, 4));
        panelTotales.add(new JLabel("Subtotal:"));
        lblSubtotal = new JLabel("$0.00", SwingConstants.RIGHT);
        panelTotales.add(lblSubtotal);

        panelTotales.add(new JLabel("IVA (15%):"));
        lblIva = new JLabel("$0.00", SwingConstants.RIGHT);
        panelTotales.add(lblIva);

        JLabel lblTxtTotal = new JLabel("Total a pagar:");
        lblTxtTotal.setFont(new Font("Arial", Font.BOLD, 13));
        panelTotales.add(lblTxtTotal);

        lblTotal = new JLabel("$0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 13));
        panelTotales.add(lblTotal);

        panelSur.add(panelBotonesAccion, BorderLayout.WEST);
        panelSur.add(panelTotales, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);

        btnAgregarLinea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarLinea();
            }
        });

        btnQuitarLinea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarLinea();
            }
        });

        btnConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarPedido();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarPedido();
            }
        });

        cargarComboProductos();
    }

    public void cargarComboProductos() {
        cmbProducto.removeAllItems();
        for (Producto p : gestorProductos.getProductos()) {
            cmbProducto.addItem(p);
        }
    }

    public void agregarLinea() {
        Producto seleccionado = (Producto) cmbProducto.getSelectedItem();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this, "No hay productos disponibles en el catalogo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cantStr = spnCantidad.getValue().toString();
        ArrayList<String> errores = Validador.validarCantidad(cantStr);
        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = Integer.parseInt(cantStr);
        int cantidadYaEnPedido = 0;
        for (DetallePedido dp : pedidoEnCurso.getDetalles()) {
            if (dp.getProducto().getCodigo().equalsIgnoreCase(seleccionado.getCodigo())) {
                cantidadYaEnPedido += dp.getCantidad();
            }
        }

        if (seleccionado.estaAgotado() || !seleccionado.hayStock(cantidad + cantidadYaEnPedido)) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Stock actual: " + seleccionado.getStock(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DetallePedido nuevaLinea = new DetallePedido(seleccionado, cantidad, seleccionado.getPrecio());
        pedidoEnCurso.agregarDetalle(nuevaLinea);
        actualizarTotales();
    }

    public void quitarLinea() {
        int fila = tablaLineas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una linea en la tabla para quitar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        pedidoEnCurso.getDetalles().remove(fila);
        actualizarTotales();
    }

    public void confirmarPedido() {
        String cliente = txtCliente.getText().trim();
        if (cliente.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (pedidoEnCurso.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El pedido debe tener al menos una linea.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pedidoEnCurso.setCliente(cliente);
        String totalFormateado = String.format(Locale.US, "%.2f", pedidoEnCurso.calcularTotal());
        int resp = JOptionPane.showConfirmDialog(this, "¿Confirmar pedido? Total a pagar: $" + totalFormateado, "Confirmar pedido", JOptionPane.YES_NO_OPTION);

        if (resp == JOptionPane.YES_OPTION) {
            if (gestorPedidos.confirmarPedido(pedidoEnCurso)) {
                JOptionPane.showMessageDialog(this, "Pedido registrado con exito. Total a pagar: $" + totalFormateado, "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
                prepararNuevoPedido();
                ventanaPrincipal.actualizarEstadoInferior();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo confirmar el pedido por falta de stock.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void cancelarPedido() {
        prepararNuevoPedido();
    }

    private void prepararNuevoPedido() {
        pedidoEnCurso = gestorPedidos.crearPedido("");
        txtCliente.setText("");
        spnCantidad.setValue(1);
        cargarComboProductos();
        actualizarTotales();
    }

    public void actualizarTotales() {
        modeloLineas.setRowCount(0);
        for (DetallePedido dp : pedidoEnCurso.getDetalles()) {
            modeloLineas.addRow(new Object[]{
                dp.getProducto().getNombre(),
                dp.getCantidad(),
                "$" + String.format(Locale.US, "%.2f", dp.getPrecioUnitario()),
                "$" + String.format(Locale.US, "%.2f", dp.calcularSubtotal())
            });
        }
        lblSubtotal.setText("$" + String.format(Locale.US, "%.2f", pedidoEnCurso.calcularSubtotal()));
        lblIva.setText("$" + String.format(Locale.US, "%.2f", pedidoEnCurso.calcularIva()));
        lblTotal.setText("$" + String.format(Locale.US, "%.2f", pedidoEnCurso.calcularTotal()));
    }
}