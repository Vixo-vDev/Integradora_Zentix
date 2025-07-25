package Dao;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;

import java.util.List;

public interface ISolicitudDao {

    List<Solicitud> findAll(int id_usuario) throws Exception;
    void create(Solicitud solicitud) throws Exception;
    int totalSolicitudes(int id_usuario);
    int totalRechazados(int id_usuario);
    int total_pendientes(int id_usuario);

}
