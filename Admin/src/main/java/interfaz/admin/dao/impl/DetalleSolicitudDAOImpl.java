package interfaz.admin.dao.impl;

import interfaz.admin.Config.ConexionBD;
import interfaz.admin.dao.DetalleSolicitudDAO;
import interfaz.admin.modelos.DetalleSolicitud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DetalleSolicitudDAOImpl implements DetalleSolicitudDAO {

    private static final Logger LOGGER = Logger.getLogger(DetalleSolicitudDAOImpl.class.getName());

    @Override
    public boolean insertarDetalleSolicitud(DetalleSolicitud detalleSolicitud) throws SQLException {
        String sql = "INSERT INTO DETALLE_SOLICITUD (ID_EQUIPO, ID_TIPO_EQUIPO, ID_SOLICITUD, CANTIDAD) VALUES (?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, detalleSolicitud.getIdEquipo());
            ps.setInt(2, detalleSolicitud.getIdTipoEquipo());
            ps.setInt(3, detalleSolicitud.getIdSolicitud());
            ps.setInt(4, detalleSolicitud.getCantidad());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar detalle de solicitud para Solicitud ID: " + detalleSolicitud.getIdSolicitud(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean actualizarDetalleSolicitud(DetalleSolicitud detalleSolicitud) throws SQLException {
        String sql = "UPDATE DETALLE_SOLICITUD SET ID_EQUIPO=?, ID_TIPO_EQUIPO=?, ID_SOLICITUD=?, CANTIDAD=? WHERE ID_DETALLE=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, detalleSolicitud.getIdEquipo());
            ps.setInt(2, detalleSolicitud.getIdTipoEquipo());
            ps.setInt(3, detalleSolicitud.getIdSolicitud());
            ps.setInt(4, detalleSolicitud.getCantidad());
            ps.setInt(5, detalleSolicitud.getIdDetalle());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar detalle de solicitud con ID: " + detalleSolicitud.getIdDetalle(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean eliminarDetalleSolicitud(int idDetalle) throws SQLException {
        String sql = "DELETE FROM DETALLE_SOLICITUD WHERE ID_DETALLE=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idDetalle);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar detalle de solicitud con ID: " + idDetalle, e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public DetalleSolicitud obtenerDetalleSolicitudPorId(int idDetalle) throws SQLException {
        String sql = "SELECT ID_DETALLE, ID_EQUIPO, ID_TIPO_EQUIPO, ID_SOLICITUD, CANTIDAD FROM DETALLE_SOLICITUD WHERE ID_DETALLE=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DetalleSolicitud detalleSolicitud = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idDetalle);
            rs = ps.executeQuery();

            if (rs.next()) {
                detalleSolicitud = mapearResultSetADetalleSolicitud(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener detalle de solicitud por ID: " + idDetalle, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return detalleSolicitud;
    }

    @Override
    public List<DetalleSolicitud> obtenerTodosLosDetallesSolicitud() throws SQLException {
        String sql = "SELECT ID_DETALLE, ID_EQUIPO, ID_TIPO_EQUIPO, ID_SOLICITUD, CANTIDAD FROM DETALLE_SOLICITUD ORDER BY ID_DETALLE";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DetalleSolicitud> detallesSolicitud = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                detallesSolicitud.add(mapearResultSetADetalleSolicitud(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los detalles de solicitud.", e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return detallesSolicitud;
    }

    @Override
    public List<DetalleSolicitud> obtenerDetallesPorSolicitud(int idSolicitud) throws SQLException {
        String sql = "SELECT ID_DETALLE, ID_EQUIPO, ID_TIPO_EQUIPO, ID_SOLICITUD, CANTIDAD FROM DETALLE_SOLICITUD WHERE ID_SOLICITUD=? ORDER BY ID_DETALLE";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DetalleSolicitud> detallesSolicitud = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idSolicitud);
            rs = ps.executeQuery();

            while (rs.next()) {
                detallesSolicitud.add(mapearResultSetADetalleSolicitud(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener detalles por solicitud ID: " + idSolicitud, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return detallesSolicitud;
    }

    private DetalleSolicitud mapearResultSetADetalleSolicitud(ResultSet rs) throws SQLException {
        DetalleSolicitud detalleSolicitud = new DetalleSolicitud();
        detalleSolicitud.setIdDetalle(rs.getInt("ID_DETALLE"));
        detalleSolicitud.setIdEquipo(rs.getInt("ID_EQUIPO"));
        detalleSolicitud.setIdTipoEquipo(rs.getInt("ID_TIPO_EQUIPO"));
        detalleSolicitud.setIdSolicitud(rs.getInt("ID_SOLICITUD"));
        detalleSolicitud.setCantidad(rs.getInt("CANTIDAD"));
        return detalleSolicitud;
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