package interfaz.admin.dao.impl;

import interfaz.admin.Config.ConexionBD;
import interfaz.admin.dao.UsuarioDAO;
import interfaz.admin.modelos.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAOImpl implements UsuarioDAO {

    private static final Logger LOGGER = Logger.getLogger(UsuarioDAOImpl.class.getName());

    @Override
    public boolean insertarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOMBRE, APELLIDOS, CORREO_INSTITUCIONAL, DOMICILIO, LADA, TELEFONO, FECHA_NACIMIENTO, EDAD, ROL, MATRICULA, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getCorreoInstitucional());
            ps.setString(4, usuario.getDomicilio());
            ps.setString(5, usuario.getLada());
            ps.setString(6, usuario.getTelefono());
            ps.setDate(7, usuario.getFechaNacimiento());
            // Para EDAD, si es nula en el objeto Usuario, se establece NULL en la BD
            if (usuario.getEdad() != null) {
                ps.setInt(8, usuario.getEdad());
            } else {
                ps.setNull(8, Types.NUMERIC);
            }
            ps.setString(9, usuario.getRol());
            // Para MATRICULA, si es nula, se establece NULL en la BD
            if (usuario.getMatricula() != null && !usuario.getMatricula().isEmpty()) {
                ps.setString(10, usuario.getMatricula());
            } else {
                ps.setNull(10, Types.VARCHAR);
            }
            ps.setString(11, usuario.getPassword());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar usuario: " + usuario.getCorreoInstitucional(), e);
            throw e; // Re-lanzar la excepción para que la capa superior la maneje
        } finally {
            // No cerramos la conexión aquí, ya que se gestiona de forma global en ConexionBD
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE USUARIO SET NOMBRE=?, APELLIDOS=?, CORREO_INSTITUCIONAL=?, DOMICILIO=?, LADA=?, TELEFONO=?, FECHA_NACIMIENTO=?, EDAD=?, ROL=?, MATRICULA=?, PASSWORD=? WHERE ID_USUARIO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getCorreoInstitucional());
            ps.setString(4, usuario.getDomicilio());
            ps.setString(5, usuario.getLada());
            ps.setString(6, usuario.getTelefono());
            ps.setDate(7, usuario.getFechaNacimiento());
            if (usuario.getEdad() != null) {
                ps.setInt(8, usuario.getEdad());
            } else {
                ps.setNull(8, Types.NUMERIC);
            }
            ps.setString(9, usuario.getRol());
            if (usuario.getMatricula() != null && !usuario.getMatricula().isEmpty()) {
                ps.setString(10, usuario.getMatricula());
            } else {
                ps.setNull(10, Types.VARCHAR);
            }
            ps.setString(11, usuario.getPassword());
            ps.setInt(12, usuario.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar usuario con ID: " + usuario.getIdUsuario(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean eliminarUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE ID_USUARIO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar usuario con ID: " + idUsuario, e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public Usuario obtenerUsuarioPorId(int idUsuario) throws SQLException {
        String sql = "SELECT ID_USUARIO, NOMBRE, APELLIDOS, CORREO_INSTITUCIONAL, DOMICILIO, LADA, TELEFONO, FECHA_NACIMIENTO, EDAD, ROL, MATRICULA, PASSWORD FROM USUARIO WHERE ID_USUARIO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuario por ID: " + idUsuario, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return usuario;
    }

    @Override
    public Usuario obtenerUsuarioPorCorreo(String correo) throws SQLException {
        String sql = "SELECT ID_USUARIO, NOMBRE, APELLIDOS, CORREO_INSTITUCIONAL, DOMICILIO, LADA, TELEFONO, FECHA_NACIMIENTO, EDAD, ROL, MATRICULA, PASSWORD FROM USUARIO WHERE CORREO_INSTITUCIONAL=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario usuario = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, correo);
            rs = ps.executeQuery();

            if (rs.next()) {
                usuario = mapearResultSetAUsuario(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuario por correo: " + correo, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return usuario;
    }

    @Override
    public List<Usuario> obtenerUsuariosPorRol(String rol) throws SQLException {
        String sql = "SELECT ID_USUARIO, NOMBRE, APELLIDOS, CORREO_ELECTRONICO, CONTRASENA, ROL FROM USUARIO WHERE ROL = ?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion(); // O ConexionBD.obtenerConexion() si así lo tienes
            ps = conexion.prepareStatement(sql);
            ps.setString(1, rol); // Establece el parámetro del rol
            rs = ps.executeQuery();

            while (rs.next()) {
                usuarios.add(mapearResultSetAUsuario(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener usuarios por rol: " + rol, e);
            throw e; // Relanza la excepción para que el controlador la maneje
        } finally {
            // Cierra el PreparedStatement y el ResultSet
            cerrarRecursos(ps, rs);
            // La conexión debe cerrarse si se obtuvo dentro del try.
            // Si tu ConexionDB.conectar() devuelve una nueva conexión cada vez,
            // entonces necesitas cerrar la conexión aquí también.
            try {
                if (conexion != null && !conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error al cerrar la conexión en obtenerUsuariosPorRol.", e);
            }
        }
        return usuarios;
    }


    @Override
    public List<Usuario> obtenerTodosLosUsuarios() throws SQLException {
        String sql = "SELECT ID_USUARIO, NOMBRE, APELLIDOS, CORREO_INSTITUCIONAL, DOMICILIO, LADA, TELEFONO, FECHA_NACIMIENTO, EDAD, ROL, MATRICULA, PASSWORD FROM USUARIO ORDER BY ID_USUARIO";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                usuarios.add(mapearResultSetAUsuario(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los usuarios.", e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return usuarios;
    }

    // Método auxiliar para mapear un ResultSet a un objeto Usuario
    private Usuario mapearResultSetAUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
        usuario.setNombre(rs.getString("NOMBRE"));
        usuario.setApellidos(rs.getString("APELLIDOS"));
        usuario.setCorreoInstitucional(rs.getString("CORREO_INSTITUCIONAL"));
        usuario.setDomicilio(rs.getString("DOMICILIO"));
        usuario.setLada(rs.getString("LADA"));
        usuario.setTelefono(rs.getString("TELEFONO"));
        usuario.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
        // Para campos nulos en la BD, se debe verificar y asignar null a Integer
        int edad = rs.getInt("EDAD");
        if (rs.wasNull()) {
            usuario.setEdad(null);
        } else {
            usuario.setEdad(edad);
        }
        usuario.setRol(rs.getString("ROL"));
        // Para MATRICULA, si es nulo
        String matricula = rs.getString("MATRICULA");
        if (rs.wasNull()) {
            usuario.setMatricula(null);
        } else {
            usuario.setMatricula(matricula);
        }
        usuario.setPassword(rs.getString("PASSWORD"));
        return usuario;
    }

    // Método auxiliar para cerrar recursos de JDBC
    private void cerrarRecursos(PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar ResultSet", e);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar PreparedStatement", e);
            }
        }
        // La conexión NO se cierra aquí, se gestiona en ConexionBD
    }
}