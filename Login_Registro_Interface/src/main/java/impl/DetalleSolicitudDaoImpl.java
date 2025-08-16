package impl;

import Dao.IDetalleSolicitudDao;
import com.example.netrixapp.Modelos.DetalleSolicitud;
import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;
import config.ConnectionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DetalleSolicitudDaoImpl implements IDetalleSolicitudDao {
    @Override
    public List<DetalleSolicitud> findAll() throws Exception {
        return List.of();
    }

    @Override
    public void create(DetalleSolicitud detalleSolicitud) throws Exception {
        Connection con = null;
        String sql = "INSERT INTO DETALLESOLICITUD (ID_EQUIPO, ID_TIPO_EQUIPO, ID_SOLICITUD, CANTIDAD)" +
                "VALUES (?, ?, ?, ?)";

        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, detalleSolicitud.getId_equipo());
            ps.setInt(2, detalleSolicitud.getId_tipo_equipo());
            ps.setInt(3, detalleSolicitud.getId_solicitud());
            ps.setInt(4, detalleSolicitud.getCantidad());
            ps.executeUpdate();

        }catch(SQLException e){
            throw new Exception(e.getMessage());
        }
        finally{
            if (con != null) {
                con.close();
            }
        }
    }

    @Override
    public int searchIdequip(Solicitud solicitud) throws Exception{
        int idequipo = 0;
        Connection con = null;
        String sql = "SELECT ID_EQUIPO FROM DETALLESOLICITUD WHERE ID_SOLICITUD = ?";
        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, solicitud.getId_solicitud());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                idequipo = rs.getInt("ID_EQUIPO");
            }
        } catch (SQLException e ) {
            throw new RuntimeException(e);
        }finally{
            if(con != con){
                con.close();
            }
        }
        return idequipo;
    }
}
