package interfaz.admin.modelos;

public class DetalleSolicitud {
    private int idDetalle;
    private int idEquipo; // Clave foránea
    private int idTipoEquipo; // Clave foránea
    private int idSolicitud; // Clave foránea
    private int cantidad;

    // Constructor vacío
    public DetalleSolicitud() {
    }

    // Constructor completo
    public DetalleSolicitud(int idDetalle, int idEquipo, int idTipoEquipo, int idSolicitud, int cantidad) {
        this.idDetalle = idDetalle;
        this.idEquipo = idEquipo;
        this.idTipoEquipo = idTipoEquipo;
        this.idSolicitud = idSolicitud;
        this.cantidad = cantidad;
    }

    // Constructor sin ID
    public DetalleSolicitud(int idEquipo, int idTipoEquipo, int idSolicitud, int cantidad) {
        this.idEquipo = idEquipo;
        this.idTipoEquipo = idTipoEquipo;
        this.idSolicitud = idSolicitud;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getIdTipoEquipo() {
        return idTipoEquipo;
    }

    public void setIdTipoEquipo(int idTipoEquipo) {
        this.idTipoEquipo = idTipoEquipo;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetalleSolicitud{" +
                "idDetalle=" + idDetalle +
                ", idEquipo=" + idEquipo +
                ", idTipoEquipo=" + idTipoEquipo +
                ", idSolicitud=" + idSolicitud +
                ", cantidad=" + cantidad +
                '}';
    }
}