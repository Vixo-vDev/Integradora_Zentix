package impl;

import Dao.ISolicitudDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Modelos.Solicitud;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            while(rs.next()){
                Solicitud solicitud =new Solicitud();

                solicitud.setId_solitictud(rs.getInt("ID_SOLICITUD"));
                solicitud.setFecha_solicitud(LocalDate.parse(rs.getDate("FECHA_SOLICITUD").toString()));
                solicitud.setArticulo(rs.getString("ARTICULO"));
                solicitud.setCantidad(rs.getInt("CANTIDAD"));
                solicitud.setEstado(rs.getString("ESTADO"));
                solicitudes.add(solicitud);

            }
            return solicitudes;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
