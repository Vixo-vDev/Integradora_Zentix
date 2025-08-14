package impl;

import Dao.ISolicitudDao;
import com.example.netrixapp.Controladores.ControladorAdmin.ControladorEstadisticas;
import config.ConnectionBD;
import com.example.netrixapp.Modelos.Solicitud;

import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SolicitudDaoImpl implements ISolicitudDao {


    @Override
    public List<Solicitud> findAll(int id_usuario) throws Exception {
        Connection con = null;
        String sql="SELECT * FROM SOLICITUD WHERE id_usuario = ? ORDER BY ID_SOLICITUD DESC";
        List<Solicitud> solicitudes= new ArrayList<>();

        try {
            con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs= ps.executeQuery();

            System.out.println("Ejecutando consulta para usuario ID: " + id_usuario);
            while(rs.next()){
                Solicitud solicitud =new Solicitud();

                solicitud.setId_solicitud(rs.getInt("ID_SOLICITUD"));
                solicitud.setId_usuario(rs.getInt("ID_USUARIO"));
                solicitud.setFecha_solicitud(LocalDate.parse(rs.getDate("FECHA_SOLICITUD").toString()));
                solicitud.setArticulo(rs.getString("ARTICULO"));
                solicitud.setCantidad(rs.getInt("CANTIDAD"));
                
                // Obtener fecha de recibo (puede ser null)
                java.sql.Date fechaRecibo = rs.getDate("FECHA_RECIBO");
                if (fechaRecibo != null) {
                    solicitud.setFecha_recibo(LocalDate.parse(fechaRecibo.toString()));
                }
                
                // Obtener tiempo de uso (puede ser null)
                String tiempoUso = rs.getString("TIEMPO_USO");
                if (tiempoUso != null) {
                    solicitud.setTiempo_uso(tiempoUso);
                }
                
                // Obtener razón de uso (puede ser null)
                String razon = rs.getString("RAZON_USO");
                if (razon != null) {
                    solicitud.setRazon(razon);
                }
                
                solicitud.setEstado(rs.getString("ESTADO"));
                solicitudes.add(solicitud);

            }
            System.out.println("Total solicitudes encontradas: " + solicitudes.size());
            return solicitudes;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            if (con != null) {
                con.close();
            }
        }
    }

    @Override
    public List<Solicitud> findAllAdmin() throws Exception {
        Connection con = null;
        String sql="SELECT S.ID_SOLICITUD, S.ID_USUARIO, U.NOMBRE, S.FECHA_SOLICITUD, S.ARTICULO, " +
                "S.CANTIDAD, S.FECHA_RECIBO, S.TIEMPO_USO, S.RAZON_USO, S.ESTADO\n" +
                "FROM SOLICITUD S INNER JOIN USUARIO U ON S.ID_USUARIO = U.ID_USUARIO ORDER BY ID_SOLICITUD ASC";
        List<Solicitud> solicitudes= new ArrayList<>();

        try {
            con= ConnectionBD.getConnection();
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
        }finally{
            if (con != null) {
                con.close();
            }
        }

    }

    @Override
    public List<Solicitud> findUso() throws Exception{
        Connection con = null;
        String sql="SELECT S.ID_SOLICITUD, S.ID_USUARIO, U.NOMBRE, S.FECHA_SOLICITUD, S.ARTICULO, \n" +
                "S.CANTIDAD, S.FECHA_RECIBO, S.TIEMPO_USO, S.ESTADO, E.EN_USO\n" +
                "FROM SOLICITUD S \n" +
                "INNER JOIN USUARIO U ON S.ID_USUARIO = U.ID_USUARIO \n" +
                "INNER JOIN DETALLESOLICITUD D ON S.ID_SOLICITUD = D.ID_SOLICITUD\n" +
                "INNER JOIN EQUIPO E ON E.ID_EQUIPO = D.ID_EQUIPO\n" +
                "WHERE EN_USO = 1\n" +
                "ORDER BY ID_SOLICITUD ASC";
        List<Solicitud> solicitudes= new ArrayList<>();

        try {
            con= ConnectionBD.getConnection();
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
                solicitud.setEstado(rs.getString("ESTADO"));
                solicitud.setTiempo_uso(rs.getString("TIEMPO_USO"));
                solicitud.setEn_uso(rs.getInt("EN_USO"));
                solicitudes.add(solicitud);

            }
            System.out.println("Total solicitudes encontradas: " + solicitudes.size());
            return solicitudes;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            if (con != null) {
                con.close();
            }
        }
    }

    @Override
    public List<Solicitud> findByFilters(int id_usuario, String estado, LocalDate desde, LocalDate hasta) throws Exception {
        Connection con = null;
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

        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString());
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
                s.setId_usuario(rs.getInt("id_usuario"));
                s.setFecha_solicitud(rs.getDate("fecha_solicitud").toLocalDate());
                s.setArticulo(rs.getString("articulo"));
                s.setCantidad(rs.getInt("cantidad"));
                
                // Obtener fecha de recibo (puede ser null)
                java.sql.Date fechaRecibo = rs.getDate("fecha_recibo");
                if (fechaRecibo != null) {
                    s.setFecha_recibo(fechaRecibo.toLocalDate());
                }
                
                // Obtener tiempo de uso (puede ser null)
                String tiempoUso = rs.getString("tiempo_uso");
                if (tiempoUso != null) {
                    s.setTiempo_uso(tiempoUso);
                }
                
                // Obtener razón de uso (puede ser null)
                String razon = rs.getString("razon_uso");
                if (razon != null) {
                    s.setRazon(razon);
                }
                
                s.setEstado(rs.getString("estado"));
                solicitudes.add(s);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally{
            if (con != null) {
                con.close();
            }
        }
        return solicitudes;
    }


    @Override
    public int create(Solicitud solicitud) throws Exception {
        Connection con = null;
        String sql = "BEGIN INSERT INTO SOLICITUD (ID_USUARIO, FECHA_SOLICITUD, ARTICULO, CANTIDAD, FECHA_RECIBO, TIEMPO_USO, RAZON_USO, ESTADO) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING ID_SOLICITUD INTO ?; END;";

        try {
            con = ConnectionBD.getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setInt(1, solicitud.getId_usuario());
            cs.setDate(2, Date.valueOf(solicitud.getFecha_solicitud()));
            cs.setString(3, solicitud.getArticulo());
            cs.setInt(4, solicitud.getCantidad());
            cs.setDate(5, Date.valueOf(solicitud.getFecha_recibo()));
            cs.setString(6, solicitud.getTiempo_uso());
            cs.setString(7, solicitud.getRazon());
            cs.setString(8, solicitud.getEstado());

            // Parámetro de salida
            cs.registerOutParameter(9, java.sql.Types.INTEGER);

            // Ejecutar
            cs.execute();

            return cs.getInt(9); // Este SÍ es el ID real generado por Oracle
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            if (con != null) {
                con.close();
            }
        }
    }






    @Override
    public int totalSolicitudes(int id_usuario) throws Exception {
        Connection con = null;
        int totalSolicitudes=0;
        String sql = "SELECT COUNT (id_solicitud) FROM SOLICITUD WHERE id_usuario = ?";
        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                totalSolicitudes = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally{
            if (con != null) {
                con.close();
            }
        }
        return totalSolicitudes;
    }

    @Override
    public int totalRechazados(int id_usuario) throws Exception {
        Connection con = null;
        int totalRechazados=0;
        String sql = " SELECT COUNT (id_solicitud) FROM SOLICITUD WHERE estado = 'rechazado' and  id_usuario = ?";

        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                totalRechazados = rs.getInt(1);
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if (con != null) {
                con.close();
            }
        }
        return totalRechazados;
    }

    @Override
    public int total_pendientes(int id_usuario) throws Exception{
        Connection con = null;
        int total_pendiente=0;
        String sql = " SELECT COUNT (id_solicitud) FROM SOLICITUD WHERE estado = 'pendiente' and   id_usuario = ?";

        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1,id_usuario);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                total_pendiente = rs.getInt(1);
            }

        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if (con != null) {
                con.close();
            }
        }
        return total_pendiente;
    }

    @Override
    public void actualizarEstado(int idSolicitud, String nuevoEstado) throws Exception{
        Connection con = null;
        String sql = "UPDATE SOLICITUD SET ESTADO = ? WHERE ID_SOLICITUD = ?";
        try {
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            if (con != null) {
                con.close();
            }
        }
    }

    @Override
    public List<Solicitud> findByEstado(String estado) throws Exception {
        Connection con = null;
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = "SELECT S.ID_SOLICITUD, S.ID_USUARIO, U.NOMBRE, S.FECHA_SOLICITUD, S.ARTICULO, " +
                "S.CANTIDAD, S.FECHA_RECIBO, S.TIEMPO_USO, S.RAZON_USO, S.ESTADO " +
                "FROM SOLICITUD S INNER JOIN USUARIO U ON S.ID_USUARIO = U.ID_USUARIO " +
                "WHERE S.ESTADO = ? ORDER BY ID_SOLICITUD ASC";

        try {
            con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
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
        finally{
            if (con != null) {
                con.close();
            }
        }

        System.out.println("Solicitudes encontradas: " + solicitudes.size());
        return solicitudes;
    }

    @Override
    public List<Object[]> findEquiposMasSolicitadosSinFiltro(int limite) throws Exception {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT e.DESCRIPCION, COUNT(*) AS cantidad\n" +
                "FROM DETALLESOLICITUD d\n" +
                "INNER JOIN EQUIPO e ON d.ID_EQUIPO = e.ID_EQUIPO\n" +
                "GROUP BY e.DESCRIPCION\n" +
                "ORDER BY cantidad DESC\n" +
                "FETCH FIRST " + limite + " ROWS ONLY";

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("DESCRIPCION");
                fila[1] = rs.getInt("cantidad");
                resultados.add(fila);
            }
            System.out.println("Equipos más solicitados sin filtro: " + resultados.size());
        }

        return resultados;
    }


    @Override
    public List<Object[]> findEquiposMasSolicitados(Date inicio, Date fin, int limite) throws Exception {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT e.DESCRIPCION, COUNT(*) AS cantidad\n" +
                "FROM DETALLESOLICITUD d\n" +
                "INNER JOIN EQUIPO e ON d.ID_EQUIPO = e.ID_EQUIPO\n" +
                "INNER JOIN SOLICITUD s ON d.ID_SOLICITUD = s.ID_SOLICITUD\n" +
                "WHERE s.FECHA_SOLICITUD BETWEEN ? AND ?\n" +
                "GROUP BY e.DESCRIPCION\n" +
                "ORDER BY cantidad DESC\n" +
                "FETCH FIRST " + limite + " ROWS ONLY";

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            // Pasa directamente las fechas sin conversiones adicionales
            ps.setDate(1, inicio);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("DESCRIPCION");
                fila[1] = rs.getInt("cantidad");
                resultados.add(fila);
            }
            System.out.println("Equipos más solicitados encontrados: " + resultados.size());
        }

        return resultados;
    }

    @Override
    public List<Object[]> findEquiposMenosSolicitados(Date inicio, Date fin, int limite) throws Exception {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT e.DESCRIPCION, COUNT(*) AS cantidad\n" +
                "FROM DETALLESOLICITUD d\n" +
                "INNER JOIN EQUIPO e ON d.ID_EQUIPO = e.ID_EQUIPO\n" +
                "INNER JOIN SOLICITUD s ON d.ID_SOLICITUD = s.ID_SOLICITUD\n" +
                "WHERE s.FECHA_SOLICITUD BETWEEN ? AND ?\n" +
                "GROUP BY e.DESCRIPCION\n" +
                "ORDER BY cantidad ASC\n" +
                "FETCH FIRST " + limite + " ROWS ONLY";

        try (Connection con = ConnectionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            // Pasa directamente las fechas sin conversiones adicionales
            ps.setDate(1, inicio);
            ps.setDate(2, fin);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("DESCRIPCION");
                fila[1] = rs.getInt("cantidad");
                resultados.add(fila);
            }
            System.out.println("Equipos menos solicitados encontrados: " + resultados.size());
        }

        return resultados;
    }

    @Override
    public int total_pendientesAdmin() throws  Exception {
        Connection con = null;
        int total_pendientesAdmin = 0;
        String sql = "SELECT COUNT (*) AS CANTIDAD FROM SOLICITUD WHERE ESTADO = 'pendiente'";
        try {
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total_pendientesAdmin = rs.getInt("cantidad");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally{
            if (con != null) {
                con.close();
            }
        }
        return  total_pendientesAdmin;
    }

    @Override
    public List<Solicitud> solicitudesRecientes(){
        List<Solicitud> lista = new ArrayList<>();
        String sql = "SELECT * FROM SOLICITUD WHERE ESTADO = 'pendiente' FETCH FIRST 5 ROWS ONLY";
        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Solicitud s = new Solicitud();
                s.setId_solicitud(rs.getInt("id_solicitud"));
                s.setId_usuario(rs.getInt("id_usuario"));
                s.setArticulo(rs.getString("articulo"));
                s.setFecha_solicitud(rs.getDate("fecha_solicitud").toLocalDate());
                s.setEstado(rs.getString("estado"));
                // Agrega otros campos si son necesarios
                lista.add(s);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }


}
