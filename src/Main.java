package simos;

import simos.modulos.Modulo;
import simos.modulos.ModuloPendiente;
import simos.modulos.ModuloSeguridad;
import simos.seguridad.Bitacora;
import simos.seguridad.Seguridad;
import simos.seguridad.Usuario;

import java.util.Scanner;

/** Punto de entrada de SimOS: inicio de sesión + menú principal. */
public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        Seguridad seguridad = new Seguridad();

        // Posición del arreglo = número de módulo (la posición 0 no se usa)
        Modulo[] modulos = new Modulo[8];
        modulos[1] = new ModuloPendiente("Planificación de CPU");
        modulos[2] = new ModuloPendiente("Concurrencia y sincronización");
        modulos[3] = new ModuloPendiente("Gestión de memoria");
        modulos[4] = new ModuloPendiente("Almacenamiento (planificación de disco)");
        modulos[5] = new ModuloSeguridad(seguridad);
        modulos[6] = new ModuloPendiente("Entrada/salida (cola de impresión)");
        modulos[7] = new ModuloPendiente("Modo distribuido (cliente / servidor)");

        Usuario sesion = iniciarSesion(entrada, seguridad);
        if (sesion == null) {
            System.out.println("Saliendo de SimOS.");
            return;
        }
        menuPrincipal(entrada, seguridad, modulos, sesion);
        Bitacora.registrar(sesion.getNombre(), "Cierre de sesión", true);
        System.out.println("Hasta pronto.");
    }

    private static Usuario iniciarSesion(Scanner entrada, Seguridad seguridad) {
        System.out.println("========== SimOS v1.0 ==========");
        System.out.println("Inicio de sesión (escriba 'salir' como usuario para terminar)");
        while (true) {
            System.out.print("Usuario: ");
            String nombre = entrada.nextLine().trim();
            if (nombre.equalsIgnoreCase("salir")) return null;
            System.out.print("Contraseña: ");
            String clave = entrada.nextLine();

            Usuario u = seguridad.autenticar(nombre, clave);
            if (u != null) return u;

            if (seguridad.estaBloqueado(nombre)) {
                System.out.println("Cuenta bloqueada por demasiados intentos fallidos.");
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
            }
        }
    }

    private static void menuPrincipal(Scanner entrada, Seguridad seguridad, Modulo[] modulos, Usuario sesion) {
        while (true) {
            System.out.println("\n========== SimOS v1.0 ==========");
            System.out.println("Usuario: " + sesion.getNombre() + "   Rol: " + sesion.getRol().getEtiqueta());
            System.out.println("--------------------------------");
            for (int i = 1; i <= 7; i++) {
                System.out.println(i + ". " + modulos[i].nombre());
            }
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion;
            try {
                opcion = Integer.parseInt(entrada.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número.");
                continue;
            }
            if (opcion == 0) return;
            if (opcion < 1 || opcion > 7) {
                System.out.println("Opción fuera de rango.");
                continue;
            }
            // Control de acceso: se consulta la matriz de permisos antes de entrar
            if (!seguridad.autorizar(sesion, opcion, modulos[opcion].nombre())) {
                System.out.println("ACCESO DENEGADO: su rol no tiene permiso para este módulo.");
                continue;
            }
            modulos[opcion].ejecutar(entrada);
        }
    }
}
