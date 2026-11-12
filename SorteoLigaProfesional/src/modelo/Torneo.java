package modelo;

import modelo.excepciones.NumeroEquiposInvalidoException;
import modelo.excepciones.SorteoInvalidoException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Torneo implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Random ALEATORIO = new Random();

    private final String nombre;
    private final List<Equipo> equipos;
    private final Map<String, List<Partido>> etapas = new LinkedHashMap<>();
    private Equipo campeon;

    public Torneo(String nombre, List<Equipo> equipos) {
        this.nombre = nombre;
        this.equipos = new ArrayList<>(equipos);
    }

    public static Torneo crearLigaPorDefecto() {
        String[] nombres = {
            "Barcelona SC", "Emelec", "LDU Quito", "Independiente del Valle",
            "Aucas", "El Nacional", "Deportivo Cuenca", "Universidad Católica",
            "Delfín SC", "Orense SC", "Macará", "Mushuc Runa",
            "Técnico Universitario", "Libertad FC", "Guayaquil City", "Cumbayá FC"
        };
        List<Equipo> lista = new ArrayList<>();
        for (String n : nombres) {
            lista.add(new Equipo(n));
        }
        return new Torneo("Liga Profesional", lista);
    }

    public static List<Equipo> generarEquipos(int cantidad) {
        List<Equipo> lista = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            lista.add(new Equipo("Equipo " + i));
        }
        return lista;
    }

    public void sortearTorneo() throws NumeroEquiposInvalidoException, SorteoInvalidoException {
        etapas.clear();
        campeon = null;

        List<Equipo> participantes = new ArrayList<>(equipos);
        validarNumeroEquipos(participantes.size());

        while (participantes.size() > 1) {
            List<Partido> partidos = sortearEtapa(participantes);
            etapas.put(nombreEtapa(participantes.size()), partidos);

            participantes = new ArrayList<>();
            for (Partido p : partidos) {
                Equipo ganador = ALEATORIO.nextBoolean() ? p.getLocal() : p.getVisitante();
                p.setGanador(ganador);
                participantes.add(ganador);
            }
        }
        campeon = participantes.get(0);
    }

    public List<Partido> sortearEtapa(List<Equipo> participantes)
            throws NumeroEquiposInvalidoException, SorteoInvalidoException {
        validarNumeroEquipos(participantes.size());

        List<Equipo> disponibles = new ArrayList<>(participantes); 
        List<Partido> partidos = new ArrayList<>();
        emparejar(disponibles, partidos);
        return partidos;
    }

    private void emparejar(List<Equipo> disponibles, List<Partido> partidos)
            throws SorteoInvalidoException {
        if (disponibles.isEmpty()) {
            return;
        }
        if (disponibles.size() < 2) {
            throw new SorteoInvalidoException(
                    "El equipo " + disponibles.get(0) + " se quedó sin oponente.");
        }

        Equipo local = disponibles.remove(ALEATORIO.nextInt(disponibles.size()));
        Equipo visitante = disponibles.remove(ALEATORIO.nextInt(disponibles.size()));

        if (local.equals(visitante)) {
            throw new SorteoInvalidoException(
                    "El equipo " + local + " no puede enfrentarse a sí mismo.");
        }

        Partido nuevo = new Partido(local, visitante);

        for (Partido existente : partidos) {
            if (existente.mismoEnfrentamiento(nuevo)) {
                throw new SorteoInvalidoException(
                        "El partido " + nuevo + " ya existe en esta etapa.");
            }
            if (existente.involucra(local) || existente.involucra(visitante)) {
                throw new SorteoInvalidoException(
                        "Un equipo del partido " + nuevo + " ya juega otro partido en esta etapa.");
            }
        }

        partidos.add(nuevo);
        emparejar(disponibles, partidos);
    }

    private void validarNumeroEquipos(int cantidad) throws NumeroEquiposInvalidoException {
        boolean potenciaDeDos = cantidad >= 2 && (cantidad & (cantidad - 1)) == 0;
        if (!potenciaDeDos) {
            throw new NumeroEquiposInvalidoException(
                    "Número de equipos inválido (" + cantidad
                    + "): debe ser una potencia de 2 (16, 8, 4, 2).");
        }
    }

    private String nombreEtapa(int cantidadEquipos) {
        switch (cantidadEquipos) {
            case 16: return "OCTAVOS DE FINAL";
            case 8:  return "CUARTOS DE FINAL";
            case 4:  return "SEMIFINALES";
            case 2:  return "FINAL";
            default: return "RONDA DE " + cantidadEquipos + " EQUIPOS";
        }
    }

    public void guardar(String ruta) throws IOException {
        try (ObjectOutputStream salida =
                     new ObjectOutputStream(new FileOutputStream(ruta))) {
            salida.writeObject(this);
        }
    }

    public static Torneo cargar(String ruta) throws IOException, ClassNotFoundException {
        try (ObjectInputStream entrada =
                     new ObjectInputStream(new FileInputStream(ruta))) {
            return (Torneo) entrada.readObject();
        }
    }

    public String getNombre() {
        return nombre;
    }

    public List<Equipo> getEquipos() {
        return Collections.unmodifiableList(equipos);
    }

    public Map<String, List<Partido>> getEtapas() {
        return Collections.unmodifiableMap(etapas);
    }

    public Equipo getCampeon() {
        return campeon;
    }

    public boolean tieneSorteo() {
        return !etapas.isEmpty();
    }
}
