import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal {
    JFrame ventana;
    JButton nuevoGrafo;
    JButton abrirGrafo;

    public static void main(String[] args) {
        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
    }

    VentanaPrincipal(){
        ventana = new JFrame("Selección de Grafo)");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBounds(0,0,400,300);
        ventana.setLayout(null);
        ventana.setResizable(false);

        nuevoGrafo = new JButton("Nuevo Grafo");
        abrirGrafo = new JButton("Abrir Grafo");

        nuevoGrafo.setBounds(100,50, 200, 50);
        abrirGrafo.setBounds(100,150, 200, 50);

        nuevoGrafo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] opciones = {"Números","Letras"};
                int mostrar = JOptionPane.showOptionDialog(ventana,"Como mostrar el grafo: ","Nuevo grafo",
                        JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones,opciones[0]);
                if(mostrar == 0){
                    GrafoDirigidoAciclico grafo = new GrafoDirigidoAciclico(0,false);
                    new VentanaGrafo(grafo);
                }else{
                    GrafoDirigidoAciclico grafo = new GrafoDirigidoAciclico(0,false);
                    new VentanaGrafo(grafo);
                }

                ventana.setVisible(false);
                ventana.dispose();
            }
        });

        ventana.add(nuevoGrafo);
        ventana.add(abrirGrafo);
        ventana.setVisible(true);
    }
}
