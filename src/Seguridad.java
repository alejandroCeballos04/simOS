package simOS.seguridad;

import java.util.Collection;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Módulo de seguridad: autenticación (quién eres) y autorización (qué puedes hacer).
 * La "matriz de acceso" es el mapa PERMISOS: para cada rol, los módulos que puede usar.
 */
public class Seguridad {
    public static final int MAX_INTENTOS = 3;

    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final Map<Rol, Set<Integer>> permisos = new EnumMap<>(Rol.class);

    public Seguridad() {
        // Usuarios de prueba (al menos 3 y 2 roles)
        agregar(new Usuario("admin", "admin123", Rol.ADMINISTRADOR));
        agregar(new Usuario("ana",   "ana123",   Rol.USUARIO));
        agregar(new Usuario("luis",  "luis123",  Rol.USUARIO));

        // Matriz de acceso: rol -> módulos permitidos
        permisos.put(Rol.ADMINISTRADOR, Set.of(1, 2, 3, 4, 5, 6, 7));
        permisos.put(Rol.USUARIO,       Set.of(1, 2, 3, 4, 6, 7)); // sin módulo 5
    }

    private void agregar(Usuario u) { usuarios.put(u.getNombre(), u); }

    public Collection<Usuario> getUsuarios() { return usuarios.values(); }

    /** Devuelve el usuario si las credenciales son correctas; null en caso contrario. */
    public Usuario autenticar(String nombre, String clave) {
        Usuario u = usuarios.get(nombre);
        if (u == null) {
            Bitacora.registrar(nombre, "Inicio de sesión (usuario inexistente)", false);
            return null;
        }
        if (u.isBloqueado()) {
            Bitacora.registrar(nombre, "Inicio de sesión (cuenta bloqueada)", false);
            return null;
        }
        if (u.claveCorrecta(clave)) {
            u.reiniciarIntentos();
            Bitacora.registrar(nombre, "Inicio de sesión", true);
            return u;
        }
        u.registrarFallo(MAX_INTENTOS);
        Bitacora.registrar(nombre, "Inicio de sesión (clave incorrecta)", false);
        if (u.isBloqueado()) {
            Bitacora.registrar(nombre, "Cuenta bloqueada por 3 intentos fallidos", false);
        }
        return null;
    }

    public boolean estaBloqueado(String nombre) {
        Usuario u = usuarios.get(nombre);
        return u != null && u.isBloqueado();
    }

    /** Consulta la matriz de acceso y deja el resultado en la bitácora. */
    public boolean autorizar(Usuario u, int modulo, String nombreModulo) {
        boolean ok = permisos.get(u.getRol()).contains(modulo);
        Bitacora.registrar(u.getNombre(), "Acceso al módulo " + modulo + " (" + nombreModulo + ")", ok);
        return ok;
    }

    /** Lista de módulos permitidos para un rol (para mostrar en el menú). */
    public boolean puede(Rol rol, int modulo) { return permisos.get(rol).contains(modulo); }
}
