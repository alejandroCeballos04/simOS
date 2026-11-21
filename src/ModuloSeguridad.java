package simos.modulos;

import simos.seguridad.Bitacora;
import simos.seguridad.Seguridad;
import simos.seguridad.Usuario;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/** Módulo 5: ver usuarios con su rol y consultar la bitácora de auditoría. */
public class ModuloSeguridad implements Modulo {
    private final Seguridad seguridad;

    public ModuloSeguridad(Seguridad seguridad) { this.seguridad = seguridad; }

    @Override public String nombre() { return "Usuarios, permisos y bitácora"; }

    @Override
    public void ejecutar(Scanner entrada) {
        while (true) {
            System.out.println("\n--- Seguridad y protección ---");
            System.out.println("1. Ver usuarios y roles");
            System.out.println("2. Ver bitácora de auditoría");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            String op = entrada.nextLine().trim();
            switch (op) {
                case "1" -> {
                    System.out.printf("%-10s %-15s %s%n", "Usuario", "Rol", "Estado");
                    for (Usuario u : seguridad.getUsuarios()) {
                        System.out.printf("%-10s %-15s %s%n", u.getNombre(),
                                u.getRol().getEtiqueta(), u.isBloqueado() ? "BLOQUEADO" : "Activo");
                    }
                }
                case "2" -> {
                    try {
                        List<String> lineas = Bitacora.leer();
                        if (lineas.isEmpty()) System.out.println("La bitácora está vacía.");
                        lineas.forEach(System.out::println);
                    } catch (IOException e) {
                        System.out.println("No se pudo leer la bitácora: " + e.getMessage());
                    }
                }
                case "0" -> { return; }
                default -> System.out.println("Opción no válida.");
            }
        }
    }
}
