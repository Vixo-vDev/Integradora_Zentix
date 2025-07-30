package interfaz.admin.dao;

import interfaz.admin.modelos.Usuario;

import java.util.List;
import java.sql.SQLException;

public interface UsuarioDAO {
    /**
     * Inserta un nuevo usuario en la base de datos.
     * @param usuario El objeto Usuario a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean insertarUsuario(Usuario usuario) throws SQLException;

    /**
     * Actualiza un usuario existente en la base de datos.
     * @param usuario El objeto Usuario con los datos actualizados.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean actualizarUsuario(Usuario usuario) throws SQLException;

    /**
     * Elimina un usuario de la base de datos por su ID.
     * @param idUsuario El ID del usuario a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    boolean eliminarUsuario(int idUsuario) throws SQLException;

    /**
     * Obtiene un usuario de la base de datos por su ID.
     * @param idUsuario El ID del usuario a buscar.
     * @return El objeto Usuario encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de SQL.
     */
    Usuario obtenerUsuarioPorId(int idUsuario) throws SQLException;

    /**
     * Obtiene un usuario de la base de datos por su correo institucional.
     * @param correo El correo institucional del usuario a buscar.
     * @return El objeto Usuario encontrado, o null si no existe.
     * @throws SQLException Si ocurre un error de SQL.
     */
    Usuario obtenerUsuarioPorCorreo(String correo) throws SQLException;

    /**
     * Obtiene todos los usuarios de la base de datos.
     * @return Una lista de objetos Usuario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    List<Usuario> obtenerTodosLosUsuarios() throws SQLException;

    List<Usuario> obtenerUsuariosPorRol(String rol) throws SQLException;
}