package src;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class BuscaDinastias extends Thread{
    private Dinastia dinastia;
    private NodeList l;
    private String res;

    public BuscaDinastias(Dinastia dinastia, NodeList lista){
        this.dinastia = dinastia;
        this.l = lista;
        res = "Dinastía " + dinastia + "-------------------------------\n";
    }

    public String getRes() {
        return res;
    }

    public void run(){
        NodeList hijos;
        Node e, n;
        String nombre="", apodo="";
        for (int i = 0; i < l.getLength(); i++) {
            if (l.item(i).getTextContent().equals(dinastia)){
                e = (Element) l.item(i).getParentNode();
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
                res = res + nombre + apodo + "\n";
            }
        }
    }
}
