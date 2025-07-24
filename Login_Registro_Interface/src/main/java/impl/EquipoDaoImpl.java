package impl;

import Dao.IEquipoDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.Equipo;
import oracle.jdbc.proxy.annotation.Pre;

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
    public List<Equipo> tipoEquipo(int idTipoEquipo) throws Exception {
        List<Equipo> equipos= new ArrayList<>();

        String sql = "SELECT DISTINCT DESCRIPCION FROM EQUIPO WHERE ID_TIPO_EQUIPO = ?";

        Connection con= ConnectionBD.getConnection();
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setInt(1, idTipoEquipo);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Equipo equipo = new Equipo();
            equipo.setDescripcion(rs.getString("DESCRIPCION"));
            equipos.add(equipo);
        }


        return equipos;
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
        int total_equipos = 0;
        String sql = "SELECT COUNT(ID_EQUIPO) FROM EQUIPO";
        try{
            Connection con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                total_equipos = rs.getInt("COUNT(ID_EQUIPO)");
            }

        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        return total_equipos;
    }

    @Override
    public int equiposDisponibles()  {
        int equiposDispo = 0;
        String sql = "SELECT COUNT(ID_EQUIPO) FROM EQUIPO WHERE DISPONIBLE = 1";
        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                equiposDispo = rs.getInt("COUNT(ID_EQUIPO)");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return equiposDispo;
    }

    @Override
    public int totalStockBajo()  {
        int totalStock = 0;
        String sql = "SELECT COUNT(*) AS total_tipos_bajo_stock\n" +
                "FROM (\n" +
                "  SELECT id_tipo_equipo\n" +
                "  FROM EQUIPO\n" +
                "  GROUP BY id_tipo_equipo\n" +
                "  HAVING COUNT(*) < 10\n" +
                ") sub";

        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                totalStock = rs.getInt("total_tipos_bajo_stock");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return totalStock;
    }

    @Override
    public int calcularCantidad(String descripcion) {
        int cantidad = 0;
        String sql = "SELECT COUNT(id_equipo) FROM EQUIPO WHERE DESCRIPCION = ?";

        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            
        }catch(SQLException e){
            throw new RuntimeException(e);
        }

        return 0;
    }

}
