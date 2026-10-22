
package fachada;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import modelo.Registro;
import persistencia.PersistenciaRegistros;

public class FachadaRegistros {

    private final PersistenciaRegistros persistencia;
    private List<Registro> registros;
    private boolean cargaCorrecta;

    public FachadaRegistros(PersistenciaRegistros persistencia) {
        this.persistencia = persistencia;
        this.registros = new ArrayList<>();
        this.cargaCorrecta = false;
    }

    public void cargarDatos() throws IOException {
        cargaCorrecta = false;
        registros = persistencia.leer();
        cargaCorrecta = true;
    }

    public List<Registro> listarRegistros() {
        return new ArrayList<>(registros);
    }

    public void registrar(Registro nuevo) {
        comprobarCargaCorrecta();

        if (buscarPorIdentificador(nuevo.getIdentificador()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un material con ese identificador.");
        }

        registros.add(nuevo);
    }

    public void modificar(int identificador, String nombre,
            String categoria, int cantidad, boolean activo,
            String observaciones) {

        comprobarCargaCorrecta();

        int posicion = obtenerPosicion(identificador);

        if (posicion == -1) {
            throw new IllegalArgumentException(
                    "No existe un material con ese identificador.");
        }

        Registro actualizado = new Registro(
                identificador, nombre, categoria,
                cantidad, activo, observaciones);

        registros.set(posicion, actualizado);
    }

    public void eliminar(int identificador) {
        comprobarCargaCorrecta();

        int posicion = obtenerPosicion(identificador);

        if (posicion == -1) {
            throw new IllegalArgumentException(
                    "No existe un material con ese identificador.");
        }

        registros.remove(posicion);
    }

    public Registro buscarPorIdentificador(int identificador) {
        for (Registro registro : registros) {
            if (registro.getIdentificador() == identificador) {
                return registro;
            }
        }

        return null;
    }

    public List<Registro> buscar(String criterio) {
        comprobarCargaCorrecta();

        List<Registro> resultados = new ArrayList<>();

        if (criterio == null || criterio.isBlank()) {
            return listarRegistros();
        }

        String texto = criterio.trim().toLowerCase();

        for (Registro registro : registros) {
            boolean coincideNombre =
                    registro.getNombre().toLowerCase().contains(texto);

            boolean coincideIdentificador =
                    String.valueOf(registro.getIdentificador()).equals(texto);

            if (coincideNombre || coincideIdentificador) {
                resultados.add(registro);
            }
        }

        return resultados;
    }

    public void guardarDatos() throws IOException {
        if (!cargaCorrecta) {
            throw new IOException(
                    "No se puede guardar sin haber cargado los datos correctamente.");
        }

        persistencia.escribir(registros);
    }

    private int obtenerPosicion(int identificador) {
        for (int i = 0; i < registros.size(); i++) {
            if (registros.get(i).getIdentificador() == identificador) {
                return i;
            }
        }

        return -1;
    }

    private void comprobarCargaCorrecta() {
        if (!cargaCorrecta) {
            throw new IllegalStateException(
                    "No se pueden modificar los materiales porque los datos "
                    + "no se cargaron correctamente.");
        }
    }
}
