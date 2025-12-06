package src;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import java.util.concurrent.Semaphore;

public class Menu extends Thread {
    private Socket s;
    private static final String xml = "/xml//ReinoNavarra.xml";
    private static Semaphore semaphore = new Semaphore(1);

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
    public void añadirRegente(Regente regente){
        try {
            semaphore.acquire();
            Document doc = descargarBD();
            Element reg = doc.createElement("regente");

            Element nombre = doc.createElement("nombreCompleto");
            nombre.setTextContent(regente.getNombreCompleto());
            reg.appendChild(nombre);
            Element apodo = doc.createElement("apodo");
            apodo.setTextContent(regente.getApodo());
            reg.appendChild(apodo);
            Element curiosidad = doc.createElement("curiosidad");
            curiosidad.setTextContent(regente.getCuriosidad());
            reg.appendChild(curiosidad);
            Element nacimiento = doc.createElement("nacimiento");
            nacimiento.setTextContent(regente.getNacimiento());
            reg.appendChild(nacimiento);
            if(regente.isLaRioja()){
                Element rioja = doc.createElement("LaRioja");
                reg.appendChild(rioja);
            }
            if (regente.isBajaNavarra()){
                Element baja = doc.createElement("BajaNavarra");
                reg.appendChild(baja);
            }
            if (regente.isFrancia()){
                Element francia = doc.createElement("Francia");
                reg.appendChild(francia);
            }
            if (regente.isAragon()){
                Element aragon = doc.createElement("Aragon");
                reg.appendChild(aragon);
            }
            if (regente.isCastilla()){
                Element castilla = doc.createElement("Castilla");
                reg.appendChild(castilla);
            }
            if (regente.isLeon()){
                Element leon = doc.createElement("Leon");
                reg.appendChild(leon);
            }

            reg.setAttribute("genero", regente.getGenero().toString());
            reg.setAttribute("inicioReinado", regente.getInicioReinado());
            reg.setAttribute("finReinado", regente.getFinReinado());
            reg.setAttribute("dinastia", regente.getDinastia().toString());

            doc.appendChild(reg);
            cargarBD(doc);
            semaphore.release();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void añadirPretendiente(Pretendiente pretendiente){
        try {
            semaphore.acquire();
            Document doc = descargarBD();
            Element pret = doc.createElement("pretendiente");

            Element nombre = doc.createElement("nombreCompleto");
            nombre.setTextContent(pretendiente.getNombreCompleto());
            pret.appendChild(nombre);
            Element pretension = doc.createElement("pretension");
            pretension.setTextContent(pretendiente.getPretension());
            pret.appendChild(pretension);
            Element curiosidad = doc.createElement("curiosidad");
            curiosidad.setTextContent(pretendiente.getCuriosidad());
            pret.appendChild(curiosidad);
            Element nacimiento = doc.createElement("nacimiento");
            nacimiento.setTextContent(pretendiente.getNacimiento());
            pret.appendChild(nacimiento);

            pret.setAttribute("genero", pretendiente.getGenero().toString());
            pret.setAttribute("dinastia", pretendiente.getDinastia().toString());

            doc.appendChild(pret);
            cargarBD(doc);
            semaphore.release();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean eliminar(String nombre){return false;}
    public String getCuriosidad(String nombre){return null;}
    public String getPretension(String pretendiente){return null;}
}
