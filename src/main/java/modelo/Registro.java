
package modelo;

import java.util.List;

public class Registro {

    public static final List<String> CATEGORIAS = List.of(
            "Material volumétrico",
            "Material de vidrio",
            "Instrumentos de medición",
            "Equipos de laboratorio",
            "Material de calentamiento"
    );

    private int identificador;
    private String nombre;
    private String categoria;
    private int cantidad;
    private boolean activo;
    private String observaciones;

    public Registro(int identificador, String nombre, String categoria,
                    int cantidad, boolean activo, String observaciones) {

        if (identificador <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser un número positivo.");
        }

        validarNombre(nombre);
        validarCategoria(categoria);

        if (cantidad < 0) {
            throw new IllegalArgumentException(
                    "La cantidad no puede ser negativa.");
        }

        if (activo && cantidad == 0) {
            throw new IllegalArgumentException(
                    "Un elemento sin unidades disponibles no puede estar activo.");
        }

        this.identificador = identificador;
        this.nombre = nombre.trim();
        this.categoria = categoria.trim();
        this.cantidad = cantidad;
        this.activo = activo;
        this.observaciones = observaciones == null
                ? ""
                : observaciones.trim();
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio.");
        }
    }

    private void validarCategoria(String categoria) {
        if (categoria == null || !CATEGORIAS.contains(categoria.trim())) {
            throw new IllegalArgumentException(
                    "Debes seleccionar una categoría válida.");
        }
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setNombre(String nombre) {
        validarNombre(nombre);
        this.nombre = nombre.trim();
    }

    public void setCategoria(String categoria) {
        validarCategoria(categoria);
        this.categoria = categoria.trim();
    }

    public void setCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException(
                    "La cantidad no puede ser negativa.");
        }

        this.cantidad = cantidad;

        if (cantidad == 0) {
            this.activo = false;
        }
    }

    public void setActivo(boolean activo) {
        if (activo && cantidad == 0) {
            throw new IllegalArgumentException(
                    "No se puede activar un elemento sin unidades disponibles.");
        }

        this.activo = activo;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones == null
                ? ""
                : observaciones.trim();
    }
}
