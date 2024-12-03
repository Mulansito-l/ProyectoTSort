import java.io.Serializable;
import java.util.*;

public class GrafoDirigidoAciclico implements Serializable {

    String nombre;
    int numeracion;
    boolean random;

    ArrayList<Vertice> listaAdyacencia;
    boolean letras;

    GrafoDirigidoAciclico(int n, boolean letras){
        this.letras = letras;
        listaAdyacencia = new ArrayList<>();
        if(!letras) {
            for (int i = 0; i < n; i++) {
                listaAdyacencia.add(new Vertice(i,letras));
            }
            numeracion = n;
        }else{
            for (int i = 65; i < 65 + n; i++) {
                listaAdyacencia.add(new Vertice(i,letras));
            }

            numeracion = 65 + n;
        }
        random = false;
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
                listaAdyacencia.add(new Vertice(x,letras));
            }else {
                i--;
            }
        }

        for(int i=0; i< rand.nextInt(5); i++){
            Vertice vi = listaAdyacencia.get(rand.nextInt(4));
            Vertice vf = listaAdyacencia.get(rand.nextInt(4));
            if(!insertarArista(vi.getNumVertice(), vf.getNumVertice())){
                i--;
            }
        }
        random = true;
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
        Random rand = new Random();
        if(!random){
            for (int i = 0; i < listaAdyacencia.size(); i++) {
                if(listaAdyacencia.get(i).getNumVertice() == numeracion){
                    return false;
                }
            }

            listaAdyacencia.add(new Vertice(numeracion,letras));
            numeracion++;
            Collections.sort(listaAdyacencia);
            return true;
        }else{
            for(int i=0; i<1; i++){
                int x = rand.nextInt(20);
                boolean existe = false;
                for(int j=0; j<listaAdyacencia.size(); j++){
                    if(listaAdyacencia.get(j).getNumVertice() == x){
                        existe = true;
                    }
                }
                if(!existe) {
                    listaAdyacencia.add(new Vertice(x,letras));
                }else {
                    i--;
                }
            }
            Collections.sort(listaAdyacencia);
            return true;
        }
    }

    public boolean isLetras() {
        return letras;
    }

    public ArrayList<Vertice> getListaAdyacencia() {
        return listaAdyacencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

        if(contarAristas() < 1){
            return "No hay aristas";
        }

        Vertice inicio = null;

        for (Vertice v : listaAdyacencia) {
            if(gradoDeEntrada(v.getNumVertice()) == 0 && inicio == null){
                inicio = v;
            }
        }

        if(inicio == null){
            return "No existe un vértice con grado de entrada 0";
        }

        topologicalSortAux(inicio, listaAdyacencia, visitados, orden);

        for (int i = 0; i < listaAdyacencia.size(); i++) {
            if(!orden.contains(listaAdyacencia.get(i)) && gradoDeEntrada(listaAdyacencia.get(i).getNumVertice()) == 0){
                topologicalSortAux(listaAdyacencia.get(i), listaAdyacencia, visitados, orden);
            }
        }


        if(orden.size() < listaAdyacencia.size()){
            return "No es posible mostrar todos los nodos";
        }

        StringBuilder sb = new StringBuilder();
        while (!orden.isEmpty()) {
            if(!letras)
                sb.append(orden.removeLast().getNumVertice()).append(" - ");
            else
                sb.append((char) orden.removeLast().getNumVertice()).append(" - ");

        }
        sb.deleteCharAt(sb.length()-1);
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    void topologicalSortAux(Vertice v, ArrayList<Vertice> lista, HashMap<Vertice, Boolean> visitados, ArrayList<Vertice> orden){
        visitados.put(v, true);
        ArrayList<Vertice> aristas = v.getAristasSalida();
        for (int i = 0; i < aristas.size(); i++) {
            Vertice adyacente = aristas.get(i);


            if(visitados.get(adyacente) == null || visitados.get(adyacente) == false) {
                topologicalSortAux(adyacente, lista, visitados, orden);
            }
        }

        orden.add(v);
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

    Integer[][] getMatrizAdyacencia(){
        Integer[][] matrizAdyacencia = new Integer[listaAdyacencia.size() + 1][listaAdyacencia.size() + 1];
        for (Integer[] integers : matrizAdyacencia) {
            Arrays.fill(integers, 0);
        }

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

        return matrizAdyacencia;
    }
}
