package Dao;

import com.example.netrixapp.Modelos.Equipo;
import java.util.List;

public interface IEquipoDao {

    List<Equipo> findAll() throws Exception;
    List<Equipo> tipoEquipo(int idTipoEquipo) throws Exception;
    int totalEquipos() throws Exception;
    int equiposDisponibles() throws Exception;
    int totalStockBajo() throws Exception;
    int calcularCantidad(String descripcion)  throws Exception;
    Equipo getById(int id) throws Exception;
    void create(Equipo equipo) throws Exception;
    void update(Equipo equipo, int id) throws Exception;
    void delete(Equipo equipo) throws Exception;
    int equiposActivos() throws  Exception;
    void setUso(int id_equipo, String estado) throws Exception;
}
