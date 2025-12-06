package src;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;

public class Menu extends Thread {
    private Socket s;
    private static final String xml = "/xml//ReinoNavarra.xml";

    public Menu(Socket socket) {
        this.s = socket;
    }

    private Document descargarBD() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(xml);
    }
    private void cargarBD(Document doc) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        DOMSource s = new DOMSource(doc);
        StreamResult res = new StreamResult(xml);
        t.transform(s, res);
    }

    public void run() {
        String menu = "Seleccione una opcion :\n" + "0.Salir \n" + "1.Añadir un regente o pretendiente \n" +
                        "2.Eliminar un regente o pretendiente\n" + "3.Ver una curiosidad" +
                        "4.Ver la pretension al trono de un pretendiente";
        String linea = "";
        int n, m;
        boolean continuar = true;

        try(DataInputStream ios = new DataInputStream(s.getInputStream());
            Writer w = new OutputStreamWriter(s.getOutputStream())) {
            while (continuar) {
                w.write(menu);
                w.flush();

                n = ios.readInt();
                switch (n){
                    case 0:
                        continuar = false;
                        break;
                    case 1:{
                        w.write("   0.Regente\n" + "    1.Pretendirnte\n");
                        w.flush();
                        m = ios.readInt();
                        try (ObjectInputStream ois = new ObjectInputStream(s.getInputStream())) {
                            switch (m){
                                case 0:{
                                    añadirRegente((Regente) ois.readObject());
                                }
                                case 1:{
                                    añadirPretendiente((Pretendiente) ois.readObject());
                                }
                            }
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                    case 2:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        linea = ios.readLine();
                        if (!eliminar(linea)){
                            w.write("Persona no encontrada en la base de datos\n");
                            w.flush();
                        }
                        break;
                    }
                    case 3:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        linea = ios.readLine();
                        w.write(getCuriosidad(linea) + "\n");
                        w.flush();
                        break;
                    }
                    case 4:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        linea = ios.readLine();
                        w.write(getPretension(linea) + "\n");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void añadirRegente(Regente regente){}
    public synchronized void añadirPretendiente(Pretendiente pretendiente){}
    public boolean eliminar(String nombre){return false;}
    public String getCuriosidad(String nombre){return null;}
    public String getPretension(String pretendiente){return null;}
}
