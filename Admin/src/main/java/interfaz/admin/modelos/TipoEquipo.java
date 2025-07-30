package interfaz.admin.modelos;

public class TipoEquipo {
    private int idTipoEquipo;
    private String nombre;

    // Constructor vacío
    public TipoEquipo() {
    }

    // Constructor completo
    public TipoEquipo(int idTipoEquipo, String nombre) {
        this.idTipoEquipo = idTipoEquipo;
        this.nombre = nombre;
    }

    // Constructor sin ID
    public TipoEquipo(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getIdTipoEquipo() {
        return idTipoEquipo;
    }

    public void setIdTipoEquipo(int idTipoEquipo) {
        this.idTipoEquipo = idTipoEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "TipoEquipo{" +
                "idTipoEquipo=" + idTipoEquipo +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}