package interfaz.admin.modelos;

import java.sql.Date;

public class Solicitud {
    private int idSolicitud;
    private int idUsuarioSolicitante; // <-- Asegúrate de que esta línea existe
    private int idEquipo;
    private Date fechaSolicitud;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private String observaciones;
    private Integer idProfesorValida; // Puede ser nulo
    private Date fechaValidacion; // Puede ser nulo
    private Integer idAdminEntrega; // Puede ser nulo
    private Date fechaEntrega; // Puede ser nulo
    private Integer idAdminRecibe; // Puede ser nulo
    private Date fechaDevolucion; // Puede ser nulo
    private int idUsuario;
    private Date fechaRecibo;
    private String razonUso;
    private String articulo;
    private int cantidad;
    private String tiempoUso;

    // Constructor vacío
    public Solicitud() {}

    // Constructor completo (ajusta según tus necesidades, solo un ejemplo)

    public Solicitud(int idSolicitud, int idUsuarioSolicitante,
                     int idEquipo, Date fechaSolicitud,
                     Date fechaInicio, Date fechaFin,
                     String estado, String observaciones,
                     Integer idProfesorValida, Date fechaValidacion,
                     Integer idAdminEntrega, Date fechaEntrega,
                     Integer idAdminRecibe, Date fechaDevolucion,
                     int idUsuario, Date fechaRecibo, String razonUso,
                     String articulo, int cantidad, String tiempoUso) {
        this.idSolicitud = idSolicitud;
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idEquipo = idEquipo;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.observaciones = observaciones;
        this.idProfesorValida = idProfesorValida;
        this.fechaValidacion = fechaValidacion;
        this.idAdminEntrega = idAdminEntrega;
        this.fechaEntrega = fechaEntrega;
        this.idAdminRecibe = idAdminRecibe;
        this.fechaDevolucion = fechaDevolucion;
        this.idUsuario = idUsuario;
        this.fechaRecibo = fechaRecibo;
        this.razonUso = razonUso;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.tiempoUso = tiempoUso;
    }


    // --- Getters y Setters ---

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    // Asegúrate de que estos métodos existan:
    public int getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(int idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }
    // --- Fin de los métodos que deben existir ---


    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getIdProfesorValida() {
        return idProfesorValida;
    }

    public void setIdProfesorValida(Integer idProfesorValida) {
        this.idProfesorValida = idProfesorValida;
    }

    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public Integer getIdAdminEntrega() {
        return idAdminEntrega;
    }

    public void setIdAdminEntrega(Integer idAdminEntrega) {
        this.idAdminEntrega = idAdminEntrega;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Integer getIdAdminRecibe() {
        return idAdminRecibe;
    }

    public void setIdAdminRecibe(Integer idAdminRecibe) {
        this.idAdminRecibe = idAdminRecibe;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaRecibo() {
        return fechaRecibo;
    }

    public void setFechaRecibo(Date fechaRecibo) {
        this.fechaRecibo = fechaRecibo;
    }

    public String getRazonUso() {
        return razonUso;
    }

    public void setRazonUso(String razonUso) {
        this.razonUso = razonUso;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTiempoUso() {
        return tiempoUso;
    }

    public void setTiempoUso(String tiempoUso) {
        this.tiempoUso = tiempoUso;
    }
}