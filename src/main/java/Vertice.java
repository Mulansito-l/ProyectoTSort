import java.util.ArrayList;
import java.util.Collections;
import com.mxgraph.model.mxICell;

public class Vertice implements Comparable<Vertice>{
    int numVertice;
    ArrayList<Vertice> aristasSalida;

    Vertice(int numVertice){
        aristasSalida = new ArrayList<Vertice>();
        this.numVertice = numVertice;
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
}
