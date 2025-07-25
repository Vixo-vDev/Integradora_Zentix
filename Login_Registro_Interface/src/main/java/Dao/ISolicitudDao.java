package Dao;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;

public interface ISolicitudDao {

    void create(Solicitud solicitud) throws Exception;
    int totalSolicitudes(int id_usuario);
    int totalRechazados(int id_usuario);
    int total_pendientes(int id_usuario);

}
