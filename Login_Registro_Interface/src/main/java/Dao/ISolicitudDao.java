package Dao;

import com.example.netrixapp.Modelos.Solicitud;
import com.example.netrixapp.Modelos.Usuario;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface ISolicitudDao {

    List<Solicitud> findAll(int id_usuario) throws Exception;
    List<Solicitud> findAllAdmin() throws Exception;
    List<Solicitud> findByFilters(int id_usuario, String estado, LocalDate desde, LocalDate hasta) throws Exception;
    int create(Solicitud solicitud) throws Exception;
    int totalSolicitudes(int id_usuario) throws Exception;
    int totalRechazados(int id_usuario) throws Exception;
    int total_pendientes(int id_usuario) throws Exception ;
    void actualizarEstado(int idSolicitud, String nuevoEstado) throws Exception;
    public List<Solicitud> findByEstado(String estado) throws Exception;
    List<Object[]> findEquiposMasSolicitados(Date inicio, Date fin, int limite) throws Exception;
    List<Object[]> findEquiposMenosSolicitados(Date inicio, Date fin, int limite) throws Exception;
    List<Object[]> findEquiposMasSolicitadosSinFiltro(int limite) throws Exception ;
    int total_pendientesAdmin() throws  Exception;
    List<Solicitud >solicitudesRecientes();


}
