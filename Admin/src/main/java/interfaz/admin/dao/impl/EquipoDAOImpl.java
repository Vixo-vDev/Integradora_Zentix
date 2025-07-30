package interfaz.admin.dao.impl;

import interfaz.admin.Config.ConexionBD;
import interfaz.admin.dao.EquipoDAO;
import interfaz.admin.modelos.Equipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EquipoDAOImpl implements EquipoDAO {

    private static final Logger LOGGER = Logger.getLogger(EquipoDAOImpl.class.getName());

    @Override
    public boolean insertarEquipo(Equipo equipo) throws SQLException {
        String sql = "INSERT INTO EQUIPO (CODIGO_BIEN, DESCRIPCION, MARCA, MODELO, NUMERO_SERIE, DISPONIBLE, ID_TIPO_EQUIPO) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, equipo.getCodigoBien());
            ps.setString(2, equipo.getDescripcion());
            ps.setString(3, equipo.getMarca());
            ps.setString(4, equipo.getModelo());
            // NUMERO_SERIE puede ser nulo
            if (equipo.getNumeroSerie() != null && !equipo.getNumeroSerie().isEmpty()) {
                ps.setString(5, equipo.getNumeroSerie());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            ps.setString(6, String.valueOf(equipo.getDisponible()));
            ps.setInt(7, equipo.getIdTipoEquipo());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al insertar equipo con código: " + equipo.getCodigoBien(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean actualizarEquipo(Equipo equipo) throws SQLException {
        String sql = "UPDATE EQUIPO SET CODIGO_BIEN=?, DESCRIPCION=?, MARCA=?, MODELO=?, NUMERO_SERIE=?, DISPONIBLE=?, ID_TIPO_EQUIPO=? WHERE ID_EQUIPO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setString(1, equipo.getCodigoBien());
            ps.setString(2, equipo.getDescripcion());
            ps.setString(3, equipo.getMarca());
            ps.setString(4, equipo.getModelo());
            if (equipo.getNumeroSerie() != null && !equipo.getNumeroSerie().isEmpty()) {
                ps.setString(5, equipo.getNumeroSerie());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            ps.setString(6, String.valueOf(equipo.getDisponible()));
            ps.setInt(7, equipo.getIdTipoEquipo());
            ps.setInt(8, equipo.getIdEquipo());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar equipo con ID: " + equipo.getIdEquipo(), e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public boolean eliminarEquipo(int idEquipo) throws SQLException {
        String sql = "DELETE FROM EQUIPO WHERE ID_EQUIPO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idEquipo);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar equipo con ID: " + idEquipo, e);
            throw e;
        } finally {
            cerrarRecursos(ps, null);
        }
    }

    @Override
    public Equipo obtenerEquipoPorId(int idEquipo) throws SQLException {
        String sql = "SELECT ID_EQUIPO, CODIGO_BIEN, DESCRIPCION, MARCA, MODELO, NUMERO_SERIE, DISPONIBLE, ID_TIPO_EQUIPO FROM EQUIPO WHERE ID_EQUIPO=?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Equipo equipo = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idEquipo);
            rs = ps.executeQuery();

            if (rs.next()) {
                equipo = mapearResultSetAEquipo(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener equipo por ID: " + idEquipo, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return equipo;
    }

    @Override
    public List<Equipo> obtenerTodosLosEquipos() throws SQLException {
        String sql = "SELECT ID_EQUIPO, CODIGO_BIEN, DESCRIPCION, MARCA, MODELO, NUMERO_SERIE, DISPONIBLE, ID_TIPO_EQUIPO FROM EQUIPO ORDER BY ID_EQUIPO";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Equipo> equipos = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                equipos.add(mapearResultSetAEquipo(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener todos los equipos.", e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return equipos;
    }

    @Override
    public List<Equipo> obtenerEquiposDisponibles() throws SQLException {
        String sql = "SELECT ID_EQUIPO, CODIGO_BIEN, DESCRIPCION, MARCA, MODELO, NUMERO_SERIE, DISPONIBLE, ID_TIPO_EQUIPO FROM EQUIPO WHERE DISPONIBLE = '1' ORDER BY ID_EQUIPO";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Equipo> equipos = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                equipos.add(mapearResultSetAEquipo(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener equipos disponibles.", e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return equipos;
    }

    @Override
    public List<Equipo> obtenerEquiposPorTipo(int idTipoEquipo) throws SQLException {
        String sql = "SELECT ID_EQUIPO, CODIGO_BIEN, DESCRIPCION, MARCA, MODELO, NUMERO_SERIE, DISPONIBLE, ID_TIPO_EQUIPO FROM EQUIPO WHERE ID_TIPO_EQUIPO = ? ORDER BY ID_EQUIPO";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Equipo> equipos = new ArrayList<>();
        try {
            conexion = ConexionBD.obtenerConexion();
            ps = conexion.prepareStatement(sql);
            ps.setInt(1, idTipoEquipo);
            rs = ps.executeQuery();

            while (rs.next()) {
                equipos.add(mapearResultSetAEquipo(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener equipos por tipo de equipo ID: " + idTipoEquipo, e);
            throw e;
        } finally {
            cerrarRecursos(ps, rs);
        }
        return equipos;
    }

    private Equipo mapearResultSetAEquipo(ResultSet rs) throws SQLException {
        Equipo equipo = new Equipo();
        equipo.setIdEquipo(rs.getInt("ID_EQUIPO"));
        equipo.setCodigoBien(rs.getString("CODIGO_BIEN"));
        equipo.setDescripcion(rs.getString("DESCRIPCION"));
        equipo.setMarca(rs.getString("MARCA"));
        equipo.setModelo(rs.getString("MODELO"));
        String numeroSerie = rs.getString("NUMERO_SERIE");
        if (rs.wasNull()) {
            equipo.setNumeroSerie(null);
        } else {
            equipo.setNumeroSerie(numeroSerie);
        }
        equipo.setDisponible(rs.getString("DISPONIBLE").charAt(0));
        equipo.setIdTipoEquipo(rs.getInt("ID_TIPO_EQUIPO"));
        return equipo;
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