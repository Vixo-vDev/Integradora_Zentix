package Dao;

import com.example.netrixapp.Modelos.DetalleSolicitud;
import com.example.netrixapp.Modelos.Usuario;

import java.util.List;

public interface IDetalleSolicitudDao {

    List<DetalleSolicitud> findAll() throws Exception;
    void create(DetalleSolicitud detalleSolicitud) throws Exception;
}
