package src;

import java.io.*;
import java.net.Socket;

public class Menu extends Thread {
    private Socket s;
    private static final String xml = "/xml//ReinoNavarra.xml";

    public Menu(Socket socket) {
        this.s = socket;
    }

    public void run() {
        String menu = "Seleccione una opcion :\n" + "0.Salir \n" + "0.Añadir un regente o pretendiente \n";
        String linea;
        boolean continuar = true;

        try(DataInputStream ios = new DataInputStream(s.getInputStream());
            Writer w = new OutputStreamWriter(s.getOutputStream())) {
            while (continuar) {
                w.write(menu);
                w.flush();

                linea = ios.readLine();
                switch (linea){
                    case "0":
                        continuar = false;
                        break;
                    case "1":
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
