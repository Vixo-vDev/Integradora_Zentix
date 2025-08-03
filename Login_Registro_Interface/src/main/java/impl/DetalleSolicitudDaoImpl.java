package impl;

import Dao.IDetalleSolicitudDao;
import com.example.netrixapp.Modelos.DetalleSolicitud;
import com.example.netrixapp.Modelos.Usuario;
import config.ConnectionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DetalleSolicitudDaoImpl implements IDetalleSolicitudDao {
    @Override
    public List<DetalleSolicitud> findAll() throws Exception {
        return List.of();
    }

    @Override
    public void create(DetalleSolicitud detalleSolicitud) throws Exception {
        String sql = "INSERT INTO DETALLESOLICITUD (ID_EQUIPO, ID_TIPO_EQUIPO, ID_SOLICITUD, CANTIDAD)" +
                "VALUES (?, ?, ?, ?)";

        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, detalleSolicitud.getId_equipo());
            ps.setInt(2, detalleSolicitud.getId_tipo_equipo());
            ps.setInt(3, detalleSolicitud.getId_solicitud());
            ps.setInt(4, detalleSolicitud.getCantidad());
            ps.executeUpdate();

        }catch(SQLException e){
            throw new Exception(e.getMessage());
        }
    }
}
