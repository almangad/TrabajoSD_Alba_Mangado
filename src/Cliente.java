package src;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    public static void main(String [] args) {
        try(Socket s = new Socket("localhost", 55555);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            Scanner scanner = new Scanner(System.in)){
            boolean salir = false;
            String linea;
            int n, m;

            //Mientras no se indique la salida (opcion 0) se repite el menú
            while (!salir) {
                leer(br);
                linea = scanner.nextLine();
                w.write(linea + "\n");
                w.flush();

                n = Integer.parseInt(linea);
                switch(n) {
                    case 0:{
                        salir = true;
                        break;
                    }
                    case 1:{
                        leer(br);
                        linea = scanner.nextLine();
                        w.write(linea + "\n");
                        w.flush();

                        m = Integer.parseInt(linea);
                        switch(m) {
                            case 0:{
                                String nombreCompleto, apodo, nacimiento;
                                Genero genero;
                                Dinastia dinastia;

                                //Creamos el regente
                                System.out.println("Nombre?\n");
                                nombreCompleto = scanner.nextLine();
                                System.out.println("Apodo?\n");
                                apodo = scanner.nextLine();
                                System.out.println("Nacimiento?\n");
                                nacimiento = scanner.nextLine();
                                System.out.println("Genero?\n");
                                genero = Genero.valueOf(scanner.nextLine());
                                System.out.println("Dinastia?\n");
                                dinastia = Dinastia.valueOf(scanner.nextLine());

                                Regente r = new Regente(nombreCompleto, apodo, nacimiento, genero, dinastia);

                                //Utilizamos ObjectOutputStream para enviar el regente
                                try(ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream())) {
                                    oos.reset();
                                    oos.writeObject(r);
                                    oos.flush();
                                }
                                break;
                            }
                            case 1:{
                                String nombreCompleto, nacimiento;
                                Genero genero;
                                Dinastia dinastia;

                                //Creamos el pretendiente
                                System.out.println("Nombre?\n");
                                nombreCompleto = scanner.nextLine();
                                System.out.println("Nacimiento?\n");
                                nacimiento = scanner.nextLine();
                                System.out.println("Genero?\n");
                                genero = Genero.valueOf(scanner.nextLine());
                                System.out.println("Dinastia?\n");
                                dinastia = Dinastia.valueOf(scanner.nextLine());

                                //Utilizamos ObjectOutputStream para enviar el pretendiente
                                Pretendiente p = new Pretendiente(nombreCompleto, nacimiento, genero, dinastia);

                                try(ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream())) {
                                    oos.reset();
                                    oos.writeObject(p);
                                    oos.flush();
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case 2:{
                        System.out.println(br.readLine());
                        w.write(scanner.nextLine() + "\n");
                        w.flush();
                        break;
                    }
                    case 3:{
                        linea = br.readLine();
                        System.out.println(linea);
                        w.write(scanner.nextLine() + "\n");
                        w.flush();
                        linea= br.readLine();
                        System.out.println(linea);
                        break;
                    }
                    case 4:{
                        System.out.println(br.readLine());
                        w.write(scanner.nextLine() + "\n");
                        w.flush();
                        linea= br.readLine();
                        System.out.println(linea);
                        break;
                    }
                    case 5:{
                        System.out.println(br.readLine());
                        w.write(scanner.nextLine() + "\n");
                        w.flush();
                        linea= br.readLine();
                        System.out.println(linea);
                        break;
                    }
                    case 6:{
                        System.out.println(br.readLine());
                        w.write(scanner.nextLine() + "\n");
                        w.flush();
                        linea= br.readLine();
                        System.out.println(linea);
                        break;
                    }
                    case 7:{
                        leer(br);
                        break;
                    }
                    case 8:{
                        linea= br.readLine();
                        System.out.println(linea);
                        break;
                    }
                    case 9:{
                        System.out.println(br.readLine());
                        w.write(scanner.nextLine() + "\n");
                        w.flush();
                        linea= br.readLine();
                        System.out.println(linea);
                        break;
                    }
                    case 10:{
                        linea= br.readLine();
                        System.out.println(linea);
                        linea= br.readLine();
                        System.out.println(linea);
                        w.write(scanner.nextLine() + "\n");
                        w.flush();
                        leer(br);
                        break;
                    }
                    default:{
                        linea= br.readLine();
                        System.out.println(linea);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //Lee hasta la indicación de fin
    public static void leer(BufferedReader br) throws IOException {
        boolean aux = false;
        String linea;
        while(!aux && ((linea= br.readLine())!= null)) {
            if (linea.equals("FIN")) {
                aux = true;
            }else {
                System.out.println(linea);
            }
        }
    }
}
