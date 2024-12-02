import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.model.mxCell;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    private mxGraph grafoLista;
    private mxGraphComponent listaComponent;
    private JTable matrizAdyacencia;
    private JLabel tituloMatriz;
    private JLabel tituloLista;

    public VentanaGrafo(GrafoDirigidoAciclico grafo){
        this.grafo = grafo;
        grafoDibujo = new mxGraph();
        grafoComponent = new mxGraphComponent(grafoDibujo);
        grafoComponent.setPreferredSize(new Dimension(900,300));
        grafoComponent.getViewport().setBackground(new Color(185, 220, 158));
        grafoComponent.setBorder(null);

        grafoLista = new mxGraph();
        listaComponent = new mxGraphComponent(grafoLista);
        listaComponent.setPreferredSize(new Dimension(900,300));
        listaComponent.getViewport().setBackground(new Color(121, 185, 183));
        listaComponent.setBorder(null);

        ventana = new JFrame("Proyecto final by Anita & Diego");
        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                new VentanaPrincipal();
            }
        });
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
        panelMatriz.setBounds(0,0,450,350);
        panelMatriz.setBackground(new Color(255, 198, 148));
        tituloMatriz = new JLabel("Matriz de adyacencia");
        tituloMatriz.setBounds(200,10,200,20);
        panelMatriz.add(tituloMatriz);

        panelLista = new JPanel();
        panelLista.setBounds(450,0,450,350);
        panelLista.setBackground(new Color(121, 185, 183));
        panelLista.add(listaComponent);
        tituloLista = new JLabel("Lista de adyacencia");
        tituloLista.setBounds(650,10,200,20);
        panelLista.add(tituloLista);

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

        DefaultTableModel model = new DefaultTableModel();
        matrizAdyacencia = new JTable(model);
        matrizAdyacencia.setBounds(0,0,450,350);
        matrizAdyacencia.setBorder(null);
        JScrollPane sp = new JScrollPane(matrizAdyacencia);
        sp.getViewport().setBackground(new Color(255, 198, 148));
        panelMatriz.add(sp);

        //agrego el contenido del panel de control

        titulo = new JTextArea("Proyecto Topological Sort");
        titulo.setForeground(Color.WHITE);
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
        botonVertice.setBackground(new Color(100,51,152));
        botonVertice.setForeground(Color.white);
        botonVertice.setBounds(950,100,150,40);
        panelControl.add(botonVertice);

        botonVertice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grafo.nuevoVertice();
                actualizarDibujoGrafo();
                actualizarMatrizAdyacencia();
                actualizarListaAdyacencia();
            }
        });

        textoArista = new JLabel("Arista de:");
        textoArista.setBounds(950,160,200,20);
        textoArista.setFont(new Font("Arial", Font.BOLD, 12));
        textoArista.setForeground(Color.white);
        panelControl.add(textoArista);

        textoArista1 = new JTextField();
        textoArista1.setBounds(950,200,200,20);
        panelControl.add(textoArista1);

        textoA = new JLabel("A:");
        textoA.setBounds(950,240,200,20);
        textoA.setFont(new Font("Arial", Font.BOLD, 12));
        textoA.setForeground(Color.white);
        panelControl.add(textoA);

        textoArista2 = new JTextField();
        textoArista2.setBounds(950,280,200,20);
        panelControl.add(textoArista2);

        botonArista = new JButton("Crear arista");
        botonArista.setBackground(new Color(100,51,152));
        botonArista.setForeground(Color.white);
        botonArista.setBounds(950,320,150,40);
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
                actualizarMatrizAdyacencia();
                actualizarListaAdyacencia();
            }
        });

        botonSort = new JButton("Topological Sort");
        botonSort.setBounds(950,380,200,20);
        botonSort.setBackground(new Color(100,51,152));
        botonSort.setForeground(Color.white);
        panelControl.add(botonSort);

        botonSort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultadoSort.setText(grafo.topologicalSort());
            }
        });

        resultadoSort = new JLabel();
        resultadoSort.setBounds(950,420,200,20);
        resultadoSort.setForeground(Color.white);
        panelControl.add(resultadoSort);

        botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(950, 460, 200,20);
        botonGuardar.setBackground(new Color(100,51,152));
        botonGuardar.setForeground(Color.white);
        panelControl.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuardarGrafo(grafo);
            }
        });

        botonRegresar = new JButton("Regresar");
        botonRegresar.setBounds(950, 500, 200,20);
        botonRegresar.setBackground(new Color(100,51,152));
        botonRegresar.setForeground(Color.white);
        panelControl.add(botonRegresar);

        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
                ventana.dispose();
                new VentanaPrincipal();
            }
        });

        ventana.add(panelControl);
        ventana.setVisible(true);

        actualizarDibujoGrafo();
        actualizarMatrizAdyacencia();
        actualizarListaAdyacencia();
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
                verticesCeldas.put(Character.toString((char) v.getNumVertice()),v1);
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
                    grafoDibujo.insertEdge(parent, null, null, verticesCeldas.get(Character.toString((char) vi.getNumVertice())), verticesCeldas.get(Character.toString((char) vf.getNumVertice())));
                }
            }
        }
        grafoComponent = new mxGraphComponent(grafoDibujo);
        grafoComponent.setPreferredSize(new Dimension(900,300));
        mxCircleLayout layout = new mxCircleLayout(grafoDibujo);
        layout.setX0(320);
        layout.setY0(50);
        layout.setRadius(80);
        layout.execute(parent);
        grafoComponent.getViewport().setBackground(new Color(185, 220, 158));
        grafoComponent.setBorder(null);
        panelGrafo.add(grafoComponent);
        panelGrafo.setMaximumSize(new Dimension(900,350));
        panelGrafo.revalidate();
        panelGrafo.repaint();
    }

    void actualizarMatrizAdyacencia(){
        Integer[][] matrizAdy = grafo.getMatrizAdyacencia();
        DefaultTableModel model;
        model = new DefaultTableModel(matrizAdy, matrizAdy[0]);
        matrizAdyacencia.getTableHeader().setUI(null);
        matrizAdyacencia.setModel(model);
        matrizAdyacencia.repaint();
    }

    void actualizarListaAdyacencia(){
        HashMap<Object, Object> verticesCeldas = new HashMap<Object,Object>();
        panelLista.remove(listaComponent);
        Object parent = grafoLista.getDefaultParent();
        ArrayList<Vertice> vertices = grafo.getListaAdyacencia();
        grafoLista.getModel().beginUpdate();
        try {
            Object[] cells = grafoLista.getChildCells(parent);
            for (Object cell : cells) {
                grafoLista.removeCells(new Object[]{cell});
            }
        } finally {
            grafoLista.getModel().endUpdate();
        }

        int x = 20;
        int y = 20;

        for (int i = 0; i < grafo.getListaAdyacencia().size(); i++) {
            Vertice v = vertices.get(i);
            Object v1 = grafoLista.insertVertex(parent,null,v,x,y + (30 * i),30,30);
            verticesCeldas.put(v,v1);

            ArrayList<Vertice> aristas = v.getAristasSalida();
            Vertice vi = v;
            for (int j = 0; j < aristas.size(); j++) {
                Vertice vf = aristas.get(j);
                Object v2 = grafoLista.insertVertex(parent,null,vf,x + (40 * (j + 1)),y + (30 * i),30,30);
                verticesCeldas.put(vf,v2);
                grafoLista.insertEdge(parent, null, null, verticesCeldas.get(vi), verticesCeldas.get(vf));
                vi = vf;
            }
        }

        listaComponent = new mxGraphComponent(grafoLista);
        listaComponent.setPreferredSize(new Dimension(450,350));
        listaComponent.getViewport().setBackground(new Color(121, 185, 183));
        listaComponent.setBorder(null);
        panelLista.add(listaComponent);
        panelLista.setMaximumSize(new Dimension(900,350));
        panelLista.revalidate();
        panelLista.repaint();
    }

    void GuardarGrafo(GrafoDirigidoAciclico grafo){
        SistemaArchivos.guardarGrafo(grafo);
    }
}
