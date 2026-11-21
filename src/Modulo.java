package simos.modulos;

import java.util.Scanner;

/** Contrato que cumple cada módulo de SimOS. Así el menú los trata a todos igual. */
public interface Modulo {
    String nombre();
    void ejecutar(Scanner entrada);
}
