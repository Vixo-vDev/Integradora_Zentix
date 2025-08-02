package impl;

import Dao.ISolicitudDao;
import config.ConnectionBD;
import com.example.netrixapp.Modelos.Solicitud;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SolicitudDaoImpl implements ISolicitudDao {


    @Override
    public List<Solicitud> findAll(int id_usuario){
        String sql="SELECT * FROM SOLICITUD WHERE id_usuario = ? ORDER BY ID_SOLICITUD ASC";
        List<Solicitud> solicitudes= new ArrayList<>();

        try {
            Connection con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs= ps.executeQuery();

            System.out.println("Ejecutando consulta para usuario ID: " + id_usuario);
            while(rs.next()){
                Solicitud solicitud =new Solicitud();

                solicitud.setId_solicitud(rs.getInt("ID_SOLICITUD"));
                solicitud.setFecha_solicitud(LocalDate.parse(rs.getDate("FECHA_SOLICITUD").toString()));
                solicitud.setArticulo(rs.getString("ARTICULO"));
                solicitud.setCantidad(rs.getInt("CANTIDAD"));
                solicitud.setEstado(rs.getString("ESTADO"));
                solicitudes.add(solicitud);

            }
            System.out.println("Total solicitudes encontradas: " + solicitudes.size());
            return solicitudes;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Solicitud> findAllAdmin(){
        String sql="SELECT S.ID_SOLICITUD, S.ID_USUARIO, U.NOMBRE, S.FECHA_SOLICITUD, S.ARTICULO, " +
                "S.CANTIDAD, S.FECHA_RECIBO, S.TIEMPO_USO, S.RAZON_USO, S.ESTADO\n" +
                "FROM SOLICITUD S INNER JOIN USUARIO U ON S.ID_USUARIO = U.ID_USUARIO ORDER BY ID_SOLICITUD ASC";
        List<Solicitud> solicitudes= new ArrayList<>();

        try {
            Connection con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                Solicitud solicitud =new Solicitud();

                solicitud.setId_solicitud(rs.getInt("ID_SOLICITUD"));
                solicitud.setId_usuario(rs.getInt("ID_USUARIO"));
                solicitud.setNombreUsuario(rs.getString("NOMBRE"));
                solicitud.setFecha_solicitud(LocalDate.parse(rs.getDate("FECHA_SOLICITUD").toString()));
                solicitud.setArticulo(rs.getString("ARTICULO"));
                solicitud.setCantidad(rs.getInt("CANTIDAD"));
                solicitud.setFecha_recibo(LocalDate.parse(rs.getDate("FECHA_RECIBO").toString()));
                solicitud.setTiempo_uso(rs.getString("TIEMPO_USO"));
                solicitud.setRazon(rs.getString("RAZON_USO"));
                solicitud.setEstado(rs.getString("ESTADO"));
                solicitudes.add(solicitud);

            }
            System.out.println("Total solicitudes encontradas: " + solicitudes.size());
            return solicitudes;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Solicitud> findByFilters(int id_usuario, String estado, LocalDate desde, LocalDate hasta) {
        List<Solicitud> solicitudes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM SOLICITUD WHERE id_usuario = ?");
        if (estado != null && !estado.equalsIgnoreCase("Todos")) {
            sql.append(" AND LOWER(estado) = LOWER(?)");
        }
        if (desde != null) {
            sql.append(" AND fecha_solicitud >= ?");
        }
        if (hasta != null) {
            sql.append(" AND fecha_solicitud <= ?");
        }
        sql.append(" ORDER BY id_solicitud ASC");

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int index = 1;
            ps.setInt(index++, id_usuario);
            if (estado != null && !estado.equalsIgnoreCase("Todos")) {
                ps.setString(index++, estado);
            }
            if (desde != null) {
                ps.setDate(index++, java.sql.Date.valueOf(desde));
            }
            if (hasta != null) {
                ps.setDate(index++, java.sql.Date.valueOf(hasta));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud();
                s.setId_solicitud(rs.getInt("id_solicitud"));
                s.setFecha_solicitud(rs.getDate("fecha_solicitud").toLocalDate());
                s.setArticulo(rs.getString("articulo"));
                s.setCantidad(rs.getInt("cantidad"));
                s.setEstado(rs.getString("estado"));
                solicitudes.add(s);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return solicitudes;
    }


    @Override
    public void create(Solicitud solicitud) throws Exception {
        String sql="INSERT INTO SOLICITUD (ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD," +
                "FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO) " +
                "VALUES (?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?)";
        try {
            Connection con= ConnectionBD.getConnection(); //Se establece la conexion
            PreparedStatement ps=con.prepareStatement(sql); // se prepara la consulta para evitar inyeccion sql

            ps.setInt(1,solicitud.getId_usuario());
            ps.setString(2,solicitud.getFecha_solicitud().toString());
            ps.setString(3,solicitud.getArticulo());
            ps.setInt(4,solicitud.getCantidad());
            ps.setString(5,solicitud.getFecha_recibo().toString());
            ps.setString(6,solicitud.getTiempo_uso());
            ps.setString(7, solicitud.getRazon());
            ps.setString(8, solicitud.getEstado());
            ps.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int totalSolicitudes(int id_usuario) {
        int totalSolicitudes=0;
        String sql = "SELECT COUNT (id_solicitud) FROM SOLICITUD WHERE id_usuario = ?";
        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                totalSolicitudes = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalSolicitudes;
    }

    @Override
    public int totalRechazados(int id_usuario) {
        int totalRechazados=0;
        String sql = " SELECT COUNT (id_solicitud) FROM SOLICITUD WHERE estado = 'rechazado' and  id_usuario = ?";

        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                totalRechazados = rs.getInt(1);
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return totalRechazados;
    }

    @Override
    public int total_pendientes(int id_usuario) {
        int total_pendiente=0;
        String sql = " SELECT COUNT (id_solicitud) FROM SOLICITUD WHERE estado = 'pendiente' and   id_usuario = ?";

        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                total_pendiente = rs.getInt(1);
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }
        return total_pendiente;
    }

    @Override
    public void actualizarEstado(int idSolicitud, String nuevoEstado) {
        String sql = "UPDATE SOLICITUD SET ESTADO = ? WHERE ID_SOLICITUD = ?";
        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Solicitud> findByEstado(String estado) {
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = "SELECT S.ID_SOLICITUD, S.ID_USUARIO, U.NOMBRE, S.FECHA_SOLICITUD, S.ARTICULO, " +
                "S.CANTIDAD, S.FECHA_RECIBO, S.TIEMPO_USO, S.RAZON_USO, S.ESTADO " +
                "FROM SOLICITUD S INNER JOIN USUARIO U ON S.ID_USUARIO = U.ID_USUARIO " +
                "WHERE S.ESTADO = ? ORDER BY ID_SOLICITUD ASC";

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);  // Asegúrate de pasar exactamente "Pendiente", "Aprobada", etc.
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setId_solicitud(rs.getInt("ID_SOLICITUD"));
                solicitud.setId_usuario(rs.getInt("ID_USUARIO"));
                solicitud.setNombreUsuario(rs.getString("NOMBRE"));
                solicitud.setFecha_solicitud(rs.getDate("FECHA_SOLICITUD").toLocalDate());
                solicitud.setArticulo(rs.getString("ARTICULO"));
                solicitud.setCantidad(rs.getInt("CANTIDAD"));
                solicitud.setFecha_recibo(rs.getDate("FECHA_RECIBO").toLocalDate());
                solicitud.setTiempo_uso(rs.getString("TIEMPO_USO"));
                solicitud.setRazon(rs.getString("RAZON_USO"));
                solicitud.setEstado(rs.getString("ESTADO"));
                solicitudes.add(solicitud);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Solicitudes encontradas: " + solicitudes.size());
        return solicitudes;
    }

    @Override
    public List<Object[]> findEquiposMasSolicitados(LocalDate inicio, LocalDate fin, int limite) throws Exception {
        List<Object[]> resultados = new ArrayList<>();
        String sql = """
        SELECT e.DESCRIPCION, COUNT(*) AS cantidad
        FROM SOLICITUD s
        INNER JOIN EQUIPO e ON s.ID_EQUIPO = e.ID_EQUIPO
        WHERE s.FECHA_SOLICITUD BETWEEN ? AND ?
        GROUP BY e.DESCRIPCION
        ORDER BY cantidad DESC
        FETCH FIRST ? ROWS ONLY
    """;

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));
            ps.setInt(3, limite);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("DESCRIPCION");
                fila[1] = rs.getInt("cantidad");
                resultados.add(fila);
            }
        }

        return resultados;
    }

    @Override
    public List<Object[]> findEquiposMenosSolicitados(LocalDate inicio, LocalDate fin, int limite) throws Exception {
        List<Object[]> resultados = new ArrayList<>();
        String sql = """
        SELECT e.DESCRIPCION, COUNT(*) AS cantidad
        FROM SOLICITUD s
        JOIN EQUIPO e ON s.ID_EQUIPO = e.ID_EQUIPO
        WHERE s.FECHA_SOLICITUD BETWEEN ? AND ?
        GROUP BY e.DESCRIPCION
        ORDER BY cantidad ASC
        FETCH FIRST ? ROWS ONLY
    """;

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));
            ps.setInt(3, limite);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("DESCRIPCION");
                fila[1] = rs.getInt("cantidad");
                resultados.add(fila);
            }
        }

        return resultados;
    }

}
