package src;

public class Pretendiente {
    private String nombreCompleto;
    private String pretension;
    private String curiosidad;
    private String nacimiento;

    private Genero genero;
    private Dinastia dinastia;

    public Pretendiente(String nombreCompleto, String nacimiento, Genero genero, Dinastia dinastia) {
        this.nombreCompleto = nombreCompleto;
        this.pretension = "";
        this.curiosidad = "";
        this.nacimiento = nacimiento;
        this.genero = genero;
        this.dinastia = dinastia;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getPretension() {
        return pretension;
    }
    public void setPretension(String pretension) {
        this.pretension = pretension;
    }

    public String getCuriosidad() {
        return curiosidad;
    }
    public void setCuriosidad(String curiosidad) {
        this.curiosidad = curiosidad;
    }

    public String getNacimiento() {
        return nacimiento;
    }
    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    public Genero getGenero() {
        return genero;
    }

    public Dinastia getDinastia() {
        return dinastia;
    }
}
