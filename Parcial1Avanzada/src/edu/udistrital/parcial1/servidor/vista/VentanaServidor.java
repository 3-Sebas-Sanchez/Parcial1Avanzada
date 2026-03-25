/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.vista;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * @author sebas
 */
public class VentanaServidor extends JFrame{
    // Panel que contiene el GIF del estado actual del Dohyō.
    private JPanel panelIzquierdo;
 
    // Label que muestra el GIF animado del estado del servidor.
    private JLabel lblGif;
 
    // Panel derecho que agrupa la lista de luchadores y los resultados.
    private JPanel panelDerecho;
 
    // Modelo de la lista de luchadores conectados.
    private DefaultListModel<String> modeloLuchadores;
 
    // Lista visual de luchadores registrados.
    private JList<String> listaLuchadores;
 
    // Área de texto con los resultados de cada combate.
    private JTextArea areaResultados;
 
    // GIF mostrado mientras el servidor espera luchadores.
    private static final String GIF_INICIO = "ImagesAndGifs/sumoInicio.gif";
 
    // GIF mostrado cuando se conecta un nuevo luchador.
    private static final String GIF_ENTRADA = "ImagesAndGifs/sumoEntrada.gif";
 
    // GIF mostrado durante el combate activo.
    private static final String GIF_PELEA = "ImagesAndGifs/sumoPelea.gif";
 
    public VentanaServidor() {
        initComponentes();
    }
 
    /**
     * Inicializa, configura y organiza todos los componentes de la ventana.
     */
    private void initComponentes() {
 
        // Configuración del JFrame
        setTitle("Servidor de Sumo");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 20, 10));
 
        //Panel izquierdo
        panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setPreferredSize(new Dimension(420, 600));
        panelIzquierdo.setBackground(new Color(30, 20, 10));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
 
        lblGif = new JLabel();
        lblGif.setHorizontalAlignment(JLabel.CENTER);
        lblGif.setVerticalAlignment(JLabel.CENTER);
        cambiarGif(GIF_INICIO);
        panelIzquierdo.add(lblGif, BorderLayout.CENTER);
 
        //Panel derecho
        panelDerecho = new JPanel(new GridLayout(2, 1, 0, 8));
        panelDerecho.setBackground(new Color(30, 20, 10));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
 
        // Subpanel: lista de luchadores conectados
        JPanel panelLuchadores = new JPanel(new BorderLayout());
        panelLuchadores.setBackground(new Color(45, 30, 15));
        panelLuchadores.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 50), 2),
                "⚔  Luchadores Registrados",
                0, 0,
                new Font("SansSerif", Font.BOLD, 13),
                new Color(200, 150, 50)));
 
        modeloLuchadores = new DefaultListModel<>();
        listaLuchadores = new JList<>(modeloLuchadores);
        listaLuchadores.setBackground(new Color(45, 30, 15));
        listaLuchadores.setForeground(new Color(230, 210, 170));
        listaLuchadores.setFont(new Font("Monospaced", Font.PLAIN, 13));
        listaLuchadores.setSelectionBackground(new Color(120, 80, 20));
        panelLuchadores.add(new JScrollPane(listaLuchadores), BorderLayout.CENTER);
 
        // Subpanel: resultados de combates
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBackground(new Color(45, 30, 15));
        panelResultados.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 50), 2),
                "🏆  Resultados de Combates",
                0, 0,
                new Font("SansSerif", Font.BOLD, 13),
                new Color(200, 150, 50)));
 
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setBackground(new Color(45, 30, 15));
        areaResultados.setForeground(new Color(230, 210, 170));
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);
        panelResultados.add(new JScrollPane(areaResultados), BorderLayout.CENTER);
 
        panelDerecho.add(panelLuchadores);
        panelDerecho.add(panelResultados);
 
        //Ensamblar ventana
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);
 
        setVisible(true);
    }
 
    /**
     * Agrega un luchador a la lista visible de conectados.
     * Seguro para llamar desde hilos externos.
     *
     * @param nombreLuchador Nombre del luchador a mostrar en la lista
     */
    public void agregarLuchadorLista(String nombreLuchador) {
        SwingUtilities.invokeLater(() ->
                modeloLuchadores.addElement("🥋 " + nombreLuchador));
    }
 
    /**
     * Agrega una línea al área de resultados de combates.
     * Seguro para llamar desde hilos externos.
     *
     * @param mensaje Texto a agregar en el área de resultados
     */
    public void agregarResultado(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaResultados.append(mensaje + "\n");
            areaResultados.setCaretPosition(areaResultados.getDocument().getLength());
        });
    }
 
    /**
     * Cambia el GIF mostrado en el panel izquierdo según el estado del servidor.
     * Seguro para llamar desde hilos externos.
     *
     * @param rutaGif Ruta relativa al archivo GIF a mostrar
     */
    public void cambiarGif(String rutaGif) {
        SwingUtilities.invokeLater(() -> {
            ImageIcon icon = new ImageIcon(rutaGif);
            lblGif.setIcon(icon);
        });
    }
 
    /**
     * Activa el GIF de espera (sumoInicio) — estado inicial del servidor.
     */
    public void mostrarGifInicio() {
        cambiarGif(GIF_INICIO);
    }
 
    /**
     * Activa el GIF de entrada — cuando llega un nuevo luchador.
     */
    public void mostrarGifEntrada() {
        cambiarGif(GIF_ENTRADA);
    }
 
    /**
     * Activa el GIF de pelea — durante un combate activo.
     */
    public void mostrarGifPelea() {
        cambiarGif(GIF_PELEA);
    }
 
/**
     * Abre un {@link JFileChooser} para que el usuario seleccione
     * un archivo {@code .properties}.
     * Este método vive en la Vista porque involucra un componente Swing.
     * Obliga a iniciar la búsqueda desde la carpeta "data" del proyecto.
     *
     * @param titulo Título del diálogo selector
     * @return El {@link File} seleccionado, o {@code null} si el usuario canceló
     */
    public File seleccionarProperties(String titulo) {
        // Obtiene la ruta del proyecto actual
        String rutaProyecto = System.getProperty("user.dir");
        
        // Apuntamos a la subcarpeta "data"
        java.io.File carpetaData = new java.io.File(rutaProyecto, "data");
        
        //Le pasa el directorio inicial al FileChooser
        JFileChooser chooser = new JFileChooser(carpetaData);
        
        chooser.setDialogTitle(titulo);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos de propiedades (*.properties)", "properties"));
        
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }
}
