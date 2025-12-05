package src;

import java.io.Serializable;

public class Persona implements Serializable {
    private String nombreCompleto;
    private String curiosidad;
    private String nacimiento;

    private Genero genero;
    private Dinastia dinastia;

    public Persona(String nombreCompleto, String nacimiento, Genero genero, Dinastia dinastia) {
        this.nombreCompleto = nombreCompleto;
        this.curiosidad = "";
        this.nacimiento = nacimiento;
        this.genero = genero;
        this.dinastia = dinastia;
    }
    public Persona(String nombreCompleto, String curiosidad, String nacimiento, Genero genero, Dinastia dinastia) {
        this.nombreCompleto = nombreCompleto;
        this.curiosidad = curiosidad;
        this.nacimiento = nacimiento;
        this.genero = genero;
        this.dinastia = dinastia;
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

    public void setCuriosidad(String curiosidad) {
        this.curiosidad = curiosidad;
    }
}
