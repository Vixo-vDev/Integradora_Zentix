package Dao;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;

import java.time.LocalDate;
import java.util.List;

public interface ISolicitudDao {

    List<Solicitud> findAll(int id_usuario) throws Exception;
    List<Solicitud> findByFilters(int id_usuario, String estado, LocalDate desde, LocalDate hasta) throws Exception;
    void create(Solicitud solicitud) throws Exception;
    int totalSolicitudes(int id_usuario);
    int totalRechazados(int id_usuario);
    int total_pendientes(int id_usuario);

}
