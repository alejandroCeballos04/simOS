package simos.modulos;

import java.util.Scanner;

/** Marcador temporal: se reemplaza por el módulo real a medida que lo programemos. */
public class ModuloPendiente implements Modulo {
    private final String nombre;

    public ModuloPendiente(String nombre) { this.nombre = nombre; }

    @Override public String nombre() { return nombre; }

    @Override
    public void ejecutar(Scanner entrada) {
        System.out.println("\n[" + nombre + "] Módulo en construcción.");
    }
}
