/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de Fondo - Contenedor principal con diseño personalizado
 * Organiza los otros paneles en una disposición coherente
 *
 * @author Nath
 */
public class PanelFondo extends JPanel {

    /**
     * Constructor que inicializa el panel
     */
    public PanelFondo() {
        inicializarComponentes();
    }

    /**
     * Inicializa todos los componentes visuales del panel
     */
    private void inicializarComponentes() {
        // Configurar el layout principal de la ventana
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurar el fondo con un color suave
        this.setBackground(new Color(245, 245, 220)); // Beige claro
    }

    /**
     * Agrega el panel de carga (Norte)
     * 
     * @param panelCarga Panel de configuración/carga
     */
    public void agregarPanelCarga(PanelCarga panelCarga) {
        this.add(panelCarga, BorderLayout.NORTH);
    }

    /**
     * Agrega el panel de formulario (Oeste)
     * 
     * @param panelFormulario Panel de datos del luchador
     */
    public void agregarPanelFormulario(PanelFormulario panelFormulario) {
        this.add(panelFormulario, BorderLayout.WEST);
    }

    /**
     * Agrega el panel de kimarites (Centro)
     * 
     * @param panelKimarites Panel de técnicas
     */
    public void agregarPanelKimarites(PanelKinarites panelKimarites) {
        this.add(panelKimarites, BorderLayout.CENTER);
    }

    /**
     * Agrega el panel de conexión (Sur)
     * 
     * @param panelConectar Panel de botones
     */
    public void agregarPanelConectar(PanelConectar panelConectar) {
        this.add(panelConectar, BorderLayout.SOUTH);
    }
}