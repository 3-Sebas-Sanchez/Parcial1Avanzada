/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel de Botones - Acciones principales (Conectar y Pelear)
 * Ubicación: Sur (Bottom) de la ventana
 *
 * @author Nath
 */
public class PanelConectar extends JPanel {

    private JButton btnEntrarAlDohyo;
    private JLabel lblEstado;

    /**
     * Constructor que inicializa el panel con sus componentes
     */
    public PanelConectar() {
        inicializarComponentes();
    }

    /**
     * Inicializa todos los componentes visuales del panel
     */
    private void inicializarComponentes() {
        // Configurar el layout principal
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurar el fondo con un gradiente simulado
        this.setBackground(new Color(220, 20, 60)); // Crimson

        // ===== BOTÓN PRINCIPAL =====
        btnEntrarAlDohyo = new JButton(" ¡ENTRAR AL DOHYŌ!");
        btnEntrarAlDohyo.setFont(new Font("Arial", Font.BOLD, 14));
        btnEntrarAlDohyo.setBackground(new Color(255, 69, 0)); // Orange Red
        btnEntrarAlDohyo.setForeground(Color.WHITE);
        btnEntrarAlDohyo.setFocusPainted(false);
        btnEntrarAlDohyo.setPreferredSize(new Dimension(400, 50));
        btnEntrarAlDohyo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrarAlDohyo.setBorder(BorderFactory.createRaisedBevelBorder());

        // Agregar efecto visual al pasar el mouse
        btnEntrarAlDohyo.setRolloverEnabled(true);

        // ===== LABEL DE ESTADO =====
        lblEstado = new JLabel("⏳ Esperando selección de archivo...");
        lblEstado.setFont(new Font("Arial", Font.ITALIC, 11));
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setHorizontalAlignment(SwingConstants.CENTER);

        // ===== PANEL CONTENEDOR DEL BOTÓN =====
        JPanel panelBoton = new JPanel();
        panelBoton.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(220, 20, 60));
        panelBoton.add(btnEntrarAlDohyo);

        // Agregar componentes al panel principal
        this.add(panelBoton, BorderLayout.CENTER);
        this.add(lblEstado, BorderLayout.SOUTH);
    }

    /**
     * Retorna el botón de entrada al Dohyō
     * @return JButton btnEntrarAlDohyo
     */
    public JButton getBtnEntrarAlDohyo() {
        return btnEntrarAlDohyo;
    }

    /**
     * Retorna la etiqueta de estado
     * @return JLabel lblEstado
     */
    public JLabel getLblEstado() {
        return lblEstado;
    }

    /**
     * Actualiza el mensaje de estado
     * 
     * @param nuevoEstado Nuevo mensaje de estado
     */
    public void actualizarEstado(String nuevoEstado) {
        lblEstado.setText(nuevoEstado);
        lblEstado.revalidate();
        lblEstado.repaint();
    }

    /**
     * Habilita o deshabilita el botón
     * 
     * @param habilitado true para habilitar, false para deshabilitar
     */
    public void habilitarBoton(boolean habilitado) {
        btnEntrarAlDohyo.setEnabled(habilitado);
    }
}