package impl;

import Dao.ITipoEquipoDao;
import com.example.netrixapp.Controladores.ConnectionBD;
import com.example.netrixapp.Modelos.TipoEquipo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoEquipoDaoImpl implements ITipoEquipoDao {
    @Override
    public List<TipoEquipo> obtenerTodos() {
        List<TipoEquipo> lista = new ArrayList<>();

        String sql = "SELECT * FROM TIPOEQUIPO";
        try{
            Connection con = ConnectionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                TipoEquipo tipoEquipo = new TipoEquipo();
                tipoEquipo.setId_tipo_equipo(rs.getInt("ID_TIPO_EQUIPO"));
                tipoEquipo.setNombre(rs.getString("NOMBRE"));
                lista.add(tipoEquipo);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return lista;
    }
}
