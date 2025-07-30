package interfaz.admin.modelos;

import java.sql.Date; // Usamos java.sql.Date para las fechas

public class Notificacion {
    private int idNotificacion;
    private int idUsuario; // Clave foránea
    private int idSolicitud; // Clave foránea
    private String mensaje; // CLOB en la BD, String en Java
    private Date fecha;
    private String estado;

    // Constructor vacío
    public Notificacion() {
    }

    // Constructor completo
    public Notificacion(int idNotificacion, int idUsuario, int idSolicitud, String mensaje, Date fecha, String estado) {
        this.idNotificacion = idNotificacion;
        this.idUsuario = idUsuario;
        this.idSolicitud = idSolicitud;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Constructor sin ID (para nuevas notificaciones)
    public Notificacion(int idUsuario, int idSolicitud, String mensaje, Date fecha, String estado) {
        this.idUsuario = idUsuario;
        this.idSolicitud = idSolicitud;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", idUsuario=" + idUsuario +
                ", idSolicitud=" + idSolicitud +
                ", mensaje='" + mensaje + '\'' +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                '}';
    }
}