package src;

import java.io.Serializable;

public class Pretendiente extends Persona implements Serializable {
    private String pretension;

    public Pretendiente(String nombreCompleto, String nacimiento, Genero genero, Dinastia dinastia) {
        super(nombreCompleto, nacimiento, genero, dinastia);
        this.pretension = "";
    }

    public Pretendiente(String nombreCompleto, String curiosidad, String nacimiento, Genero genero, Dinastia dinastia, String pretension) {
        super(nombreCompleto, curiosidad, nacimiento, genero, dinastia);
        this.pretension = pretension;
    }

    public Pretendiente(String nombreCompleto, String nacimiento, Genero genero, Dinastia dinastia, String pretension) {
        super(nombreCompleto, nacimiento, genero, dinastia);
        this.pretension = pretension;
    }

    public String getPretension() {
        return pretension;
    }
    public void setPretension(String pretension) {
        this.pretension = pretension;
    }
}
