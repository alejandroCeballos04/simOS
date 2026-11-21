package simos.seguridad;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Bitácora de auditoría: archivo de texto con fecha, hora, usuario, acción y resultado.
 * Todos los módulos deben llamar a Bitacora.registrar(...) para dejar trazabilidad.
 * Es "synchronized" porque en el modo distribuido varios hilos podrían escribir a la vez.
 */
public final class Bitacora {
    private static final Path ARCHIVO = Paths.get("bitacora.txt");
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Bitacora() {}

    public static synchronized void registrar(String usuario, String accion, boolean permitido) {
        String linea = String.format("%s | %-10s | %-40s | %s",
                LocalDateTime.now().format(FORMATO), usuario, accion,
                permitido ? "PERMITIDO" : "DENEGADO");
        try {
            Files.write(ARCHIVO, (linea + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("[Aviso] No se pudo escribir en la bitácora: " + e.getMessage());
        }
    }

    public static synchronized List<String> leer() throws IOException {
        if (!Files.exists(ARCHIVO)) return List.of();
        return Files.readAllLines(ARCHIVO, StandardCharsets.UTF_8);
    }
}
