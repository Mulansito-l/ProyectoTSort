import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxCell;
import javax.swing.*;

public class VentanaGrafo {
    private JFrame ventana;
    private JPanel panelControl;
    private JPanel panelDibujo;
    private JPanel panelGrafo;
    private JPanel panelMatriz;
    private JPanel panelLista;
    private JLabel textoArista;
    private JLabel textoA;
    private JTextField textoArista1;
    private JTextField textoArista2;
    private JButton botonArista;
    private JButton botonVertice;
    private JLabel resultadoSort;
    private JButton botonSort;
    private JButton botonGuardar;
    private JButton botonRegresar;
    private JTextArea titulo;
    private GrafoDirigidoAciclico grafo;
    private mxGraph grafoDibujo;
    private mxGraphComponent grafoComponent;

    public VentanaGrafo(GrafoDirigidoAciclico grafo){
        this.grafo = grafo;
        grafoDibujo = new mxGraph();
        grafoComponent = new mxGraphComponent(grafoDibujo);
        grafoComponent.setBounds(0,0,900,350);

        ventana = new JFrame("Proyecto final by Anita & Diego");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setBounds(0, 0, 1200, 700);

        panelControl = new JPanel();
        panelControl.setLayout(null);
        panelControl.setBounds(900, 0, 300, 700);
        panelControl.setBackground(new Color(157, 121, 185));

        panelDibujo = new JPanel();
        panelDibujo.setLayout(new GridBagLayout());
        panelDibujo.setBounds(0,0,900,700);

        panelMatriz = new JPanel();
        panelMatriz.setBounds(0,0,350,350);
        panelMatriz.setBackground(new Color(255, 198, 148));

        panelLista = new JPanel();
        panelLista.setBounds(350,0,350,350);
        panelLista.setBackground(new Color(121, 185, 183));

        panelGrafo = new JPanel();
        panelGrafo.setBackground(new Color(185, 220, 158));
        panelGrafo.setBounds(0,0,900,350);
        panelGrafo.add(grafoComponent);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        panelDibujo.add(panelMatriz, c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        panelDibujo.add(panelLista, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;

        panelDibujo.add(panelGrafo, c);

        ventana.add(panelDibujo);

        //agrego el contenido del panel de control

        titulo = new JTextArea("Proyecto Topological Sort");
        titulo.setLineWrap(true);
        titulo.setWrapStyleWord(true);
        titulo.setLineWrap(true);
        titulo.setOpaque(false);
        titulo.setEditable(false);
        titulo.setFocusable(false);
        titulo.setBackground(null);
        titulo.setBounds(950,50,200,20);
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        panelControl.add(titulo);

        botonVertice = new JButton("Crear vértice");
        botonVertice.setBounds(950,100,150,40);
        panelControl.add(botonVertice);

        botonVertice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grafo.nuevoVertice();
                actualizarDibujoGrafo();
            }
        });

        textoArista = new JLabel("Arista de:");
        textoArista.setBounds(950,200,200,20);
        textoArista.setFont(new Font("Arial", Font.BOLD, 12));
        panelControl.add(textoArista);

        textoArista1 = new JTextField();
        textoArista1.setBounds(950,240,200,20);
        panelControl.add(textoArista1);

        textoA = new JLabel("A:");
        textoA.setBounds(950,400,280,20);
        textoA.setFont(new Font("Arial", Font.BOLD, 12));
        panelControl.add(textoA);

        textoArista2 = new JTextField();
        textoArista2.setBounds(950,320,200,20);
        panelControl.add(textoArista2);

        botonArista = new JButton("Crear arista");
        botonArista.setBounds(950,360,150,40);
        panelControl.add(botonArista);

        botonArista.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(grafo.isLetras()){
                    try {
                        grafo.insertarArista((int) textoArista1.getText().charAt(0), (int) textoArista2.getText().charAt(0));
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(ventana,"No es una arista válida");
                    }
                } else{
                    try {
                        grafo.insertarArista(Integer.parseInt(textoArista1.getText()), Integer.parseInt(textoArista2.getText()));
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(ventana,"No es una arista válida");
                    }
                }
                actualizarDibujoGrafo();
            }
        });

        resultadoSort = new JLabel();
        botonSort = new JButton("Topological Sort");
        botonGuardar = new JButton("Guardar");
        botonRegresar = new JButton("Regresar");


        ventana.add(panelControl);

        ventana.setVisible(true);
    }

    void actualizarDibujoGrafo(){
        HashMap<String, Object> verticesCeldas = new HashMap<String,Object>();
        panelGrafo.remove(grafoComponent);
        Object parent = grafoDibujo.getDefaultParent();
        ArrayList<Vertice> vertices = grafo.getListaAdyacencia();
        grafoDibujo.getModel().beginUpdate();
        try {
            Object[] cells = grafoDibujo.getChildCells(parent);

            for (Object cell : cells) {
                grafoDibujo.removeCells(new Object[]{cell});
            }
        } finally {
            grafoDibujo.getModel().endUpdate();
        }
        for (int i = 0; i < grafo.getListaAdyacencia().size(); i++) {
            Vertice v = vertices.get(i);
            if(!grafo.isLetras()){
                Object v1 = grafoDibujo.insertVertex(parent,null,Integer.toString(v.getNumVertice()),0,0,30,30);
                verticesCeldas.put(Integer.toString(v.getNumVertice()),v1);
            }else{
                Object v1 = grafoDibujo.insertVertex(parent,null,(char) v.getNumVertice(),0,0,30,30);
                verticesCeldas.put(Integer.toString((char) v.getNumVertice()),v1);
            }
        }

        for (int i = 0; i < vertices.size(); i++) {
            Vertice vi = vertices.get(i);
            ArrayList<Vertice> aristas = vi.getAristasSalida();
            for (int j = 0; j < aristas.size(); j++) {
                Vertice vf = aristas.get(j);
                if(!grafo.isLetras()){
                    grafoDibujo.insertEdge(parent, null, null, verticesCeldas.get(Integer.toString(vi.getNumVertice())), verticesCeldas.get(Integer.toString(vf.getNumVertice())));
                }else{
                    grafoDibujo.insertEdge(parent, null, null, verticesCeldas.get(Integer.toString((char)vi.getNumVertice())), verticesCeldas.get(Integer.toString((char)vf.getNumVertice())));
                }
            }
        }
        grafoComponent = new mxGraphComponent(grafoDibujo);
        mxCircleLayout layout = new mxCircleLayout(grafoDibujo);
        layout.execute(parent);
        panelGrafo.add(grafoComponent);
        panelGrafo.revalidate();
        panelGrafo.repaint();
    }
}
