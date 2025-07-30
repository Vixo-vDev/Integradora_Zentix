package interfaz.admin.dao.impl;

import interfaz.admin.Config.ConexionBD;
import interfaz.admin.dao.SolicitudDAO;
import interfaz.admin.modelos.Solicitud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SolicitudDAOImpl implements SolicitudDAO {

    private static final Logger LOGGER = Logger.getLogger(SolicitudDAOImpl.class.getName());

    @Override
    public boolean insertarSolicitud(Solicitud solicitud) throws SQLException {
        String sql = "INSERT INTO SOLICITUD (ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, solicitud.getIdUsuario());
            ps.setDate(2, solicitud.getFechaSolicitud());
            if (solicitud.getArticulo() != null && !solicitud.getArticulo().isEmpty()) {
                ps.setString(3, solicitud.getArticulo());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            ps.setInt(4, solicitud.getCantidad());
            ps.setDate(5, solicitud.getFechaRecibo());
            ps.setString(6, solicitud.getTiempoUso());
            ps.setString(7, solicitud.getRazonUso()); // CLOB se maneja como String
            ps.setString(8, solicitud.getEstado());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar solicitud para usuario ID: " + solicitud.getIdUsuario(), e);
            throw e;
        } finally {
            // Se corrige para pasar la conexión y cerrarla
            cerrarRecursos(conexion, ps, null);
        }
    }

    @Override
    public boolean actualizarSolicitud(Solicitud solicitud) throws SQLException {
        String sql = "UPDATE SOLICITUD SET ID_USUARIO=?, FECHA_SOLICITUD=?, ARTICULO=?, CANTIDAD=?, FECHA_RECIBO=?, TIEMPO_USO=?, RAZON_USO=?, ESTADO=?, ID_PROFESOR_VALIDA=?, FECHA_VALIDACION=?, ID_ADMIN_ENTREGA=?, FECHA_ENTREGA=?, ID_ADMIN_RECIBE=?, FECHA_DEVOLUCION=? WHERE ID_SOLICITUD=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, solicitud.getIdUsuario());
            ps.setDate(2, solicitud.getFechaSolicitud());
            if (solicitud.getArticulo() != null && !solicitud.getArticulo().isEmpty()) {
                ps.setString(3, solicitud.getArticulo());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            ps.setInt(4, solicitud.getCantidad());
            ps.setDate(5, solicitud.getFechaRecibo());
            ps.setString(6, solicitud.getTiempoUso());
            ps.setString(7, solicitud.getRazonUso());
            ps.setString(8, solicitud.getEstado());

            // Nuevos campos para actualización
            if (solicitud.getIdProfesorValida() != null) {
                ps.setInt(9, solicitud.getIdProfesorValida());
            } else {
                ps.setNull(9, Types.INTEGER);
            }
            ps.setDate(10, solicitud.getFechaValidacion());
            if (solicitud.getIdAdminEntrega() != null) {
                ps.setInt(11, solicitud.getIdAdminEntrega());
            } else {
                ps.setNull(11, Types.INTEGER);
            }
            ps.setDate(12, solicitud.getFechaEntrega());
            if (solicitud.getIdAdminRecibe() != null) {
                ps.setInt(13, solicitud.getIdAdminRecibe());
            } else {
                ps.setNull(13, Types.INTEGER);
            }
            ps.setDate(14, solicitud.getFechaDevolucion());

            ps.setInt(15, solicitud.getIdSolicitud());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar solicitud con ID: " + solicitud.getIdSolicitud(), e);
            throw e;
        } finally {
            // Se corrige para pasar la conexión y cerrarla
            cerrarRecursos(conexion, ps, null);
        }
    }

    @Override
    public boolean eliminarSolicitud(int idSolicitud) throws SQLException {
        String sql = "DELETE FROM SOLICITUD WHERE ID_SOLICITUD=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idSolicitud);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar solicitud con ID: " + idSolicitud, e);
            throw e;
        } finally {
            // Se corrige para pasar la conexión y cerrarla
            cerrarRecursos(conexion, ps, null);
        }
    }

    @Override
    public Solicitud obtenerSolicitudPorId(int idSolicitud) throws SQLException {
        // La consulta debe incluir todas las columnas que se mapean
        String sql = "SELECT ID_SOLICITUD, ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO, ID_PROFESOR_VALIDA, FECHA_VALIDACION, ID_ADMIN_ENTREGA, FECHA_ENTREGA, ID_ADMIN_RECIBE, FECHA_DEVOLUCION FROM SOLICITUD WHERE ID_SOLICITUD=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Solicitud solicitud = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idSolicitud);
            rs = ps.executeQuery();

            if (rs.next()) {
                solicitud = mapearResultSetASolicitud(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener solicitud por ID: " + idSolicitud, e);
            throw e;
        } finally {
            // Se corrige para pasar la conexión y cerrarla
            cerrarRecursos(conexion, ps, rs);
        }
        return solicitud;
    }

    @Override
    public List<Solicitud> obtenerTodasLasSolicitudes() throws SQLException {
        // La consulta debe incluir todas las columnas que se mapean
        String sql = "SELECT ID_SOLICITUD, ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO, ID_PROFESOR_VALIDA, FECHA_VALIDACION, ID_ADMIN_ENTREGA, FECHA_ENTREGA, ID_ADMIN_RECIBE, FECHA_DEVOLUCION FROM SOLICITUD ORDER BY FECHA_SOLICITUD DESC";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Solicitud> solicitudes = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                solicitudes.add(mapearResultSetASolicitud(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todas las solicitudes.", e);
            throw e;
        } finally {
            // Se corrige para pasar la conexión y cerrarla
            cerrarRecursos(conexion, ps, rs);
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> obtenerSolicitudesPorUsuario(int idUsuario) throws SQLException {
        // La consulta debe incluir todas las columnas que se mapean
        String sql = "SELECT ID_SOLICITUD, ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO, ID_PROFESOR_VALIDA, FECHA_VALIDACION, ID_ADMIN_ENTREGA, FECHA_ENTREGA, ID_ADMIN_RECIBE, FECHA_DEVOLUCION FROM SOLICITUD WHERE ID_USUARIO=? ORDER BY FECHA_SOLICITUD DESC";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Solicitud> solicitudes = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            while (rs.next()) {
                solicitudes.add(mapearResultSetASolicitud(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener solicitudes por usuario ID: " + idUsuario, e);
            throw e;
        } finally {
            // Se corrige para pasar la conexión y cerrarla
            cerrarRecursos(conexion, ps, rs);
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> obtenerTodasLasSolicitudesFiltradas(String estado, Date fechaInicio, Date fechaFin) throws SQLException {
        List<Solicitud> solicitudes = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT ID_SOLICITUD, ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO, ID_PROFESOR_VALIDA, FECHA_VALIDACION, ID_ADMIN_ENTREGA, FECHA_ENTREGA, ID_ADMIN_RECIBE, FECHA_DEVOLUCION FROM SOLICITUD WHERE 1=1");

        if (estado != null && !estado.isEmpty() && !"Todos".equalsIgnoreCase(estado)) {
            sqlBuilder.append(" AND ESTADO = ?");
        }
        if (fechaInicio != null) {
            sqlBuilder.append(" AND FECHA_SOLICITUD >= ?");
        }
        if (fechaFin != null) {
            sqlBuilder.append(" AND FECHA_SOLICITUD <= ?");
        }
        sqlBuilder.append(" ORDER BY FECHA_SOLICITUD DESC");

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sqlBuilder.toString());

            int paramIndex = 1;
            if (estado != null && !estado.isEmpty() && !"Todos".equalsIgnoreCase(estado)) {
                ps.setString(paramIndex++, estado);
            }
            if (fechaInicio != null) {
                ps.setDate(paramIndex++, fechaInicio);
            }
            if (fechaFin != null) {
                ps.setDate(paramIndex++, fechaFin);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                solicitudes.add(mapearResultSetASolicitud(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener solicitudes filtradas. Estado: " + estado + ", Fecha Inicio: " + fechaInicio + ", Fecha Fin: " + fechaFin, e);
            throw e;
        } finally {
            cerrarRecursos(conexion, ps, rs);
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> obtenerSolicitudesPorProfesorValida(int idProfesor) throws SQLException {
        List<Solicitud> solicitudes = new ArrayList<>();
        // Note: Make sure ID_PROFESOR_VALIDA exists in your SOLICITUD table
        String sql = "SELECT ID_SOLICITUD, ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO, ID_PROFESOR_VALIDA, FECHA_VALIDACION, ID_ADMIN_ENTREGA, FECHA_ENTREGA, ID_ADMIN_RECIBE, FECHA_DEVOLUCION FROM SOLICITUD WHERE ID_PROFESOR_VALIDA = ? ORDER BY FECHA_SOLICITUD DESC";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProfesor);
            rs = ps.executeQuery();

            while (rs.next()) {
                solicitudes.add(mapearResultSetASolicitud(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener solicitudes por profesor que valida. ID Profesor: " + idProfesor, e);
            throw e;
        } finally {
            cerrarRecursos(conexion, ps, rs);
        }
        return solicitudes;
    }

    @Override
    public List<Solicitud> obtenerSolicitudesPorUsuarioSolicitante(int idUsuario) throws SQLException {
        List<Solicitud> solicitudes = new ArrayList<>();
        // Note: Make sure ID_USUARIO is the column for the requesting user
        String sql = "SELECT ID_SOLICITUD, ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO, ID_PROFESOR_VALIDA, FECHA_VALIDACION, ID_ADMIN_ENTREGA, FECHA_ENTREGA, ID_ADMIN_RECIBE, FECHA_DEVOLUCION FROM SOLICITUD WHERE ID_USUARIO = ? ORDER BY FECHA_SOLICITUD DESC";

        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            while (rs.next()) {
                solicitudes.add(mapearResultSetASolicitud(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener solicitudes por usuario solicitante. ID Usuario: " + idUsuario, e);
            throw e;
        } finally {
            cerrarRecursos(conexion, ps, rs);
        }
        return solicitudes;
    }

    // --- Métodos Auxiliares ---

    private Solicitud mapearResultSetASolicitud(ResultSet rs) throws SQLException {
        Solicitud solicitud = new Solicitud();
        solicitud.setIdSolicitud(rs.getInt("ID_SOLICITUD"));
        solicitud.setIdUsuario(rs.getInt("ID_USUARIO"));
        solicitud.setFechaSolicitud(rs.getDate("FECHA_SOLICITUD"));

        String articulo = rs.getString("ARTICULO");
        if (rs.wasNull()) {
            solicitud.setArticulo(null);
        } else {
            solicitud.setArticulo(articulo);
        }

        solicitud.setCantidad(rs.getInt("CANTIDAD"));
        solicitud.setFechaRecibo(rs.getDate("FECHA_RECIBO"));
        solicitud.setTiempoUso(rs.getString("TIEMPO_USO"));
        solicitud.setRazonUso(rs.getString("RAZON_USO"));
        solicitud.setEstado(rs.getString("ESTADO"));

        // ¡Estos son los campos que necesitas mapear para el profesor/admin!
        // Asegúrate de que existan en tu modelo Solicitud y en tu base de datos.
        solicitud.setIdProfesorValida(rs.getObject("ID_PROFESOR_VALIDA", Integer.class));
        solicitud.setFechaValidacion(rs.getDate("FECHA_VALIDACION"));
        solicitud.setIdAdminEntrega(rs.getObject("ID_ADMIN_ENTREGA", Integer.class));
        solicitud.setFechaEntrega(rs.getDate("FECHA_ENTREGA"));
        solicitud.setIdAdminRecibe(rs.getObject("ID_ADMIN_RECIBE", Integer.class));
        solicitud.setFechaDevolucion(rs.getDate("FECHA_DEVOLUCION"));

        return solicitud;
    }

    // Método auxiliar para cerrar recursos, ahora también cierra la conexión
    private void cerrarRecursos(Connection conn, PreparedStatement ps, ResultSet rs) {
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
        // Es crucial cerrar la conexión al final del bloque finally
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error al cerrar Connection", e);
            }
        }
    }
}