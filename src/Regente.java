package src;

import java.io.Serializable;

public class Regente extends Persona implements Serializable {
    private String apodo;

    private String inicioReinado;
    private String finReinado;

    private boolean LaRioja;
    private boolean BajaNavarra;
    private boolean Francia;
    private boolean Aragon;
    private boolean Castilla;
    private boolean Leon;

    public Regente(String nombreCompleto, String apodo, String nacimiento, Genero genero, Dinastia dinastia) {
        super(nombreCompleto, apodo, nacimiento, genero, dinastia);
        this.apodo = apodo;
        this.inicioReinado = "";
        this.finReinado = "";

        this.LaRioja = false;
        this.BajaNavarra = false;
        this.Francia = false;
        this.Aragon = false;
        this.Castilla = false;
        this.Leon = false;
    }

    public Regente(String nombreCompleto, String curiosidad, String nacimiento, Genero genero, Dinastia dinastia, String apodo, String finReinado, String inicioReinado, boolean laRioja, boolean bajaNavarra, boolean francia, boolean aragon, boolean leon, boolean castilla) {
        super(nombreCompleto, curiosidad, nacimiento, genero, dinastia);
        this.apodo = apodo;
        this.finReinado = finReinado;
        this.inicioReinado = inicioReinado;
        LaRioja = laRioja;
        BajaNavarra = bajaNavarra;
        Francia = francia;
        Aragon = aragon;
        Leon = leon;
        Castilla = castilla;
    }

    public String getApodo() {
        return apodo;
    }

    public String getInicioReinado() {
        return inicioReinado;
    }
    public void setInicioReinado(String inicioReinado) {
        this.inicioReinado = inicioReinado;
    }

    public String getFinReinado() {
        return finReinado;
    }
    public void setFinReinado(String finReinado) {
        this.finReinado = finReinado;
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
