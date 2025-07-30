package interfaz.admin.dao;

import interfaz.admin.modelos.Equipo;

import java.util.List;
import java.sql.SQLException;

public interface EquipoDAO {
    /**
     * Inserta un nuevo equipo en la base de datos.
     * @param equipo El objeto Equipo a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean insertarEquipo(Equipo equipo) throws SQLException;

    /**
     * Actualiza un equipo existente en la base de datos.
     * @param equipo El objeto Equipo con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean actualizarEquipo(Equipo equipo) throws SQLException;

    /**
     * Elimina un equipo de la base de datos por su ID.
     * @param idEquipo El ID del equipo a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean eliminarEquipo(int idEquipo) throws SQLException;

    /**
     * Obtiene un equipo de la base de datos por su ID.
     * @param idEquipo El ID del equipo a buscar.
     * @return El objeto Equipo encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de SQL.
     */
    Equipo obtenerEquipoPorId(int idEquipo) throws SQLException;

    /**
     * Obtiene todos los equipos de la base de datos.
     * @return Una lista de objetos Equipo.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Equipo> obtenerTodosLosEquipos() throws SQLException;

    /**
     * Obtiene todos los equipos disponibles.
     * @return Una lista de objetos Equipo que están disponibles.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Equipo> obtenerEquiposDisponibles() throws SQLException;

    /**
     * Obtiene equipos por tipo de equipo.
     * @param idTipoEquipo El ID del tipo de equipo.
     * @return Una lista de objetos Equipo de ese tipo.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Equipo> obtenerEquiposPorTipo(int idTipoEquipo) throws SQLException;
}