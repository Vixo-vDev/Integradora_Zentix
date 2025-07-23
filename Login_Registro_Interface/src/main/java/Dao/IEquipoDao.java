package Dao;

import com.example.netrixapp.Modelos.Equipo;
import java.util.List;

public interface IEquipoDao {

    List<Equipo> findAll() throws Exception;
    int totalEquipos();
    Equipo getById(int id) throws Exception;
    void create(Equipo equipo) throws Exception;
    void update(Equipo equipo) throws Exception;
    void delete(Equipo equipo) throws Exception;
}
