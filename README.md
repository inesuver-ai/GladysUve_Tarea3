# Sorteo de equipos para una liga profesional

Tarea Unidad 3 – Manejo de Excepciones y Archivos (Programación Orientada a Objetos, UPS).

Aplicación de consola en Java que sortea los partidos de una liga de 16 equipos
(octavos, cuartos, semifinales y final) con un algoritmo **recursivo**, usando
**MVC**, **excepciones propias** y **serialización** del torneo.

## Estructura (MVC)

```
src/
├── Main.java
├── modelo/                      Modelo: datos, sorteo recursivo y persistencia
│   ├── Equipo.java
│   ├── Partido.java
│   ├── Torneo.java              (Serializable; sortearTorneo, emparejar [recursivo], guardar, cargar)
│   └── excepciones/
│       ├── NumeroEquiposInvalidoException.java
│       └── SorteoInvalidoException.java
├── vista/
│   └── VistaConsola.java        Vista: solo entrada/salida, sin lógica de negocio
└── controlador/
    └── ControladorTorneo.java   Controlador: mediación y try-catch
```

## Cómo funciona

- `Torneo.emparejar(...)` es el método recursivo. Caso base: no quedan equipos.
  Paso recursivo: extrae dos equipos al azar, valida que no sean el mismo equipo,
  que no repitan partido ni jueguen dos veces en la etapa, registra el partido y
  se llama a sí mismo con los equipos restantes.
- `NumeroEquiposInvalidoException` se lanza si el número de equipos no es potencia de 2.
- `SorteoInvalidoException` se lanza desde el método recursivo ante un emparejamiento imposible.
- El ganador de cada partido se simula al azar para poder sortear la siguiente etapa.
- `Torneo` implementa `Serializable`; se guarda/carga con `ObjectOutputStream`/`ObjectInputStream`
  en `torneo.ser`, manejando `IOException` y `ClassNotFoundException`.

## Compilar y ejecutar

```bash
mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -Dfile.encoding=UTF-8 -cp out Main
```
