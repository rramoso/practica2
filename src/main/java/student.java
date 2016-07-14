/**
 * Created by ricardoramos on 5/30/16.
 */
public class student {

    private String matricula;
    private  String nombre;
    private  String apellido;
    private  String telefono;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String nombre) {
        this.apellido = nombre;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String carrera) {
        this.telefono = carrera;
    }

    public String getMatricula() {return matricula;  }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
