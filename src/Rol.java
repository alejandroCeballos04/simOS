package simos.seguridad;

/** Roles del sistema. Cada rol tiene permisos distintos (ver Seguridad.java). */
public enum Rol {
    ADMINISTRADOR("Administrador"),
    USUARIO("Usuario");

    private final String etiqueta;

    Rol(String etiqueta) { this.etiqueta = etiqueta; }

    public String getEtiqueta() { return etiqueta; }
}
