package Dao;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;

public interface ISolicitudDao {

    void create(Solicitud solicitud) throws Exception;
    int totalSolicitudes();
    int totalRechazados();
    int toal_pendientes();

}
