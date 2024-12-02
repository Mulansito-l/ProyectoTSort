import java.io.*;
import java.util.ArrayList;

public class SistemaArchivos {
    static ArrayList<GrafoDirigidoAciclico> grafos = new ArrayList();

    static void guardarGrafo(GrafoDirigidoAciclico grafo){

        for (int i = 0; i < grafos.size(); i++) {
            if(grafos.get(i).getNombre().equals(grafo.getNombre())){
                grafos.remove(i);
            }
        }

        grafos.add(grafo);

        try {
            FileOutputStream fileOut = new FileOutputStream("grafos.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(grafos);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    static void leerGrafos(){
        try {
            FileInputStream fileIn = new FileInputStream("grafos.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            grafos = (ArrayList<GrafoDirigidoAciclico>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("No se ha encontrado la clase GrafoDirigidoAciclico");
            c.printStackTrace();
        }
    }

    static boolean grafoExiste(String grafo){
        for (int i = 0; i < grafos.size(); i++) {
            if(grafos.get(i).getNombre().equals(grafo)){
                return true;
            }
        }
        return false;
    }
}
