package interfaz.admin.modelos;

import java.sql.Date; // Usaremos java.sql.Date para las fechas de la BD

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellidos;
    private String correoInstitucional;
    private String domicilio;
    private String lada;
    private String telefono;
    private Date fechaNacimiento;
    private Integer edad; // Usamos Integer para permitir valores nulos
    private String rol;
    private String matricula;
    private String password;

    // Constructor vacío
    public Usuario() {
    }

    // Constructor completo
    public Usuario(int idUsuario, String nombre, String apellidos, String correoInstitucional, String domicilio,
                   String lada, String telefono, Date fechaNacimiento, Integer edad, String rol, String matricula, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoInstitucional = correoInstitucional;
        this.domicilio = domicilio;
        this.lada = lada;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.rol = rol;
        this.matricula = matricula;
        this.password = password;
    }

    // Constructor sin ID (útil para insertar nuevos usuarios)
    public Usuario(String nombre, String apellidos, String correoInstitucional, String domicilio,
                   String lada, String telefono, Date fechaNacimiento, Integer edad, String rol, String matricula, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoInstitucional = correoInstitucional;
        this.domicilio = domicilio;
        this.lada = lada;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.rol = rol;
        this.matricula = matricula;
        this.password = password;
    }

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLada() {
        return lada;
    }

    public void setLada(String lada) {
        this.lada = lada;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correoInstitucional='" + correoInstitucional + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", lada='" + lada + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", edad=" + edad +
                ", rol='" + rol + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}