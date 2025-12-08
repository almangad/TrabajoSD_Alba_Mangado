package src;

import java.io.Serializable;

public class Pretendiente implements Serializable {
    private String pretension;
    private String nombreCompleto;
    private String curiosidad;
    private String nacimiento;

    private Genero genero;
    private Dinastia dinastia;

    public Pretendiente(String nombreCompleto, String nacimiento, Genero genero, Dinastia dinastia) {
        this.nombreCompleto = nombreCompleto;
        this.curiosidad = "";
        this.nacimiento = nacimiento;
        this.genero = genero;
        this.dinastia = dinastia;
        this.pretension = "";
    }

    public Pretendiente(String nombreCompleto, String curiosidad, String nacimiento, Genero genero, Dinastia dinastia, String pretension) {
        this.nombreCompleto = nombreCompleto;
        this.curiosidad = curiosidad;
        this.nacimiento = nacimiento;
        this.genero = genero;
        this.dinastia = dinastia;
        this.pretension = pretension;
    }

    public Pretendiente(String nombreCompleto, String nacimiento, Genero genero, Dinastia dinastia, String pretension) {
        this.nombreCompleto = nombreCompleto;
        this.curiosidad = "";
        this.nacimiento = nacimiento;
        this.genero = genero;
        this.dinastia = dinastia;
        this.pretension = pretension;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getCuriosidad() {
        return curiosidad;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public Dinastia getDinastia() {
        return dinastia;
    }

    public Genero getGenero() {
        return genero;
    }

    public String getPretension() {
        return pretension;
    }
    public void setPretension(String pretension) {
        this.pretension = pretension;
    }
}
