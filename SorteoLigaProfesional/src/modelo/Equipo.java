package modelo;

import java.io.Serializable;
import java.util.Objects;

public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombre;

    public Equipo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo)) return false;
        return nombre.equalsIgnoreCase(((Equipo) o).nombre);
    }

    public int hashCode() {
        return Objects.hash(nombre.toLowerCase());
    }

    public String toString() {
        return nombre;
    }
}
