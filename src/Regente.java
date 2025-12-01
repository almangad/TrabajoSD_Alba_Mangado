package src;

public class Regente {
    private String nombreCompleto;
    private String apodo;
    private String curiosidad;
    private String nacimiento;

    private Genero genero;
    private int inicioReinado;
    private int finReinado;
    private Dinastia dinastia;

    private boolean LaRioja;
    private boolean BajaNavarra;
    private boolean Francia;
    private boolean Aragon;
    private boolean Castilla;
    private boolean Leon;

    public Regente(String nombreCompleto, String apodo, String nacimiento, Genero genero, Dinastia dinastia) {
        this.nombreCompleto = nombreCompleto;
        this.apodo = apodo;
        this.curiosidad = "";
        this.nacimiento = nacimiento;
        this.genero = genero;
        this.inicioReinado = -1;
        this.finReinado = -1;
        this.dinastia = dinastia;

        this.LaRioja = false;
        this.BajaNavarra = false;
        this.Francia = false;
        this.Aragon = false;
        this.Castilla = false;
        this.Leon = false;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getApodo() {
        return apodo;
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

    public int getInicioReinado() {
        return inicioReinado;
    }
    public void setInicioReinado(int inicioReinado) {
        this.inicioReinado = inicioReinado;
    }

    public int getFinReinado() {
        return finReinado;
    }
    public void setFinReinado(int finReinado) {
        this.finReinado = finReinado;
    }

    public Dinastia getDinastia() {
        return dinastia;
    }

    public boolean isLaRioja() {
        return LaRioja;
    }
    public void setLaRioja(boolean laRioja) {
        LaRioja = laRioja;
    }

    public boolean isBajaNavarra() {
        return BajaNavarra;
    }
    public void setBajaNavarra(boolean bajaNavarra) {
        BajaNavarra = bajaNavarra;
    }

    public boolean isFrancia() {
        return Francia;
    }
    public void setFrancia(boolean francia) {
        Francia = francia;
    }

    public boolean isAragon() {
        return Aragon;
    }
    public void setAragon(boolean aragon) {
        Aragon = aragon;
    }

    public boolean isCastilla() {
        return Castilla;
    }
    public void setCastilla(boolean castilla) {
        Castilla = castilla;
    }

    public boolean isLeon() {
        return Leon;
    }
    public void setLeon(boolean leon) {
        Leon = leon;
    }
}
