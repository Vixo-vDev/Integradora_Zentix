package interfaz.admin.dao.impl;

import interfaz.admin.Config.ConexionBD;
import interfaz.admin.dao.NotificacionDAO;
import interfaz.admin.modelos.Notificacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificacionDAOImpl implements NotificacionDAO {

    private static final Logger LOGGER = Logger.getLogger(NotificacionDAOImpl.class.getName());

    @Override
    public boolean insertarNotificacion(Notificacion notificacion) throws SQLException {
        String sql = "INSERT INTO NOTIFICATION (ID_USUARIO, ID_SOLICITUD, MENSAJE, FECHA, ESTADO) VALUES (?, ?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, notificacion.getIdUsuario());
            ps.setInt(2, notificacion.getIdSolicitud());
            ps.setString(3, notificacion.getMensaje()); // CLOB
            ps.setDate(4, notificacion.getFecha());
            ps.setString(5, notificacion.getEstado());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar notificación para usuario ID: " + notificacion.getIdUsuario(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean actualizarNotificacion(Notificacion notificacion) throws SQLException {
        String sql = "UPDATE NOTIFICATION SET ID_USUARIO=?, ID_SOLICITUD=?, MENSAJE=?, FECHA=?, ESTADO=? WHERE ID_NOTIFICATION=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, notificacion.getIdUsuario());
            ps.setInt(2, notificacion.getIdSolicitud());
            ps.setString(3, notificacion.getMensaje());
            ps.setDate(4, notificacion.getFecha());
            ps.setString(5, notificacion.getEstado());
            ps.setInt(6, notificacion.getIdNotificacion());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar notificación con ID: " + notificacion.getIdNotificacion(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean eliminarNotificacion(int idNotificacion) throws SQLException {
        String sql = "DELETE FROM NOTIFICATION WHERE ID_NOTIFICATION=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idNotificacion);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar notificación con ID: " + idNotificacion, e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public Notificacion obtenerNotificacionPorId(int idNotificacion) throws SQLException {
        String sql = "SELECT ID_NOTIFICATION, ID_USUARIO, ID_SOLICITUD, MENSAJE, FECHA, ESTADO FROM NOTIFICATION WHERE ID_NOTIFICATION=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Notificacion notificacion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idNotificacion);
            rs = ps.executeQuery();

            if (rs.next()) {
                notificacion = mapearResultSetANotificacion(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener notificación por ID: " + idNotificacion, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return notificacion;
    }

    @Override
    public List<Notificacion> obtenerTodasLasNotificaciones() throws SQLException {
        String sql = "SELECT ID_NOTIFICATION, ID_USUARIO, ID_SOLICITUD, MENSAJE, FECHA, ESTADO FROM NOTIFICATION ORDER BY FECHA DESC";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notificacion> notificaciones = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                notificaciones.add(mapearResultSetANotificacion(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todas las notificaciones.", e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return notificaciones;
    }

    @Override
    public List<Notificacion> obtenerNotificacionesPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT ID_NOTIFICATION, ID_USUARIO, ID_SOLICITUD, MENSAJE, FECHA, ESTADO FROM NOTIFICATION WHERE ID_USUARIO=? ORDER BY FECHA DESC";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Notificacion> notificaciones = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            while (rs.next()) {
                notificaciones.add(mapearResultSetANotificacion(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener notificaciones por usuario ID: " + idUsuario, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return notificaciones;
    }

    private Notificacion mapearResultSetANotificacion(ResultSet rs) throws SQLException {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(rs.getInt("ID_NOTIFICATION"));
        notificacion.setIdUsuario(rs.getInt("ID_USUARIO"));
        notificacion.setIdSolicitud(rs.getInt("ID_SOLICITUD"));
        notificacion.setMensaje(rs.getString("MENSAJE"));
        notificacion.setFecha(rs.getDate("FECHA"));
        notificacion.setEstado(rs.getString("ESTADO"));
        return notificacion;
    }

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
    }
}