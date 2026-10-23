
package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import modelo.Registro;

public class VentanaPrincipal extends JFrame {

    private final JTextField campoIdentificador = new JTextField(10);
    private final JTextField campoNombre = new JTextField(20);

    private final JComboBox<String> comboCategoria =
            new JComboBox<>(Registro.CATEGORIAS.toArray(new String[0]));
private final JList<String> listaCategorias = new JList<>(Registro.CATEGORIAS.toArray(new String[0]));
private final JSpinner campoCantidad = new JSpinner(new SpinnerNumberModel(0, 0, 999999, 1));
    private final JCheckBox casillaActivo = new JCheckBox("Activo", true);
    private final JTextArea campoObservaciones = new JTextArea(3, 20);

    private final JTextField campoBusqueda = new JTextField(20);

    private final JButton botonRegistrar = new JButton("Registrar");
    private final JButton botonModificar = new JButton("Modificar");
    private final JButton botonEliminar = new JButton("Eliminar");
    private final JButton botonBuscar = new JButton("Buscar");
    private final JButton botonLimpiar = new JButton("Limpiar");
    private final JButton botonGuardar = new JButton("Guardar");

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new String[] {
                "Identificador",
                "Nombre",
                "Categoría",
                "Cantidad",
                "Activo",
                "Observaciones"
            }, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tablaRegistros = new JTable(modeloTabla);
private final javax.swing.table.TableRowSorter<DefaultTableModel> filtroTabla = new javax.swing.table.TableRowSorter<>(modeloTabla);
    public VentanaPrincipal() {
        super("ChemLab - Gestión de materiales de laboratorio");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
tablaRegistros.setRowSorter(filtroTabla);
        listaCategorias.addListSelectionListener(evento -> {
    if (!evento.getValueIsAdjusting()) {
        String categoria = listaCategorias.getSelectedValue();
        filtroTabla.setRowFilter(categoria == null ? null :
            javax.swing.RowFilter.regexFilter("^" + java.util.regex.Pattern.quote(categoria) + "$", 2));
    }
});
        
JPanel formulario = new JPanel(new BorderLayout(15, 0));
formulario.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));

JPanel etiquetas = new JPanel(new GridLayout(6, 1, 5, 5));
etiquetas.setPreferredSize(new java.awt.Dimension(185, 0));

etiquetas.add(new JLabel("Identificador:"));
etiquetas.add(new JLabel("Nombre:"));
etiquetas.add(new JLabel("Categoría:"));
etiquetas.add(new JLabel("Cantidad disponible:"));
etiquetas.add(new JLabel("Estado:"));
etiquetas.add(new JLabel("Observaciones:"));

JPanel campos = new JPanel(new GridLayout(6, 1, 5, 5));
JPanel filaIdentificador = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
filaIdentificador.add(campoIdentificador);
campos.add(filaIdentificador);
JPanel filaNombre = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
filaNombre.add(campoNombre);
campos.add(filaNombre);
JPanel filaCategoria = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
filaCategoria.add(comboCategoria);
campos.add(filaCategoria);
JPanel filaCantidad = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
filaCantidad.add(campoCantidad);
campos.add(filaCantidad);
campos.add(casillaActivo);
campos.add(new JScrollPane(campoObservaciones));

formulario.add(etiquetas, BorderLayout.WEST);
formulario.add(campos, BorderLayout.CENTER);
        JPanel acciones = new JPanel(new FlowLayout());
        acciones.add(botonRegistrar);
        acciones.add(botonModificar);
        acciones.add(botonEliminar);
        acciones.add(botonLimpiar);
        acciones.add(botonGuardar);

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.add(formulario, BorderLayout.CENTER);
        panelSuperior.add(acciones, BorderLayout.SOUTH);

        JPanel busqueda = new JPanel(new FlowLayout());
        busqueda.add(new JLabel("Buscar por identificador o nombre:"));
        busqueda.add(campoBusqueda);
        busqueda.add(botonBuscar);
        listaCategorias.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
JPanel filtros = new JPanel(new BorderLayout(5, 5));
filtros.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 20, 10, 20));
filtros.setPreferredSize(new java.awt.Dimension(310, 0));
filtros.add(new JLabel("Filtrar por categoría:"), BorderLayout.NORTH);
filtros.add(new JScrollPane(listaCategorias), BorderLayout.CENTER);
JButton botonMostrarTodos = new JButton("Mostrar todos");
botonMostrarTodos.addActionListener(evento -> listaCategorias.clearSelection());
filtros.add(botonMostrarTodos, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);
        add(filtros, BorderLayout.WEST);
        add(new JScrollPane(tablaRegistros), BorderLayout.CENTER);
        add(busqueda, BorderLayout.SOUTH);

        setSize(1150, 750);
        setLocationRelativeTo(null);
    }

    public void mostrarRegistros(List<Registro> registros) {
        modeloTabla.setRowCount(0);

        for (Registro registro : registros) {
            modeloTabla.addRow(new Object[] {
                registro.getIdentificador(),
                registro.getNombre(),
                registro.getCategoria(),
                registro.getCantidad(),
                registro.isActivo() ? "Sí" : "No",
                registro.getObservaciones()
            });
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "ChemLab", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public JTextField getCampoIdentificador() {
        return campoIdentificador;
    }

    public JTextField getCampoNombre() {
        return campoNombre;
    }

    public JComboBox<String> getComboCategoria() {
        return comboCategoria;
    }

  public JSpinner getCampoCantidad() {
        return campoCantidad;
    }

    public JCheckBox getCasillaActivo() {
        return casillaActivo;
    }

    public JTextArea getCampoObservaciones() {
        return campoObservaciones;
    }

    public JTextField getCampoBusqueda() {
        return campoBusqueda;
    }

    public JTable getTablaRegistros() {
        return tablaRegistros;
    }

    public JButton getBotonRegistrar() {
        return botonRegistrar;
    }

    public JButton getBotonModificar() {
        return botonModificar;
    }

    public JButton getBotonEliminar() {
        return botonEliminar;
    }

    public JButton getBotonBuscar() {
        return botonBuscar;
    }

    public JButton getBotonLimpiar() {
        return botonLimpiar;
    }

    public JButton getBotonGuardar() {
        return botonGuardar;
    }
}
