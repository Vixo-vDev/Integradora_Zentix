package Modelos;

public class Usuario {
    //Aplicando la abtración y encapsulamiento

    //Atributos
    private String nombre;
    private String apellidos;
    private String correo;
    private String calle;
    private String lada;
    private String telefono;
    private String date;
    private int edad;
    private String rol;
    private String password;

    public Usuario(){}

    public Usuario(String nombre, String apellidos, String correo, String calle, String lada, String telefono, String date, int edad, String rol, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.calle = calle;
        this.lada = lada;
        this.telefono = telefono;
        this.date = date;
        this.edad = edad;
        this.rol = rol;
        this.password = password;
    }
}
