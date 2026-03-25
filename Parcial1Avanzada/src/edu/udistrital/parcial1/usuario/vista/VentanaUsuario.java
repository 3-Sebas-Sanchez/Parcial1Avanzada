/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.vista;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana Principal del Cliente (Rikishi)
 * Organiza todos los paneles de la interfaz gráfica
 *
 * @author Nath
 */
public class VentanaUsuario extends JFrame {

    private PanelFondo panelFondo;
    private PanelCarga panelCarga;
    private PanelFormulario panelFormulario;
    private PanelKinarites panelKimarites;
    private PanelConectar panelConectar;

    /**
     * Constructor que inicializa la ventana y sus componentes
     */
    public VentanaUsuario() {
        inicializarVentana();
        inicializarPaneles();
        organizarLayout();
    }

    /**
     * Configura las propiedades principales de la ventana
     */
    private void inicializarVentana() {
        this.setTitle(" TORNEO DE SUMO ");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setIconImage(new ImageIcon("sumo.png").getImage()); // Opcional
    }

    /**
     * Inicializa todos los paneles
     */
    private void inicializarPaneles() {
        panelFondo = new PanelFondo();
        panelCarga = new PanelCarga();
        panelFormulario = new PanelFormulario();
        panelKimarites = new PanelKinarites();
        panelConectar = new PanelConectar();
    }

    /**
     * Organiza los paneles en la ventana
     */
    private void organizarLayout() {
        panelFondo.agregarPanelCarga(panelCarga);
        panelFondo.agregarPanelFormulario(panelFormulario);
        panelFondo.agregarPanelKimarites(panelKimarites);
        panelFondo.agregarPanelConectar(panelConectar);

        this.setContentPane(panelFondo);
    }

    // ===== GETTERS DE COMPONENTES =====

    public JButton getBtnCargarArchivo() {
        return panelCarga.getBtnCargarArchivo();
    }

    public JLabel getLblRutaArchivo() {
        return panelCarga.getLblRutaArchivo();
    }

    public JTextField getTxtNombre() {
        return panelFormulario.getTxtNombre();
    }

    public JTextField getTxtPeso() {
        return panelFormulario.getTxtPeso();
    }

    public JTextField getTxtCombates() {
        return panelFormulario.getTxtCombates();
    }

    public JButton getBtnPelear() {
        return panelConectar.getBtnEntrarAlDohyo();
    }

    public List<JCheckBox> getListaCasillasTecnicas() {
        return new ArrayList<>(panelKimarites.getListaCasillasTecnicas());
    }

    // ===== MÉTODOS DE GESTIÓN DE PANELES =====

    public void crearPestanaCategoria(String nombreCategoria) {
        panelKimarites.crearPestana(nombreCategoria);
    }

    public void llenarTecnicasEnUltimaPestana(String[] kimarites) {
        panelKimarites.llenarTecnicasEnUltimaPestana(kimarites);
    }

    public void limpiarPestanas() {
        panelKimarites.limpiarPestanas();
    }

    // ===== MÉTODOS DE DIÁLOGOS =====

    public String abrirSelectorArchivo(String titulo) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(titulo);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrarResultado(String resultado) {
        JOptionPane.showMessageDialog(this, resultado, "RESULTADO DEL COMBATE ", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarVentana() {
        this.setVisible(true);
    }
}