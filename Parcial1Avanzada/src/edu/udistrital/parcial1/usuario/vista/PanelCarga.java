/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel de Configuración - Carga del archivo de propiedades
 * Ubicación: Norte (Top) de la ventana
 *
 * @author Nath
 */
public class PanelCarga extends JPanel {

    private JButton btnCargarArchivo;
    private JLabel lblRutaArchivo;

    /**
     * Constructor que inicializa el panel con sus componentes
     */
    public PanelCarga() {
        inicializarComponentes();
    }

    /**
     * Inicializa todos los componentes visuales del panel
     */
    private void inicializarComponentes() {
        // Configurar el layout
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        
        // Configurar el borde con título
        TitledBorder borde = new TitledBorder("Carga Kimarites");
        borde.setTitleFont(new Font("Arial", Font.BOLD, 12));
        this.setBorder(borde);
        
        // Configurar el fondo
        this.setBackground(new Color(240, 248, 255)); // Alice Blue

        // Crear el botón de cargar archivo
        btnCargarArchivo = new JButton(" Cargar Archivo de Tecnicas");
        btnCargarArchivo.setFont(new Font("Arial", Font.BOLD, 12));
        btnCargarArchivo.setBackground(new Color(65, 105, 225)); // Royal Blue
        btnCargarArchivo.setForeground(Color.WHITE);
        btnCargarArchivo.setFocusPainted(false);
        btnCargarArchivo.setPreferredSize(new Dimension(200, 35));
        btnCargarArchivo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Crear la etiqueta de ruta
        lblRutaArchivo = new JLabel("No se ha cargado archivo");
        lblRutaArchivo.setFont(new Font("Arial", Font.ITALIC, 11));
        lblRutaArchivo.setForeground(new Color(70, 70, 70));

        // Agregar componentes al panel
        this.add(btnCargarArchivo);
        this.add(lblRutaArchivo);
    }

    /**
     * Retorna el botón de cargar archivo
     * @return JButton btnCargarArchivo
     */
    public JButton getBtnCargarArchivo() {
        return btnCargarArchivo;
    }

    /**
     * Retorna la etiqueta de ruta del archivo
     * @return JLabel lblRutaArchivo
     */
    public JLabel getLblRutaArchivo() {
        return lblRutaArchivo;
    }
}