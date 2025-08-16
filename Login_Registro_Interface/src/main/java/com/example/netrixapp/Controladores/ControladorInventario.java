package com.example.netrixapp.Controladores;

import com.example.netrixapp.Modelos.Equipo;
import com.example.netrixapp.Vistas.VistaInventario;
import impl.EquipoDaoImpl;

import java.util.List;

public class ControladorInventario {
    private VistaInventario vista;
    private final EquipoDaoImpl equipoDao;

    public ControladorInventario(VistaInventario vista) {
        this.vista = vista;
        this.equipoDao = new EquipoDaoImpl();
        cargarEquipos();
    }

    private void cargarEquipos() {
        try {
            List<Equipo> listaEquipos = equipoDao.findAll();
            vista.mostrarEquipos(listaEquipos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
