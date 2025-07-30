package interfaz.admin.dao;

import interfaz.admin.modelos.Solicitud;

import java.sql.Date;
import java.util.List;
import java.sql.SQLException;

public interface SolicitudDAO {
    /**
     * Inserta una nueva solicitud en la base de datos.
     * @param solicitud El objeto Solicitud a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean insertarSolicitud(Solicitud solicitud) throws SQLException;

    /**
     * Actualiza una solicitud existente en la base de datos.
     * @param solicitud El objeto Solicitud con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean actualizarSolicitud(Solicitud solicitud) throws SQLException;

    /**
     * Elimina una solicitud de la base de datos por su ID.
     * @param idSolicitud El ID de la solicitud a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean eliminarSolicitud(int idSolicitud) throws SQLException;

    /**
     * Obtiene una solicitud de la base de datos por su ID.
     * @param idSolicitud El ID de la solicitud a buscar.
     * @return El objeto Solicitud encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de SQL.
     */
    Solicitud obtenerSolicitudPorId(int idSolicitud) throws SQLException;

    /**
     * Obtiene todas las solicitudes de la base de datos.
     * @return Una lista de objetos Solicitud.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Solicitud> obtenerTodasLasSolicitudes() throws SQLException;

    /**
     * Obtiene todas las solicitudes de un usuario específico.
     * @param idUsuario El ID del usuario cuyas solicitudes se desean obtener.
     * @return Una lista de objetos Solicitud.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Solicitud> obtenerSolicitudesPorUsuario(int idUsuario) throws SQLException;
    List<Solicitud> obtenerTodasLasSolicitudesFiltradas(String estado, Date fechaInicio, Date fechaFin) throws SQLException;
    List<Solicitud> obtenerSolicitudesPorProfesorValida(int idProfesor) throws SQLException;
    List<Solicitud> obtenerSolicitudesPorUsuarioSolicitante(int idUsuario) throws SQLException;
}