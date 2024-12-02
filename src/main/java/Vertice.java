import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import com.mxgraph.model.mxICell;

public class Vertice implements Comparable<Vertice>, Serializable {
    int numVertice;
    boolean letras;
    ArrayList<Vertice> aristasSalida;

    Vertice(int numVertice, boolean letras){
        aristasSalida = new ArrayList<Vertice>();
        this.numVertice = numVertice;
        this.letras = letras;
    }

    public void añadirArista(Vertice v){
        aristasSalida.add(v);
        Collections.sort(aristasSalida);
    }

    public ArrayList<Vertice> getAristasSalida() { return aristasSalida; }

    public int getNumVertice() {return numVertice;}

    public int cuantasAristasHay(){return aristasSalida.size();}

    @Override
    public int compareTo(Vertice o) {
        return this.numVertice - o.numVertice;
    }

    @Override
    public String toString() {
        if(!letras)
            return Integer.toString(numVertice);
        return Character.toString(numVertice);
    }
}
