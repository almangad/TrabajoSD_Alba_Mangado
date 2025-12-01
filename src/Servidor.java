package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try(ServerSocket ss = new ServerSocket(55555)) {
            while(true) {
                Socket s = ss.accept();
                pool.execute(new Menu(s));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
