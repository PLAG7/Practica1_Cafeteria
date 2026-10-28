package cafeteria.delegado;

import cafeteria.modelo.GestorProductos;
import cafeteria.modelo.Producto;
import cafeteria.util.UtilGenerico;
import cafeteria.util.Validador;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;

public class PanelProductos extends JPanel {
    private JTextField txtCodigo, txtNombre, txtPrecio, txtStock;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar, btnEditar, btnReponer, btnLimpiar;
    private GestorProductos gestorProductos;
    private VentanaPrincipal ventanaPrincipal;

    public PanelProductos(GestorProductos gestorProductos, VentanaPrincipal ventanaPrincipal) {
        this.gestorProductos = gestorProductos;
        this.ventanaPrincipal = ventanaPrincipal;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelNorte = new JPanel(new BorderLayout(5, 10));
        JLabel lblTitulo = new JLabel("Modulo Productos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(12);
        panelForm.add(txtCodigo, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panelForm.add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelForm.add(new JLabel("Precio:"), gbc);
        gbc.gridx = 1;
        txtPrecio = new JTextField(12);
        panelForm.add(txtPrecio, gbc);

        gbc.gridx = 2;
        panelForm.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 3;
        txtStock = new JTextField(12);
        panelForm.add(txtStock, gbc);

        panelNorte.add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnRegistrar = new JButton("Registrar");
        btnEditar = new JButton("Editar");
        btnReponer = new JButton("Reponer stock");
        btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnReponer);
        panelBotones.add(btnLimpiar);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);

        add(panelNorte, BorderLayout.NORTH);

        ArrayList<String> columnas = UtilGenerico.crearLista("Codigo", "Nombre", "Precio", "Stock");
        ArrayList<Integer> anchos = UtilGenerico.crearLista(90, 240, 100, 90);

        modeloTabla = new DefaultTableModel(columnas.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        for (int i = 0; i < anchos.size(); i++) {
            tablaProductos.getColumnModel().getColumn(i).setPreferredWidth(anchos.get(i));
        }

        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        JLabel lblNota = new JLabel("La fila seleccionada se carga en el formulario para editarla o reponer su stock.");
        lblNota.setForeground(Color.DARK_GRAY);
        add(lblNota, BorderLayout.SOUTH);

        tablaProductos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int fila = tablaProductos.getSelectedRow();
                if (fila >= 0) {
                    txtCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
                    txtCodigo.setEditable(false);
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    String precioStr = modeloTabla.getValueAt(fila, 2).toString().replace("$", "");
                    txtPrecio.setText(precioStr);
                    txtStock.setText(modeloTabla.getValueAt(fila, 3).toString());
                }
            }
        });

        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarProducto();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProducto();
            }
        });

        btnReponer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reponerStock();
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarFormulario();
            }
        });

        actualizarTabla();
    }

    public void registrarProducto() {
        if (!txtCodigo.isEditable()) {
            JOptionPane.showMessageDialog(this, "Presione Limpiar antes de registrar un producto nuevo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cod = txtCodigo.getText().trim();
        String nom = txtNombre.getText().trim();
        String preStr = txtPrecio.getText().trim();
        String stkStr = txtStock.getText().trim();

        ArrayList<String> errores = Validador.validarProducto(cod, nom, preStr, stkStr, true);
        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores), "Error de validacion", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio = Double.parseDouble(preStr.replace(",", "."));
        int stock = Integer.parseInt(stkStr);

        if (gestorProductos.registrar(new Producto(cod.toUpperCase(), nom, precio, stock))) {
            actualizarTabla();
            limpiarFormulario();
            ventanaPrincipal.actualizarEstadoInferior();
            JOptionPane.showMessageDialog(this, "Producto registrado con exito.", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "El codigo ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto en la tabla para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cod = txtCodigo.getText().trim();
        String nom = txtNombre.getText().trim();
        String preStr = txtPrecio.getText().trim();

        ArrayList<String> errores = Validador.validarProducto(cod, nom, preStr, "0", false);
        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores), "Error de validacion", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio = Double.parseDouble(preStr.replace(",", "."));
        if (gestorProductos.editar(cod, nom, precio)) {
            actualizarTabla();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Producto editado con exito.", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo editar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void reponerStock() {
        int fila = tablaProductos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto en la tabla para reponer stock.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String cod = modeloTabla.getValueAt(fila, 0).toString();
        String cantStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad a reponer para " + cod + ":", "Reponer stock", JOptionPane.QUESTION_MESSAGE);
        if (cantStr == null) return;

        ArrayList<String> errores = Validador.validarCantidad(cantStr);
        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, String.join("\n", errores), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int cantidad = Integer.parseInt(cantStr.trim());
        if (gestorProductos.reponerStock(cod, cantidad)) {
            actualizarTabla();
            Producto p = gestorProductos.buscarPorCodigo(cod);
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Stock actualizado. Nuevo stock: " + p.getStock(), "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void limpiarFormulario() {
        txtCodigo.setText("");
        txtCodigo.setEditable(true);
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        tablaProductos.clearSelection();
    }

    public void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Producto p : gestorProductos.getProductos()) {
            modeloTabla.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                "$" + String.format(Locale.US, "%.2f", p.getPrecio()),
                p.getStock()
            });
        }
    }
}