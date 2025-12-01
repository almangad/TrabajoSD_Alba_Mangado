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
}
