package impl;

import Dao.IEquipoDao;
import config.ConnectionBD;
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
        Connection con = null;
        String sql="SELECT * FROM EQUIPO WHERE DISPONIBLE = '1' ORDER BY ID_EQUIPO ASC";
        List<Equipo> equipos= new ArrayList<>();

        try {
            con= ConnectionBD.getConnection();
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

            }
            return equipos;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(con != null){
                con.close();
            }
        }
    }

    @Override
    public List<Equipo> tipoEquipo(int idTipoEquipo) throws Exception {
        Connection con = null;
        List<Equipo> equipos= new ArrayList<>();

        String sql = "SELECT \n" +
                "    MIN(ID_EQUIPO) AS ID_EQUIPO,\n" +
                "    DESCRIPCION,\n" +
                "    MIN(ID_TIPO_EQUIPO) AS ID_TIPO_EQUIPO \n" +
                "FROM \n" +
                "    EQUIPO\n" +
                "WHERE ID_TIPO_EQUIPO = ?\n" +
                "GROUP BY \n" +
                "    DESCRIPCION\n" +
                "ORDER BY \n" +
                "    DESCRIPCION";
        try{
            con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1, idTipoEquipo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Equipo equipo = new Equipo();
                equipo.setDescripcion(rs.getString("DESCRIPCION"));
                equipo.setId_equipo(rs.getInt("ID_EQUIPO"));
                equipos.add(equipo);
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally {
            if(con != null){
                con.close();
            }
        }

        return equipos;
    }


    @Override
    public Equipo getById(int id) throws Exception {
        return null;
    }

    @Override
    public void create(Equipo equipo) throws Exception {
        Connection con = null;
        String sql = "INSERT INTO EQUIPO (CODIGO_BIEN, DESCRIPCION, MARCA, MODELO, NUMERO_SERIE, ID_TIPO_EQUIPO) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try{
            con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1, equipo.getCodigo_bien());
            ps.setString(2, equipo.getDescripcion());
            ps.setString(3, equipo.getMarca());
            ps.setString(4, equipo.getModelo());
            ps.setString(5, equipo.getNumero_serie());
            ps.setInt(6, equipo.getTipo_equipo());
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        } finally {
            if(con != null){
                con.close();
            }
        }
    }

    @Override
    public void update(Equipo equipo, int id) throws Exception {
        Connection con = null;
        String sql = "UPDATE EQUIPO SET CODIGO_BIEN = ?, DESCRIPCION = ?, " +
                "MARCA = ?, MODELO = ?, NUMERO_SERIE = ?, " +
                "DISPONIBLE = ?, ID_TIPO_EQUIPO = ?\n" +
                "WHERE ID_EQUIPO = ?";

        try {
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, equipo.getCodigo_bien());
            ps.setString(2, equipo.getDescripcion());
            ps.setString(3, equipo.getMarca());
            ps.setString(4, equipo.getModelo());
            ps.setString(5, equipo.getNumero_serie());
            ps.setInt(6, equipo.getDisponible());
            ps.setInt(7, equipo.getTipo_equipo());
            ps.setInt(8, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(con != null){
                con.close();
            }
        }
    }

    @Override
    public void delete(Equipo equipo) throws Exception {
        Connection con = null;
        String sql = "UPDATE EQUIPO SET DISPONIBLE = '0' WHERE ID_EQUIPO = ?";

        try {
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, equipo.getId_equipo());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(con != null){
                con.close();
            }
        }
    }

    @Override
    public int totalEquipos()  throws Exception{
        Connection con = null;
        int total_equipos = 0;
        String sql = "SELECT COUNT(ID_EQUIPO) FROM EQUIPO";
        try{
            con= ConnectionBD.getConnection();
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                total_equipos = rs.getInt("COUNT(ID_EQUIPO)");
            }

        }
        catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            if(con != null){
                con.close();
            }
        }
        return total_equipos;
    }

    @Override
    public int equiposDisponibles()  throws Exception{
        Connection con = null;
        int equiposDispo = 0;
        String sql = "SELECT COUNT(ID_EQUIPO) FROM EQUIPO WHERE DISPONIBLE = 1";
        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                equiposDispo = rs.getInt("COUNT(ID_EQUIPO)");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(con != null){
                con.close();
            }
        }
        return equiposDispo;
    }

    @Override
    public int totalStockBajo()   throws Exception{
        Connection con = null;
        int totalStock = 0;
        String sql = "SELECT COUNT(*) AS total_tipos_bajo_stock\n" +
                "FROM (\n" +
                "  SELECT id_tipo_equipo\n" +
                "  FROM EQUIPO\n" +
                "  GROUP BY id_tipo_equipo\n" +
                "  HAVING COUNT(*) < 10\n" +
                ") sub";

        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                totalStock = rs.getInt("total_tipos_bajo_stock");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(con != null){
                con.close();
            }
        }
        return totalStock;
    }

    @Override
    public int calcularCantidad(String descripcion) throws Exception {
        Connection con = null;
        int cantidad = 0;
        String sql = "SELECT COUNT(id_equipo) FROM EQUIPO WHERE DESCRIPCION = ? AND EN_USO = 0";

        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, descripcion);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                cantidad = rs.getInt("COUNT(ID_EQUIPO)");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally {
            if(con != null){
                con.close();
            }
        }

        return cantidad;
    }

    @Override
    public int equiposActivos() throws  Exception{
        Connection con = null;
        int equiposActivos = 0;
        String sql = "SELECT COUNT (*) AS TOTAL FROM EQUIPO WHERE DISPONIBLE = '1'";
        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                equiposActivos = rs.getInt("TOTAL");
            }
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally {
            if(con != null){
                con.close();
            }
        }
        return  equiposActivos;
    }

    @Override
    public void setUso(int id_equipo, String estado) throws Exception{
        Connection con = null;
        String sql = "";

        if(estado.equals("aprobado")){
            sql = "UPDATE EQUIPO SET EN_USO = 1 WHERE ID_EQUIPO = ?";
        }
        else if(estado.equals("rechazado") || estado.equals("pendiente")) {
            sql = "UPDATE EQUIPO SET EN_USO = 0 WHERE ID_EQUIPO = ?";
        }

        try{
            con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id_equipo);
            ps.executeUpdate();
        }catch(SQLException e){
            throw new RuntimeException(e);
        }finally {
            if(con != null){
                con.close();
            }
        }

    }

}
