package interfaz.admin.dao;

import interfaz.admin.modelos.TipoEquipo;

import java.util.List;
import java.sql.SQLException;

public interface TipoEquipoDAO {
    /**
     * Inserta un nuevo tipo de equipo en la base de datos.
     * @param tipoEquipo El objeto TipoEquipo a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean insertarTipoEquipo(TipoEquipo tipoEquipo) throws SQLException;

    /**
     * Actualiza un tipo de equipo existente en la base de datos.
     * @param tipoEquipo El objeto TipoEquipo con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean actualizarTipoEquipo(TipoEquipo tipoEquipo) throws SQLException;

    /**
     * Elimina un tipo de equipo de la base de datos por su ID.
     * @param idTipoEquipo El ID del tipo de equipo a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean eliminarTipoEquipo(int idTipoEquipo) throws SQLException;

    /**
     * Obtiene un tipo de equipo de la base de datos por su ID.
     * @param idTipoEquipo El ID del tipo de equipo a buscar.
     * @return El objeto TipoEquipo encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de SQL.
     */
    TipoEquipo obtenerTipoEquipoPorId(int idTipoEquipo) throws SQLException;

    /**
     * Obtiene todos los tipos de equipo de la base de datos.
     * @return Una lista de objetos TipoEquipo.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<TipoEquipo> obtenerTodosLosTiposEquipo() throws SQLException;
}