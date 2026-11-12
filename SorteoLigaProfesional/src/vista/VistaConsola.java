package vista;

import modelo.Equipo;
import modelo.Partido;
import modelo.Torneo;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class VistaConsola {

    private final Scanner scanner = new Scanner(System.in);

    public void mostrarMenu() {
        System.out.println();
        System.out.println(" SORTEO LIGA PROFESIONAL ");
        System.out.println("1. Ver equipos participantes");
        System.out.println("2. Realizar sorteo completo (octavos a final)");
        System.out.println("3. Ver último sorteo");
        System.out.println("4. Guardar torneo (torneo.ser)");
        System.out.println("5. Cargar torneo (torneo.ser)");
        System.out.println("6. Demostrar excepciones (torneo con 12 equipos)");
        System.out.println("0. Salir");
    }

    public int leerOpcion() {
        System.out.print("Seleccione una opción: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        } catch (NoSuchElementException e) {
            return 0; 
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("[ERROR] " + mensaje);
    }

    public void mostrarEquipos(List<Equipo> equipos) {
        System.out.println("\nEquipos (" + equipos.size() + "):");
        int i = 1;
        for (Equipo e : equipos) {
            System.out.printf("  %2d. %s%n", i++, e);
        }
    }

    public void mostrarTorneo(Torneo torneo) {
        if (!torneo.tieneSorteo()) {
            System.out.println("\nAún no se ha realizado ningún sorteo.");
            return;
        }
        System.out.println("\n##### " + torneo.getNombre().toUpperCase() + " #####");
        for (Map.Entry<String, List<Partido>> etapa : torneo.getEtapas().entrySet()) {
            System.out.println("\n--- " + etapa.getKey() + " ---");
            int n = 1;
            for (Partido p : etapa.getValue()) {
                System.out.printf("  Partido %d: %s  ->  avanza: %s%n", n++, p, p.getGanador());
            }
        }
        System.out.println("\n*** CAMPEÓN: " + torneo.getCampeon() + " ***");
    }
}
