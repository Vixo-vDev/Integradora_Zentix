package interfaz.admin.dao;

import interfaz.admin.modelos.DetalleSolicitud;

import java.util.List;
import java.sql.SQLException;

public interface DetalleSolicitudDAO {
    /**
     * Inserta un nuevo detalle de solicitud en la base de datos.
     * @param detalleSolicitud El objeto DetalleSolicitud a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean insertarDetalleSolicitud(DetalleSolicitud detalleSolicitud) throws SQLException;

    /**
     * Actualiza un detalle de solicitud existente en la base de datos.
     * @param detalleSolicitud El objeto DetalleSolicitud con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean actualizarDetalleSolicitud(DetalleSolicitud detalleSolicitud) throws SQLException;

    /**
     * Elimina un detalle de solicitud de la base de datos por su ID.
     * @param idDetalle El ID del detalle de solicitud a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean eliminarDetalleSolicitud(int idDetalle) throws SQLException;

    /**
     * Obtiene un detalle de solicitud de la base de datos por su ID.
     * @param idDetalle El ID del detalle de solicitud a buscar.
     * @return El objeto DetalleSolicitud encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de SQL.
     */
    DetalleSolicitud obtenerDetalleSolicitudPorId(int idDetalle) throws SQLException;

    /**
     * Obtiene todos los detalles de solicitud de la base de datos.
     * @return Una lista de objetos DetalleSolicitud.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<DetalleSolicitud> obtenerTodosLosDetallesSolicitud() throws SQLException;

    /**
     * Obtiene todos los detalles de solicitud asociados a una solicitud específica.
     * @param idSolicitud El ID de la solicitud.
     * @return Una lista de objetos DetalleSolicitud.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<DetalleSolicitud> obtenerDetallesPorSolicitud(int idSolicitud) throws SQLException;
}