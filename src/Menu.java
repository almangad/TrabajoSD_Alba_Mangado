package src;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        String menu = "Seleccione una opcion :\n" + "0.Salir \n"
                + "1.Añadir un regente o pretendiente \n"
                + "2.Eliminar un regente o pretendiente\n"
                + "3.Ver una curiosidad"
                + "4.Ver la pretension al trono de un pretendiente"
                + "5.Ver informacion sobre el nacimiento"
                + "6.Buscar apodo de un regente"
                + "7.Ver los miembros de las dinastias"
                + "8.Comparacion por género"
                + "9.¿Quien reinaba entonces?"
                + "10.¿Quien reino en un territorio?";
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
                        eliminar(linea);
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
                        w.flush();
                        break;
                    }
                    case 5:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        linea = ios.readLine();
                        w.write(getNacimiento(linea) + "\n");
                        w.flush();
                        break;
                    }
                    case 6:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        linea = ios.readLine();
                        w.write(getApodo(linea) + "\n");
                        w.flush();
                        break;
                    }
                    case 7:{
                        String mienbros = Dinastias();
                        w.write(mienbros);
                        w.flush();
                        break;
                    }
                    case 8:{
                        String gen = RegentesGenero();
                        w.write(gen);
                        w.flush();
                        break;
                    }
                    case 9:{
                        w.write("Indique el año sobre el que esté buscando\n");
                        w.flush();
                        m = ios.readInt();
                        w.write(getRegente(m) + "\n");
                        w.flush();
                        break;
                    }
                    case 10:{
                        w.write("Indique el territorio sobre el que desea conocer sus gobernantes\n");
                        w.write("Los posibles territorios a elegir son: LaRioja, BajaNavarra, Francia, Aragon, Castilla, Leon\n");
                        w.flush();
                        linea = ios.readLine();
                        w.write(getTerritorio(linea) + "\n");
                        w.flush();
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
    public void eliminar(String nombre){
        try {
            semaphore.acquire();
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("nombreCompleto");

            for (int i = 0; i < l.getLength(); i++) {
                if (l.item(i).getTextContent().equals(nombre)){
                    doc.removeChild(l.item(i).getParentNode());
                }
            }
            cargarBD(doc);
            semaphore.release();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
    public String getCuriosidad(String nombre){
        try {
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("nombreCompleto"), hijos;
            Element e;
            Node n;

            for (int i = 0; i < l.getLength(); i++) {
                if (l.item(i).getTextContent().equals(nombre)){
                    e = (Element) l.item(i).getParentNode();
                    hijos = e.getChildNodes();
                    for (int j = 0; j < hijos.getLength(); j++) {
                        n   =  hijos.item(j);
                        if ((n.getNodeType()== Node.ELEMENT_NODE) && (n.getNodeName().equals("curiosidad"))){
                            return n.getTextContent();
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return "La persona indicada no tiene ninguna curiosidad";
    }
    public String getPretension(String nombre){
        try {
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("nombreCompleto"), hijos;
            Element e;
            Node n;

            for (int i = 0; i < l.getLength(); i++) {
                if (l.item(i).getTextContent().equals(nombre)){
                    e = (Element) l.item(i).getParentNode();
                    hijos = e.getChildNodes();
                    for (int j = 0; j < hijos.getLength(); j++) {
                        n   =  hijos.item(j);
                        if ((n.getNodeType()== Node.ELEMENT_NODE) && (n.getNodeName().equals("pretension"))){
                            return n.getTextContent();
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return "La persona no se trata de un pretendiente";
    }
    public String getNacimiento(String nombre){
        try {
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("nombreCompleto"), hijos;
            Element e;
            Node n;

            for (int i = 0; i < l.getLength(); i++) {
                if (l.item(i).getTextContent().equals(nombre)){
                    e = (Element) l.item(i).getParentNode();
                    hijos = e.getChildNodes();
                    for (int j = 0; j < hijos.getLength(); j++) {
                        n   =  hijos.item(j);
                        if ((n.getNodeType()== Node.ELEMENT_NODE) && (n.getNodeName().equals("nacimiento"))){
                            return n.getTextContent();
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return "Persona no encontrada";
    }
    public String getApodo(String nombre){
        try {
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("nombreCompleto"), hijos;
            Element e;
            Node n;

            for (int i = 0; i < l.getLength(); i++) {
                if (l.item(i).getTextContent().equals(nombre)){
                    e = (Element) l.item(i).getParentNode();
                    hijos = e.getChildNodes();
                    for (int j = 0; j < hijos.getLength(); j++) {
                        n   =  hijos.item(j);
                        if ((n.getNodeType()== Node.ELEMENT_NODE) && (n.getNodeName().equals("apodo"))){
                            return n.getTextContent();
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return "Persona no encontrada";
    }
    public String Dinastias(){
        String respuesta = "";
        try {
            List<BuscaDinastias> hilos = new ArrayList<>();
            BuscaDinastias aux;
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("dinastia");

            for (Dinastia d : Dinastia.values()){
                aux = new BuscaDinastias(d, l);
                aux.start();
                hilos.add(aux);
            }
            for (BuscaDinastias d : hilos){
                d.join();
            }
            for (BuscaDinastias d : hilos){
                respuesta += d.getRes();
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return respuesta;
    }
    public String RegentesGenero(){
        int h=0, m=0;
        try {
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("genero");
            for (int i = 0; i < l.getLength(); i++) {
                if (l.item(i).getTextContent().equals("rey")){
                    h++;
                }
                if (l.item(i).getTextContent().equals("reina")){
                    m++;
                }
            }

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        return "Reyes: " + h + "; Reinaes: " + m;
    }
    public String getRegente(int f){
        try {
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("regente"), hijos;
            Element e;
            Node n;
            String nombre = "", apodo = "", ini, fin;
            boolean encontrado = false;
            
            for (int i = 0; i < l.getLength(); i++) {
                e = (Element) l.item(i);
                ini = e.getAttribute("inicioReinado");
                fin = e.getAttribute("finReinado");
                if((Integer.parseInt(ini)<f)&&(Integer.parseInt(fin)>f)){
                    encontrado = true;
                    hijos = e.getChildNodes();
                    for (int j = 0; j < hijos.getLength(); j++) {
                        n   =  hijos.item(j);
                        if ((n.getNodeType()== Node.ELEMENT_NODE) && (n.getNodeName().equals("nombreCompleto"))){
                            nombre = n.getTextContent();
                        }
                        if ((n.getNodeType()== Node.ELEMENT_NODE) && (n.getNodeName().equals("apodo"))){
                            nombre = n.getTextContent();
                        }
                    }
                }
            }
            
            if (encontrado){
                return nombre + "" + apodo;
            }else {
                return "En esa fecha no había un soberano en el Reino";
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }
    public String getTerritorio(String p){
        try {
            String res = "", apodo = "", nombre="";
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("p"), hijos;
            Element e;
            Node n;

            for (int i = 0; i < l.getLength(); i++) {
                e = (Element) l.item(i).getParentNode();
                hijos = e.getChildNodes();
                for (int j = 0; j < hijos.getLength(); j++) {
                    n = hijos.item(j);
                    if ((n.getNodeType() == Node.ELEMENT_NODE) && (n.getNodeName().equals("apodo"))) {
                        apodo = n.getTextContent();
                    }
                    if ((n.getNodeType() == Node.ELEMENT_NODE) && (n.getNodeName().equals("nombreCompleto"))) {
                        nombre = n.getTextContent();
                    }
                }
                res = res + nombre + " " + apodo + "\n";
            }
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        }
        return "El lugar indicado no perteneció al reino en ningun momento";
    }
}
