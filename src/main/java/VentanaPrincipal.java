import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;

public class VentanaPrincipal {
    JFrame ventana;
    JButton nuevoGrafo;
    JButton nuevoGrafoAleatorio;
    JButton abrirGrafo;

    public static void main(String[] args) {
        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
    }

    VentanaPrincipal(){
        SistemaArchivos.leerGrafos();
        ventana = new JFrame("Selección de Grafo)");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBounds(0,0,400,300);
        ventana.setLayout(null);
        ventana.getContentPane().setBackground(new Color(157, 121, 185));
        ventana.setResizable(false);

        nuevoGrafo = new JButton("Nuevo grafo");
        nuevoGrafoAleatorio = new JButton("Nuevo grafo aleatorio");
        abrirGrafo = new JButton("Abrir grafo");

        nuevoGrafo.setBounds(100,50, 200, 20);
        nuevoGrafo.setBackground(new Color(100,51,152));
        nuevoGrafo.setForeground(Color.white);

        nuevoGrafoAleatorio.setBounds(100,100, 200, 20);
        nuevoGrafoAleatorio.setBackground(new Color(100,51,152));
        nuevoGrafoAleatorio.setForeground(Color.white);

        abrirGrafo.setBounds(100,150, 200, 20);
        abrirGrafo.setBackground(new Color(100,51,152));
        abrirGrafo.setForeground(Color.white);

        nuevoGrafo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GrafoDirigidoAciclico grafo;
                String[] opciones = {"Números","Letras"};
                int mostrar = JOptionPane.showOptionDialog(ventana,"Como mostrar el grafo: ","Nuevo grafo",
                        JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,opciones,opciones[0]);
                if(mostrar == 0){
                    grafo = new GrafoDirigidoAciclico(0,false);
                }else{
                    grafo = new GrafoDirigidoAciclico(0,true);
                }

                String nombre;
                LocalDateTime fecha;
                do {
                    nombre = JOptionPane.showInputDialog("Ingrese el nombre");
                    fecha = LocalDateTime.now();

                    if(SistemaArchivos.grafoExiste(nombre)){
                        JOptionPane.showMessageDialog(ventana,"El grafo ya existe");
                    }
                } while(nombre.isEmpty() || SistemaArchivos.grafoExiste(nombre));
                nombre = nombre +" - "+fecha;
                grafo.setNombre(nombre);

                new VentanaGrafo(grafo);

                ventana.setVisible(false);
                ventana.dispose();
            }
        });

        nuevoGrafoAleatorio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GrafoDirigidoAciclico grafo;
                grafo = new GrafoDirigidoAciclico();

                String nombre;
                do {
                    nombre = JOptionPane.showInputDialog("Ingrese el nombre");
                    if(SistemaArchivos.grafoExiste(nombre)){
                        JOptionPane.showMessageDialog(ventana,"El grafo ya existe");
                    }
                } while(nombre.isEmpty() || SistemaArchivos.grafoExiste(nombre));
                grafo.setNombre(nombre);

                new VentanaGrafo(grafo);

                ventana.setVisible(false);
                ventana.dispose();
            }
        });

        abrirGrafo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] opciones = new String[SistemaArchivos.grafos.size()];
                for (int i = 0; i < opciones.length; i++) {
                    opciones[i] = SistemaArchivos.grafos.get(i).getNombre();
                }
                JComboBox comboBox = new JComboBox(opciones); comboBox.setSelectedIndex(-1);
                JOptionPane.showMessageDialog(null, comboBox, "Seleccion grafo a abrir:",
                        JOptionPane.QUESTION_MESSAGE);
                ventana.add(comboBox);

                int abrir = comboBox.getSelectedIndex();
                if(abrir != -1){
                    new VentanaGrafo(SistemaArchivos.grafos.get(abrir));
                    ventana.setVisible(false);
                    ventana.dispose();
                }
            }
        });

        ventana.add(nuevoGrafo);
        ventana.add(nuevoGrafoAleatorio);
        ventana.add(abrirGrafo);
        ventana.setVisible(true);
    }
}
