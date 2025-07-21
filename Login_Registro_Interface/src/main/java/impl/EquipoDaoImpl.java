package impl;

import Dao.IEquipoDao;
import com.example.netrixapp.Modelos.Equipo;

import java.util.List;

public class EquipoDaoImpl implements IEquipoDao {
    @Override
    public List<Equipo> findAll() throws Exception {
        return List.of();
    }

    @Override
    public Equipo getById(int id) throws Exception {
        return null;
    }

    @Override
    public void create(Equipo equipo) throws Exception {

    }

    @Override
    public void update(Equipo equipo) throws Exception {

    }

    @Override
    public void delete(Equipo equipo) throws Exception {

    }
}
