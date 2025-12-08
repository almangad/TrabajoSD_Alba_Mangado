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
import java.util.concurrent.Semaphore;

public class Menu extends Thread {
    private Socket s;
    //Ruta al xml
    private static final String xml = "ReinoNavarra.xml";
    //Con un semaforo impedimos que dos sockets cambien a la vez la base de datos
    private static Semaphore semaphore = new Semaphore(1);

    public Menu(Socket socket) {
        this.s = socket;
    }

    //Carga la base de datos exixtente
    private Document descargarBD() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            File f = new File(xml);
            return db.parse(f);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //Guarda la nueva bd
    private void cargarBD(Document doc){
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            DOMSource s = new DOMSource(doc);
            StreamResult res = new StreamResult(xml);
            t.transform(s, res);
        } catch (TransformerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void run() {
        String menu = "Seleccione una opcion :\n" + "0.Salir \n"
                + "1.Añadir un regente o pretendiente \n"
                + "2.Eliminar un regente o pretendiente\n"
                + "3.Ver una curiosidad\n"
                + "4.Ver la pretension al trono de un pretendiente\n"
                + "5.Ver informacion sobre el nacimiento\n"
                + "6.Buscar apodo de un regente\n"
                + "7.Ver los miembros de las dinastias\n"
                + "8.Comparacion por género\n"
                + "9.¿Quien reinaba entonces?\n"
                + "10.¿Quien reino en un territorio?\n"
                +"FIN\n";
        //FIN es el indicativo del final
        String leido = "", enviado="";
        int n, m;
        boolean continuar = true;

        try(BufferedReader ios = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()))) {
            while (continuar) {
                w.write(menu);
                w.flush();

                leido = ios.readLine();
                n = Integer.parseInt(leido);
                switch (n){
                    case 0:
                        continuar = false;
                        break;
                    case 1:{
                        w.write("   0.Regente\n" + "    1.Pretendirnte\n" + "FIN\n");
                        w.flush();
                        m = Integer.parseInt(ios.readLine());
                        try (ObjectInputStream ois = new ObjectInputStream(s.getInputStream())) {
                            switch (m){
                                case 0:{
                                    añadirRegente((Regente) ois.readObject());
                                    break;
                                }
                                case 1:{
                                    añadirPretendiente((Pretendiente) ois.readObject());
                                    break;
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
                        leido = ios.readLine();
                        eliminar(leido);
                        break;
                    }
                    case 3:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        leido = ios.readLine();
                        enviado = getCuriosidad(leido);
                        w.write(enviado + "\n");
                        w.flush();
                        break;
                    }
                    case 4:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        leido = ios.readLine();
                        enviado = getPretension(leido);
                        w.write(enviado + "\n");
                        w.flush();
                        break;
                    }
                    case 5:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        leido = ios.readLine();
                        enviado = getNacimiento(leido);
                        w.write(enviado + "\n");
                        w.flush();
                        break;
                    }
                    case 6:{
                        w.write("Indique el nombre completo\n");
                        w.flush();
                        leido = ios.readLine();
                        enviado = getApodo(leido);
                        w.write(enviado + "\n");
                        w.flush();
                        break;
                    }
                    case 7:{
                        enviado = Dinastias();
                        w.write(enviado);
                        w.flush();
                        break;
                    }
                    case 8:{
                        enviado = RegentesGenero();
                        w.write(enviado);
                        w.flush();
                        break;
                    }
                    case 9:{
                        w.write("Indique el año sobre el que esté buscando\n");
                        w.flush();
                        m = Integer.parseInt(ios.readLine());
                        w.write(getRegente(m) + "\n");
                        w.flush();
                        break;
                    }
                    case 10:{
                        w.write("Indique el territorio sobre el que desea conocer sus gobernantes\n");
                        w.write("Los posibles territorios a elegir son: LaRioja, BajaNavarra, Francia, Aragon, Castilla, Leon\n");
                        w.flush();
                        leido = ios.readLine();
                        enviado = getTerritorio(leido);
                        w.write(enviado + "FIN\n");
                        w.flush();
                        break;
                    }
                    default:{
                        w.write("Elección no válida\n");
                        w.flush();
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public void añadirRegente(Regente regente){
        try {
            semaphore.acquire();
            Document doc = descargarBD();
            //Crea y completa el regente
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

            //Lo añade como hijo del elemento raiz
            doc.getDocumentElement().appendChild(reg);
            cargarBD(doc);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
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

            doc.getDocumentElement().appendChild(pret);
            cargarBD(doc);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
    public void eliminar(String nombre){
        try {
            semaphore.acquire();
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("nombreCompleto");
            Node p, r;

            for (int i = l.getLength()-1; i >= 0; i--) {
                //Busca el elemento con el nombreCompleto indicado
                if (l.item(i).getTextContent().equals(nombre)){
                    p=l.item(i).getParentNode();
                    r=p.getParentNode();
                    r.removeChild(p);
                }
            }
            cargarBD(doc);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
    public String getCuriosidad(String nombre){
        try {
            semaphore.acquire();
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("nombreCompleto"), hijos;
            Element e;
            Node n;

            for (int i = 0; i < l.getLength(); i++) {
                if (l.item(i).getTextContent().equals(nombre)){
                    e = (Element) l.item(i).getParentNode();
                    hijos = e.getChildNodes();
                    //Busca ente los nodos hijos aquellos que sean elementos y tengan el nombre deseado
                    for (int j = 0; j < hijos.getLength(); j++) {
                        n   =  hijos.item(j);
                        if ((n.getNodeType()== Node.ELEMENT_NODE) && (n.getNodeName().equals("curiosidad"))){
                            return n.getTextContent();
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
        return "La persona indicada no tiene ninguna curiosidad";
    }
    public String getPretension(String nombre){
        try {
            semaphore.acquire();
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
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
        return "La persona no se trata de un pretendiente";
    }
    public String getNacimiento(String nombre){
        try {
            semaphore.acquire();
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
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
        return "Persona no encontrada";
    }
    public String getApodo(String nombre){
        try {
            semaphore.acquire();
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
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
        return "Persona no encontrada";
    }
    //Busca a los miembros de una dinastía con un hilo por dinastía
    public String Dinastias(){
        String respuesta = "";
        try {
            semaphore.acquire();
            List<BuscaDinastias> hilos = new ArrayList<>();
            BuscaDinastias aux, ay;
            Document doc = descargarBD();
            NodeList r = doc.getElementsByTagName("regente");
            NodeList p = doc.getElementsByTagName("pretendiente");

            for (Dinastia d : Dinastia.values()){
                aux = new BuscaDinastias(d, r);
                aux.start();
                hilos.add(aux);
                ay = new BuscaDinastias(d, p);
                ay.start();
                hilos.add(ay);
            }
            for (BuscaDinastias d : hilos){
                d.join();
            }
            for (BuscaDinastias d : hilos){
                respuesta += d.getRes();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            semaphore.release();
        }
        return respuesta + "FIN\n";
    }
    //Devuelve el número de reyes y de reinas
    public String RegentesGenero(){
        int h=0, m=0;
        try {
            semaphore.acquire();
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName("regente");
            Element e;

            for (int i = 0; i < l.getLength(); i++) {
                e = (Element) l.item(i);
                if (e.getAttribute("genero").equals("rey")){
                    h++;
                }else {
                    m++;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
        return "Reyes: " + h + "; Reinaes: " + m + "\n";
    }
    //Devuelve quien reinó en un año pasado com int
    public String getRegente(int f){
        try {
            semaphore.acquire();
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
                            apodo = n.getTextContent();
                        }
                    }
                }
            }
            
            if (encontrado){
                return nombre + " " + apodo;
            }else {
                return "En esa fecha no había un soberano en el Reino";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
    //Devuelve un String con los reyes que reinaron en un territorio didtinto a la Alta Navarra
    public String getTerritorio(String p){
        try {
            semaphore.acquire();
            String res = "", apodo = "", nombre="";
            Document doc = descargarBD();
            NodeList l = doc.getElementsByTagName(p), hijos;
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
            if (l.getLength()==0){
                res = "El lugar indicado no perteneció al reino en ningun momento\n";
            }
            return res;
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }
}
