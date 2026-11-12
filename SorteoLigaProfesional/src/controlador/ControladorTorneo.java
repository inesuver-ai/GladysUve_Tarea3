package controlador;

import modelo.Torneo;
import modelo.excepciones.NumeroEquiposInvalidoException;
import modelo.excepciones.SorteoInvalidoException;
import vista.VistaConsola;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ControladorTorneo {

    private static final String ARCHIVO = "torneo.ser";

    private Torneo torneo;
    private final VistaConsola vista;

    public ControladorTorneo(VistaConsola vista) {
        this.vista = vista;
        this.torneo = Torneo.crearLigaPorDefecto();
    }

    public void iniciar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.leerOpcion();
            switch (opcion) {
                case 1: vista.mostrarEquipos(torneo.getEquipos()); break;
                case 2: realizarSorteo(torneo); break;
                case 3: vista.mostrarTorneo(torneo); break;
                case 4: guardarTorneo(); break;
                case 5: cargarTorneo(); break;
                case 6: demostrarExcepciones(); break;
                case 0: vista.mostrarMensaje("Hasta pronto."); break;
                default: vista.mostrarError("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private void realizarSorteo(Torneo t) {
        try {
            t.sortearTorneo();
            vista.mostrarTorneo(t);
        } catch (NumeroEquiposInvalidoException e) {
            vista.mostrarError("No se puede sortear: " + e.getMessage());
        } catch (SorteoInvalidoException e) {
            vista.mostrarError("El sorteo falló: " + e.getMessage());
        }
    }

    private void guardarTorneo() {
        try {
            torneo.guardar(ARCHIVO);
            vista.mostrarMensaje("Torneo guardado en " + ARCHIVO);
        } catch (IOException e) {
            vista.mostrarError("No se pudo guardar el torneo: " + e.getMessage());
        }
    }

    private void cargarTorneo() {
        try {
            torneo = Torneo.cargar(ARCHIVO);
            vista.mostrarMensaje("Torneo cargado desde " + ARCHIVO);
            vista.mostrarTorneo(torneo);
        } catch (FileNotFoundException e) {
            vista.mostrarError("No existe el archivo " + ARCHIVO + ". Guarde un torneo primero.");
        } catch (IOException e) {
            vista.mostrarError("Error de lectura del archivo: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            vista.mostrarError("El archivo contiene una clase desconocida: " + e.getMessage());
        }
    }

    private void demostrarExcepciones() {
        vista.mostrarMensaje("\nIntentando sortear un torneo con 12 equipos...");
        Torneo invalido = new Torneo("Torneo de prueba", Torneo.generarEquipos(12));
        realizarSorteo(invalido);
    }
}
