package interfaz.admin.dao.impl;

import interfaz.admin.Config.ConexionBD;
import interfaz.admin.dao.TipoEquipoDAO;
import interfaz.admin.modelos.TipoEquipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TipoEquipoDAOImpl implements TipoEquipoDAO {

    private static final Logger LOGGER = Logger.getLogger(TipoEquipoDAOImpl.class.getName());

    @Override
    public boolean insertarTipoEquipo(TipoEquipo tipoEquipo) throws SQLException {
        String sql = "INSERT INTO TIPOEQUIPO (NOMBRE) VALUES (?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, tipoEquipo.getNombre());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar tipo de equipo: " + tipoEquipo.getNombre(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean actualizarTipoEquipo(TipoEquipo tipoEquipo) throws SQLException {
        String sql = "UPDATE TIPOEQUIPO SET NOMBRE=? WHERE ID_TIPO_EQUIPO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, tipoEquipo.getNombre());
            ps.setInt(2, tipoEquipo.getIdTipoEquipo());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar tipo de equipo con ID: " + tipoEquipo.getIdTipoEquipo(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean eliminarTipoEquipo(int idTipoEquipo) throws SQLException {
        String sql = "DELETE FROM TIPOEQUIPO WHERE ID_TIPO_EQUIPO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTipoEquipo);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar tipo de equipo con ID: " + idTipoEquipo, e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public TipoEquipo obtenerTipoEquipoPorId(int idTipoEquipo) throws SQLException {
        String sql = "SELECT ID_TIPO_EQUIPO, NOMBRE FROM TIPOEQUIPO WHERE ID_TIPO_EQUIPO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        TipoEquipo tipoEquipo = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTipoEquipo);
            rs = ps.executeQuery();

            if (rs.next()) {
                tipoEquipo = mapearResultSetATipoEquipo(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener tipo de equipo por ID: " + idTipoEquipo, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return tipoEquipo;
    }

    @Override
    public List<TipoEquipo> obtenerTodosLosTiposEquipo() throws SQLException {
        String sql = "SELECT ID_TIPO_EQUIPO, NOMBRE FROM TIPOEQUIPO ORDER BY ID_TIPO_EQUIPO";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TipoEquipo> tiposEquipo = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                tiposEquipo.add(mapearResultSetATipoEquipo(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los tipos de equipo.", e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return tiposEquipo;
    }

    private TipoEquipo mapearResultSetATipoEquipo(ResultSet rs) throws SQLException {
        TipoEquipo tipoEquipo = new TipoEquipo();
        tipoEquipo.setIdTipoEquipo(rs.getInt("ID_TIPO_EQUIPO"));
        tipoEquipo.setNombre(rs.getString("NOMBRE"));
        return tipoEquipo;
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