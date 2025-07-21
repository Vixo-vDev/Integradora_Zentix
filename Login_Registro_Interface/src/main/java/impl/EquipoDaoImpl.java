package impl;

import Dao.IEquipoDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.Equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EquipoDaoImpl implements IEquipoDao {
    @Override
    public List<Equipo> findAll() throws Exception {

        String sql="SELECT * FROM EQUIPO";
        List<Equipo> equipos= new ArrayList<>();
        
        try {
            Connection con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs= ps.executeQuery();
            while(rs.next()){
                Equipo equipo =new Equipo();

                equipo.setId_equipo(rs.getInt("id_equipo"));
                equipo.setCodigo_bien(rs.getString("codigo_bien"));
                equipo.setDescripcion(rs.getString("descripcion"));
                equipo.setMarca(rs.getString("marca"));
                equipo.setModelo(rs.getString("modelo"));
                equipo.setNumero_serie(rs.getString("numero_serie"));
                equipo.setDisponible(rs.getInt("disponible"));
                equipo.setTipo_equipo(rs.getInt("tipo_equipo"));

                equipos.add(equipo);
            }
            return equipos;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
