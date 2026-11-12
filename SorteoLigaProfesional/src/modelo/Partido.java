package modelo;

import java.io.Serializable;

public class Partido implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Equipo local;
    private final Equipo visitante;
    private Equipo ganador; 

    public Partido(Equipo local, Equipo visitante) {
        this.local = local;
        this.visitante = visitante;
    }

    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public Equipo getGanador() {
        return ganador;
    }

    public void setGanador(Equipo ganador) {
        this.ganador = ganador;
    }

    public boolean involucra(Equipo equipo) {
        return local.equals(equipo) || visitante.equals(equipo);
    }

    public boolean mismoEnfrentamiento(Partido otro) {
        return (local.equals(otro.local) && visitante.equals(otro.visitante))
                || (local.equals(otro.visitante) && visitante.equals(otro.local));
    }

    public String toString() {
        return local + " vs " + visitante;
    }
}
