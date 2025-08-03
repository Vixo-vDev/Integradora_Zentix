package Dao;

import com.example.netrixapp.Modelos.Equipo;
import java.util.List;

public interface IEquipoDao {

    List<Equipo> findAll() throws Exception;
    List<Equipo> tipoEquipo(int idTipoEquipo) throws Exception;
    int totalEquipos();
    int equiposDisponibles();
    int totalStockBajo();
    int calcularCantidad(String descripcion);
    Equipo getById(int id) throws Exception;
    void create(Equipo equipo) throws Exception;
    void update(Equipo equipo, int id) throws Exception;
    void delete(Equipo equipo) throws Exception;
    int equiposActivos();
}
