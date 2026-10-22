
package controlador;

import fachada.FachadaRegistros;
import java.io.IOException;
import javax.swing.JOptionPane;
import vista.VentanaPrincipal;
import modelo.Registro;

public class ControladorRegistros {

    private final VentanaPrincipal vista;
    private final FachadaRegistros fachada;

    public ControladorRegistros(
            VentanaPrincipal vista, FachadaRegistros fachada) {

        this.vista = vista;
        this.fachada = fachada;

        vista.getBotonRegistrar().addActionListener(evento -> registrar());
        vista.getBotonModificar().addActionListener(evento -> modificar());
        vista.getBotonEliminar().addActionListener(evento -> eliminar());
        vista.getBotonBuscar().addActionListener(evento -> buscar());
        vista.getBotonLimpiar().addActionListener(evento -> limpiar());
        vista.getBotonGuardar().addActionListener(evento -> guardar());
        
        vista.getTablaRegistros().getSelectionModel()
                .addListSelectionListener(evento -> {
                    if (!evento.getValueIsAdjusting()) {
                        cargarSeleccion();
                    }
                });
    }

    public void iniciar() {
        try {
            fachada.cargarDatos();
            vista.mostrarRegistros(fachada.listarRegistros());
        } catch (IOException | IllegalArgumentException e) {
            vista.mostrarError(
                    "No se pudieron cargar los datos: " + e.getMessage()
                    + "\nNo guardes cambios hasta resolver el problema.");
        }

        vista.setVisible(true);
    }

    private void registrar() {
        try {
            Registro nuevo = leerFormulario();
            fachada.registrar(nuevo);
            actualizarTabla();
            vista.mostrarMensaje("Material registrado correctamente.");
            limpiar();
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void modificar() {
        try {
            int identificador = leerIdentificador();

            fachada.modificar(
                    identificador,
                    vista.getCampoNombre().getText(),
                    (String) vista.getComboCategoria().getSelectedItem(),
                    leerCantidad(),
                    vista.getCasillaActivo().isSelected(),
                    vista.getCampoObservaciones().getText());

            actualizarTabla();
            vista.mostrarMensaje("Material modificado correctamente.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            int identificador = leerIdentificador();

            int confirmacion = JOptionPane.showConfirmDialog(
                    vista,
                    "¿Seguro que deseas eliminar este material?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            fachada.eliminar(identificador);
            actualizarTabla();
            limpiar();
            vista.mostrarMensaje("Material eliminado correctamente.");

        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void buscar() {
        try {
            vista.mostrarRegistros(
                    fachada.buscar(vista.getCampoBusqueda().getText()));
        } catch (IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void limpiar() {
        vista.getCampoIdentificador().setText("");
        vista.getCampoNombre().setText("");
        vista.getComboCategoria().setSelectedIndex(0);
        vista.getCampoCantidad().setText("");
        vista.getCasillaActivo().setSelected(true);
        vista.getCampoObservaciones().setText("");
        vista.getCampoBusqueda().setText("");
        vista.getTablaRegistros().clearSelection();

        try {
            vista.mostrarRegistros(fachada.listarRegistros());
        } catch (IllegalStateException e) {
            vista.mostrarError(e.getMessage());
        }
    }

    private void guardar() {
        try {
            fachada.guardarDatos();
            vista.mostrarMensaje("Datos guardados correctamente.");
        } catch (IOException | IllegalStateException e) {
            vista.mostrarError(
                    "No se pudieron guardar los datos: " + e.getMessage());
        }
    }

    private Registro leerFormulario() {
        return new Registro(
                leerIdentificador(),
                vista.getCampoNombre().getText(),
                (String) vista.getComboCategoria().getSelectedItem(),
                leerCantidad(),
                vista.getCasillaActivo().isSelected(),
                vista.getCampoObservaciones().getText());
    }

    private int leerIdentificador() {
        try {
            int identificador = Integer.parseInt(
                    vista.getCampoIdentificador().getText().trim());

            if (identificador <= 0) {
                throw new IllegalArgumentException(
                        "El identificador debe ser un número positivo.");
            }

            return identificador;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El identificador debe ser un número entero positivo.");
        }
    }

    private int leerCantidad() {
        try {
            int cantidad = Integer.parseInt(
                    vista.getCampoCantidad().getText().trim());

            if (cantidad < 0) {
                throw new IllegalArgumentException(
                        "La cantidad no puede ser negativa.");
            }

            return cantidad;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser un número entero igual o mayor que cero.");
        }
    }

    private void cargarSeleccion() {
        int fila = vista.getTablaRegistros().getSelectedRow();

        if (fila == -1) {
            return;
        }

        try {
            int identificador = Integer.parseInt(
                    vista.getTablaRegistros().getValueAt(fila, 0).toString());

            Registro registro = fachada.buscarPorIdentificador(identificador);

            if (registro == null) {
                return;
            }

            vista.getCampoIdentificador().setText(
                    String.valueOf(registro.getIdentificador()));
            vista.getCampoNombre().setText(registro.getNombre());
            vista.getComboCategoria().setSelectedItem(registro.getCategoria());
            vista.getCampoCantidad().setText(
                    String.valueOf(registro.getCantidad()));
            vista.getCasillaActivo().setSelected(registro.isActivo());
            vista.getCampoObservaciones().setText(registro.getObservaciones());

        } catch (NumberFormatException e) {
            vista.mostrarError("No se pudo identificar el material seleccionado.");
        }
    }

    private void actualizarTabla() {
        vista.mostrarRegistros(fachada.listarRegistros());
    }
}
