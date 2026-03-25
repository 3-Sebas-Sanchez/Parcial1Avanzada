/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel que  Muestra la ruta del archivo de propiedades cargado
 *
 * @author Nath
 */
public class PanelCarga extends JPanel {

    private JLabel lblRutaArchivo;
    private JLabel lblEstado;

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
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Configurar el fondo
        this.setBackground(new Color(240, 248, 255)); // Alice Blue

        // ===== ETIQUETA DE ESTADO =====
        lblEstado = new JLabel(" Archivo de Properties Cargado:");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstado.setForeground(new Color(25, 25, 112)); // Midnight Blue
        this.add(lblEstado, BorderLayout.WEST);

        // ===== ETIQUETA DE RUTA =====
        lblRutaArchivo = new JLabel("Esperando selección de archivo...");
        lblRutaArchivo.setFont(new Font("Courier New", Font.PLAIN, 11));
        lblRutaArchivo.setForeground(new Color(34, 139, 34)); // Forest Green
        lblRutaArchivo.setBorder(BorderFactory.createLineBorder(new Color(169, 169, 169)));
        lblRutaArchivo.setOpaque(true);
        lblRutaArchivo.setBackground(new Color(255, 255, 255));
        lblRutaArchivo.setHorizontalAlignment(SwingConstants.LEFT);
        lblRutaArchivo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(169, 169, 169)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        this.add(lblRutaArchivo, BorderLayout.CENTER);
    }

    /**
     * Retorna la etiqueta de ruta del archivo
     * @return JLabel lblRutaArchivo
     */
    public JLabel getLblRutaArchivo() {
        return lblRutaArchivo;
    }

    /**
     * Actualiza la ruta mostrada en el panel
     * @param ruta Nueva ruta a mostrar
     */
    public void actualizarRuta(String ruta) {
        if (ruta != null && !ruta.isEmpty()) {
            lblRutaArchivo.setText(ruta);
            lblRutaArchivo.setForeground(new Color(34, 139, 34)); // Verde
        } else {
            lblRutaArchivo.setText("No se ha cargado archivo");
            lblRutaArchivo.setForeground(new Color(178, 34, 34)); // Rojo
        }
    }
}