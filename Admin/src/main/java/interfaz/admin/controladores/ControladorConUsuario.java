package interfaz.admin.controladores;

import interfaz.admin.modelos.Usuario; // Asegúrate de importar tu clase Usuario

/**
 * Interfaz para controladores que necesitan recibir el objeto Usuario logueado.
 * Permite que el DashboardControlador pase el usuario a las vistas hijas.
 */
public interface ControladorConUsuario {
    void setUsuarioActual(Usuario usuario);
}