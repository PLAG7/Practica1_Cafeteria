package cafeteria.delegado;

import cafeteria.modelo.GestorPedidos;
import cafeteria.modelo.ReporteVentas;
import cafeteria.util.UtilGenerico;
import cafeteria.util.Validador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class PanelReportes extends JPanel {
    private JTextField txtFecha;
    private JButton btnGenerar;
    private JLabel lblResumen;
    private JLabel lblValPedidos, lblValSubtotal, lblValIva, lblValTotal;
    private JTable tablaDemanda;
    private DefaultTableModel modeloDemanda;
    private GestorPedidos gestorPedidos;

    public PanelReportes(GestorPedidos gestorPedidos) {
        this.gestorPedidos = gestorPedidos;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelNorte = new JPanel(new BorderLayout(5, 10));
        JLabel lblTitulo = new JLabel("Modulo Reportes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelFiltro.add(new JLabel("Fecha:"));
        txtFecha = new JTextField(LocalDate.now().toString(), 12);
        panelFiltro.add(txtFecha);
        btnGenerar = new JButton("Generar");
        panelFiltro.add(btnGenerar);
        panelNorte.add(panelFiltro, BorderLayout.CENTER);

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 4, 15, 10));
        lblValPedidos = new JLabel("0");
        lblValSubtotal = new JLabel("$0.00");
        lblValIva = new JLabel("$0.00");
        lblValTotal = new JLabel("$0.00");

        panelTarjetas.add(crearTarjeta("Pedidos", lblValPedidos));
        panelTarjetas.add(crearTarjeta("Subtotal", lblValSubtotal));
        panelTarjetas.add(crearTarjeta("IVA", lblValIva));
        panelTarjetas.add(crearTarjeta("Total", lblValTotal));

        panelNorte.add(panelTarjetas, BorderLayout.SOUTH);
        add(panelNorte, BorderLayout.NORTH);

        ArrayList<String> columnas = UtilGenerico.crearLista("Producto", "Unidades vendidas", "Mayor demanda");
        ArrayList<Integer> anchos = UtilGenerico.crearLista(240, 140, 120);

        modeloDemanda = new DefaultTableModel(columnas.toArray(), 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaDemanda = new JTable(modeloDemanda);
        for (int i = 0; i < anchos.size(); i++) {
            tablaDemanda.getColumnModel().getColumn(i).setPreferredWidth(anchos.get(i));
        }

        tablaDemanda.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String mayor = table.getValueAt(row, 2).toString();
                if ("Si".equalsIgnoreCase(mayor)) {
                    c.setBackground(new Color(220, 235, 252));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }
                return c;
            }
        });

        add(new JScrollPane(tablaDemanda), BorderLayout.CENTER);

        lblResumen = new JLabel("Los productos de mayor demanda aparecen resaltados en la tabla.");
        lblResumen.setForeground(Color.DARK_GRAY);
        add(lblResumen, BorderLayout.SOUTH);

        btnGenerar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarReporte();
            }
        });

        generarReporte();
    }

    private JPanel crearTarjeta(String titulo, JLabel lblValor) {
        JPanel tarjeta = new JPanel(new BorderLayout(5, 5));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        JLabel lblTit = new JLabel(titulo);
        lblTit.setForeground(Color.DARK_GRAY);
        lblValor.setFont(new Font("Arial", Font.BOLD, 16));
        tarjeta.add(lblTit, BorderLayout.NORTH);
        tarjeta.add(lblValor, BorderLayout.CENTER);
        return tarjeta;
    }

    public void generarReporte() {
        String fechaTexto = txtFecha.getText().trim();
        if (!Validador.validarFecha(fechaTexto)) {
            JOptionPane.showMessageDialog(this, "Formato de fecha invalido. Use AAAA-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            limpiarReporte();
            return;
        }

        LocalDate fecha = LocalDate.parse(fechaTexto);
        ReporteVentas reporte = gestorPedidos.generarReporte(fecha);
        mostrarResumen(reporte);
        llenarTabla(reporte);
    }

    public void mostrarResumen(ReporteVentas reporte) {
        lblValPedidos.setText(String.valueOf(reporte.getNumeroPedidos()));
        lblValSubtotal.setText("$" + String.format(Locale.US, "%.2f", reporte.getSubtotal()));
        lblValIva.setText("$" + String.format(Locale.US, "%.2f", reporte.getIva()));
        lblValTotal.setText("$" + String.format(Locale.US, "%.2f", reporte.getTotal()));
    }

    public void llenarTabla(ReporteVentas reporte) {
        modeloDemanda.setRowCount(0);
        for (String linea : reporte.getUnidadesPorProducto()) {
            String[] partes = linea.split(";");
            if (partes.length == 3) {
                modeloDemanda.addRow(new Object[]{partes[0], partes[1], partes[2]});
            }
        }
    }

    public void limpiarReporte() {
        lblValPedidos.setText("0");
        lblValSubtotal.setText("$0.00");
        lblValIva.setText("$0.00");
        lblValTotal.setText("$0.00");
        modeloDemanda.setRowCount(0);
    }
}