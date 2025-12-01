package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try(ServerSocket ss = new ServerSocket(55555)) {
            while(true) {
                Socket s = ss.accept();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
