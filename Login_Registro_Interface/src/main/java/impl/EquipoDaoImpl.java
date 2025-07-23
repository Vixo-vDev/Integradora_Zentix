package impl;

import Dao.IEquipoDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.Equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

                equipo.setId_equipo(rs.getInt("ID_EQUIPO"));
                equipo.setCodigo_bien(rs.getString("CODIGO_BIEN"));
                equipo.setDescripcion(rs.getString("DESCRIPCION"));
                equipo.setMarca(rs.getString("MARCA"));
                equipo.setModelo(rs.getString("MODELO"));
                equipo.setNumero_serie(rs.getString("NUMERO_SERIE"));
                equipo.setDisponible(rs.getInt("DISPONIBLE"));
                equipo.setTipo_equipo(rs.getInt("ID_TIPO_EQUIPO"));

                equipos.add(equipo);
                System.out.println("Equipos encontrados: " + equipos.size());

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

    @Override
    public int totalEquipos()  {
        String sql = "SELECT COUNT(ID_EQUIPO) FROM EQUIPO";
        try{
            Connection con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            int total_equipos = rs.getInt("COUNT(ID_EQUIPO)");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        return totalEquipos();
    }

    @Override
    public int equiposDisponibles()  {
        String sql = "SELECT COUNT(ID_EQUIPO) FROM EQUIPO WHERE DISPONIBLE = 1";
        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int equiposDiponibles = rs.getInt("COUNT(ID_EQUIPO)");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
