package interfaz.admin.modelos;

public class Equipo {
    private int idEquipo;
    private String codigoBien;
    private String descripcion;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private char disponible; // '1' para disponible, '0' para no disponible
    private int idTipoEquipo; // Clave foránea

    // Constructor vacío
    public Equipo() {
    }

    // Constructor completo
    public Equipo(int idEquipo, String codigoBien, String descripcion, String marca, String modelo,
                  String numeroSerie, char disponible, int idTipoEquipo) {
        this.idEquipo = idEquipo;
        this.codigoBien = codigoBien;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.disponible = disponible;
        this.idTipoEquipo = idTipoEquipo;
    }

    // Constructor sin ID
    public Equipo(String codigoBien, String descripcion, String marca, String modelo,
                  String numeroSerie, char disponible, int idTipoEquipo) {
        this.codigoBien = codigoBien;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.disponible = disponible;
        this.idTipoEquipo = idTipoEquipo;
    }

    // Getters y Setters
    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getCodigoBien() {
        return codigoBien;
    }

    public void setCodigoBien(String codigoBien) {
        this.codigoBien = codigoBien;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public char getDisponible() {
        return disponible;
    }

    public void setDisponible(char disponible) {
        this.disponible = disponible;
    }

    public int getIdTipoEquipo() {
        return idTipoEquipo;
    }

    public void setIdTipoEquipo(int idTipoEquipo) {
        this.idTipoEquipo = idTipoEquipo;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "idEquipo=" + idEquipo +
                ", codigoBien='" + codigoBien + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", numeroSerie='" + numeroSerie + '\'' +
                ", disponible=" + disponible +
                ", idTipoEquipo=" + idTipoEquipo +
                '}';
    }
}