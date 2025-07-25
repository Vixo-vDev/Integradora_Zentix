package impl;

import Dao.ISolicitudDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.Solicitud;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SolicitudDaoImpl implements ISolicitudDao {

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
    public int totalSolicitudes() {
        int totalSolicitudes=0;
        String sql = "SELECT COUNT (id_solicitud) FROM SOLICITUD WHERE id_usuario = 28;";
        return totalSolicitudes;
    }
}
