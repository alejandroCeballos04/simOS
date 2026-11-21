package simos.seguridad;

/** Un usuario del sistema. La contraseña nunca se guarda en texto plano, solo su hash. */
public class Usuario {
    private final String nombre;
    private final String hashClave;
    private final Rol rol;
    private int intentosFallidos = 0;
    private boolean bloqueado = false;

    public Usuario(String nombre, String clave, Rol rol) {
        this.nombre = nombre;
        this.hashClave = Hash.sha256(clave);
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public Rol getRol() { return rol; }
    public boolean isBloqueado() { return bloqueado; }
    public int getIntentosFallidos() { return intentosFallidos; }

    public boolean claveCorrecta(String clave) {
        return hashClave.equals(Hash.sha256(clave));
    }

    public void registrarFallo(int maximo) {
        intentosFallidos++;
        if (intentosFallidos >= maximo) bloqueado = true;
    }

    public void reiniciarIntentos() { intentosFallidos = 0; }
}
