package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    public static void main(String[] args) {
        //Creamos un pool del que obtenemos los hilos necesarios
        ExecutorService pool = Executors.newCachedThreadPool();
        try(ServerSocket ss = new ServerSocket(55555)) {
            while(true) {
                Socket s = ss.accept();
                pool.execute(new Menu(s));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            pool.shutdown();
        }
    }
}
