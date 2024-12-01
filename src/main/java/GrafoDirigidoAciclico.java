import java.io.Serializable;
import java.util.*;

public class GrafoDirigidoAciclico implements Serializable {
    String identificador;
    int numeracion;

    ArrayList<Vertice> listaAdyacencia;
    boolean letras;

    GrafoDirigidoAciclico(int n, boolean letras){
        this.letras = letras;
        listaAdyacencia = new ArrayList<>();
        if(!letras) {
            for (int i = 0; i < n; i++) {
                listaAdyacencia.add(new Vertice(i));
            }
            numeracion = n;
        }else{
            for (int i = 65; i < 65 + n; i++) {
                listaAdyacencia.add(new Vertice(i));
            }

            numeracion = 65 + n;
        }
        identificador = UUID.randomUUID().toString();
    }

    GrafoDirigidoAciclico(){
        Random rand = new Random();
        listaAdyacencia = new ArrayList<>();
        for(int i=0; i<4; i++){
            int x = rand.nextInt(20);
            boolean existe = false;
            for(int j=0; j<listaAdyacencia.size(); j++){
                if(listaAdyacencia.get(j).getNumVertice() == x){
                    existe = true;
                }
            }
            if(!existe) {
                listaAdyacencia.add(new Vertice(x));
            }else {
                i--;
            }
        }
        identificador = UUID.randomUUID().toString();
        Collections.sort(listaAdyacencia);
    }

    boolean existeCaminoDFS(int i, int j, HashMap<Vertice, Boolean> visitados){

        if (i==j){
            return false;
        }

        Vertice vi = null;
        Vertice vj = null;
        for(int k=0; k<listaAdyacencia.size(); k++){
            if(listaAdyacencia.get(k).getNumVertice() == i){
                vi = listaAdyacencia.get(k);
            }
        }

        for(int k=0; k<listaAdyacencia.size(); k++){
            if(listaAdyacencia.get(k).getNumVertice() == j){
                vj = listaAdyacencia.get(k);
            }
        }

        if(vi == null || vj == null) {
            throw new IllegalArgumentException("El nodo indicado no existe");
        }

        visitados.put(vi,true);

        for (Vertice vecino:vi.getAristasSalida() ) {
            if (visitados.get(vecino)==null || visitados.get(vecino)==false) {
                if (vecino == vj) {
                    return true;
                } else {
                    if(existeCaminoDFS(vecino.getNumVertice(), j, visitados)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean conectados(int i, int j, int numVertices){
        HashMap<Vertice, Boolean>  visitados = new HashMap<Vertice, Boolean>();
        return existeCaminoDFS(i, j, visitados);
    }

    boolean nuevoVertice(){
        for (int i = 0; i < listaAdyacencia.size(); i++) {
            if(listaAdyacencia.get(i).getNumVertice() == numeracion){
                return false;
            }
        }

        listaAdyacencia.add(new Vertice(numeracion));
        numeracion++;
        Collections.sort(listaAdyacencia);
        return true;
    }

    public boolean isLetras() {
        return letras;
    }

    public ArrayList<Vertice> getListaAdyacencia() {
        return listaAdyacencia;
    }

    boolean insertarArista(int i, int j) throws IllegalArgumentException{

        Vertice vi = null;
        Vertice vj = null;
        for(int k=0; k<listaAdyacencia.size(); k++){
            if(listaAdyacencia.get(k).getNumVertice() == i){
                vi = listaAdyacencia.get(k);
            }
        }

        for(int k=0; k<listaAdyacencia.size(); k++){
            if(listaAdyacencia.get(k).getNumVertice() == j){
                vj = listaAdyacencia.get(k);
            }
        }

        if(vi == null || vj == null){
            throw new IllegalArgumentException("El nodo indicado no existe");
        }else if(i == j){
            return false;
        }else if(adyacente(i,j)){
            return false;
        }

        vi.añadirArista(vj);

        if(tieneCiclos()){
            vi.getAristasSalida().remove(vj);
            return false;
        }

        return true;
    }

    void eliminarAristas(){
        listaAdyacencia.forEach(v -> v.getAristasSalida().clear());
    }

    String topologicalSort(){
        ArrayList<Vertice> orden = new ArrayList<Vertice>();
        HashMap<Vertice, Boolean>  visitados = new HashMap<Vertice, Boolean>();

        Vertice inicio = null;

        for (Vertice v : listaAdyacencia) {
            if(gradoDeEntrada(v.getNumVertice()) == 0){
                inicio = v;
            }
        }

        if(inicio == null){
            return null;
        }

        orden.add(inicio);
        topologicalSortAux(inicio, listaAdyacencia, visitados, orden);

        StringBuilder sb = new StringBuilder();
        while (!orden.isEmpty()) {
            if(!letras)
                sb.append(orden.removeFirst().getNumVertice()).append(" - ");
            else
                sb.append((char) orden.removeFirst().getNumVertice()).append(" - ");


        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    void topologicalSortAux(Vertice v, ArrayList<Vertice> lista, HashMap<Vertice, Boolean> visitados, ArrayList<Vertice> orden){
        visitados.put(v, true);
        for (int i = 0; i < v.getAristasSalida().size(); i++) {
            Vertice adyacente = v.getAristasSalida().get(i);
            for (int j = 0; j < v.getAristasSalida().size(); j++) {
                if(gradoDeEntrada(v.getAristasSalida().get(j).getNumVertice()) < gradoDeEntrada(adyacente.getNumVertice())){
                    if(visitados.get(v.getAristasSalida().get(j)) == null || visitados.get(v.getAristasSalida().get(j)) == false)
                        adyacente = v.getAristasSalida().get(j);
                }
            }

            if(visitados.get(adyacente) == null || !visitados.get(adyacente)){
                orden.add(adyacente);
                topologicalSortAux(adyacente, lista, visitados, orden);
            }
        }
    }

    boolean tieneCiclos(){
        HashMap<Vertice, Boolean> visitados = new HashMap<>();
        HashMap<Vertice, Boolean> pila = new HashMap<>();

        for (int i = 0; i < listaAdyacencia.size(); i++) {
            Vertice v = listaAdyacencia.get(i);
            if((visitados.get(v) == null || !visitados.get(v)) && tieneCiclosAux(listaAdyacencia, v, visitados, pila)){
                return true;
            }
        }

        return false;
    }

    boolean tieneCiclosAux(ArrayList<Vertice> lista, Vertice v, HashMap<Vertice, Boolean> visitados, HashMap<Vertice, Boolean> pila){
        if(visitados.get(v) == null || !visitados.get(v)){
            visitados.put(v, true);
            pila.put(v, true);

            for (int i = 0; i < v.getAristasSalida().size(); i++) {
                Vertice adyacente = v.getAristasSalida().get(i);
                if((visitados.get(adyacente) == null || !visitados.get(adyacente)) && tieneCiclosAux(lista, adyacente, visitados, pila)){
                    return true;
                }else if(visitados.get(adyacente) != null && pila.get(adyacente)){
                    return true;
                }
            }
        }
        pila.put(v, false);
        return false;
    }

    public int contarAristas(){
        int resultado = 0;
        for (Vertice nodo:listaAdyacencia){
            resultado += nodo.getAristasSalida().size();
        }
        return resultado;
    }

    int gradoDeEntrada(int i) throws IllegalArgumentException{
        int gradoEntrada = 0;
        Vertice vi = null;

        for (Vertice vertice : listaAdyacencia) {
            if (vertice.getNumVertice() == i) {
                vi = vertice;
            }
        }
        if(vi == null){
            throw new IllegalArgumentException("El vertice no existe");
        }

        for (int j = 0; j < listaAdyacencia.size(); j++) {
            if(listaAdyacencia.get(j) != vi && listaAdyacencia.get(j).getAristasSalida().contains(vi)){
                gradoEntrada++;
            }
        }

        return gradoEntrada;
    }

    int gradoDeSalida(int i) throws IllegalArgumentException{
        Vertice vi = null;

        for (Vertice vertice : listaAdyacencia) {
            if (vertice.getNumVertice() == i) {
                vi = vertice;
            }
        }
        if(vi == null){
            throw new IllegalArgumentException("El vertice no existe");
        }

        return vi.getAristasSalida().size();
    }

    boolean adyacente(int i, int j) throws IllegalArgumentException{
        Vertice vi = null;
        Vertice vj = null;

        for (Vertice vertice : listaAdyacencia) {
            if (vertice.getNumVertice() == i) {
                vi = vertice;
            }
        }

        for (Vertice vertice : listaAdyacencia) {
            if (vertice.getNumVertice() == j) {
                vj = vertice;
            }
        }

        if(vi == null || vj == null){
            throw new IllegalArgumentException("El vertice no existe");
        }

        return vi.getAristasSalida().contains(vj);
    }

    String mostrarEstructura(){
        if(!letras){
            int[][] matrizAdyacencia = new int[listaAdyacencia.size() + 1][listaAdyacencia.size() + 1];
            for (int i = 1; i < listaAdyacencia.size() + 1; i++) {
                matrizAdyacencia[i][0] = listaAdyacencia.get(i - 1).getNumVertice();
            }
            for (int i = 1; i < listaAdyacencia.size() + 1; i++) {
                matrizAdyacencia[0][i] = listaAdyacencia.get(i - 1).getNumVertice();
            }

            // AQUI
            for (int i = 1; i < listaAdyacencia.size() + 1; i++) {
                int vertice1 = listaAdyacencia.get(i-1).getNumVertice();
                ArrayList<Vertice> aristas1 = listaAdyacencia.get(i-1).getAristasSalida();
                for (int j = 0; j < listaAdyacencia.size(); j++) {
                    int vertice2 = listaAdyacencia.get(j).getNumVertice();

                    for (Vertice arista:aristas1){
                        if (vertice2==arista.getNumVertice()){
                            matrizAdyacencia[i][j+1] = 1;
                        }
                    }

                }
            }

            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < matrizAdyacencia.length; i++) {
                sb.append(Arrays.toString(matrizAdyacencia[i])).append("\n");
            }

            return sb.toString();
        }else{
            char[][] matrizAdyacencia = new char[listaAdyacencia.size() + 1][listaAdyacencia.size() + 1];
            for (int i = 0; i < matrizAdyacencia.length; i++) {
                for (int j = 0; j < matrizAdyacencia[i].length; j++) {
                    matrizAdyacencia[i][j] = (char) 48;
                }
            }

            for (int i = 1; i < listaAdyacencia.size() + 1; i++) {
                matrizAdyacencia[i][0] = (char) listaAdyacencia.get(i - 1).getNumVertice();
            }
            for (int i = 1; i < listaAdyacencia.size() + 1; i++) {
                matrizAdyacencia[0][i] = (char)listaAdyacencia.get(i - 1).getNumVertice();
            }



            // AQUI
            for (int i = 1; i < listaAdyacencia.size() + 1; i++) {
                int vertice1 = listaAdyacencia.get(i-1).getNumVertice();
                ArrayList<Vertice> aristas1 = listaAdyacencia.get(i-1).getAristasSalida();
                for (int j = 0; j < listaAdyacencia.size(); j++) {
                    int vertice2 = listaAdyacencia.get(j).getNumVertice();

                    for (Vertice arista:aristas1){
                        if (vertice2==arista.getNumVertice()){
                            matrizAdyacencia[i][j+1] = (char) 49;
                        }
                    }

                }
            }

            StringBuilder sb = new StringBuilder("");
            for (int i = 0; i < matrizAdyacencia.length; i++) {
                sb.append(Arrays.toString(matrizAdyacencia[i])).append("\n");
            }

            return sb.toString();
        }


    }
}
