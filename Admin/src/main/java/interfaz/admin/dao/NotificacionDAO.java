package interfaz.admin.dao;

import interfaz.admin.modelos.Notificacion;

import java.util.List;
import java.sql.SQLException;

public interface NotificacionDAO {
    /**
     * Inserta una nueva notificación en la base de datos.
     * @param notificacion El objeto Notificacion a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean insertarNotificacion(Notificacion notificacion) throws SQLException;

    /**
     * Actualiza una notificación existente en la base de datos.
     * @param notificacion El objeto Notificacion con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean actualizarNotificacion(Notificacion notificacion) throws SQLException;

    /**
     * Elimina una notificación de la base de datos por su ID.
     * @param idNotificacion El ID de la notificación a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean eliminarNotificacion(int idNotificacion) throws SQLException;

    /**
     * Obtiene una notificación de la base de datos por su ID.
     * @param idNotificacion El ID de la notificación a buscar.
     * @return El objeto Notificacion encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de SQL.
     */
    Notificacion obtenerNotificacionPorId(int idNotificacion) throws SQLException;

    /**
     * Obtiene todas las notificaciones de la base de datos.
     * @return Una lista de objetos Notificacion.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Notificacion> obtenerTodasLasNotificaciones() throws SQLException;

    /**
     * Obtiene todas las notificaciones de un usuario específico.
     * @param idUsuario El ID del usuario cuyas notificaciones se desean obtener.
     * @return Una lista de objetos Notificacion.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Notificacion> obtenerNotificacionesPorUsuario(int idUsuario) throws SQLException;
}