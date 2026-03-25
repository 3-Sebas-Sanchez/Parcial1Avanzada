/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel del Formulario - Datos del Luchador (Rikishi)
 * Ubicación: Centro-Oeste (West) de la ventana
 *
 * @author Nath
 */
public class PanelFormulario extends JPanel {

    private JTextField txtNombre;
    private JTextField txtPeso;
    private JTextField txtCombates;

    /**
     * Constructor que inicializa el panel con sus componentes
     */
    public PanelFormulario() {
        inicializarComponentes();
    }

    /**
     * Inicializa todos los componentes visuales del panel
     */
    private void inicializarComponentes() {
        // Configurar el layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configurar el borde con título
        TitledBorder borde = new TitledBorder("Paso 2: Datos del Rikishi");
        borde.setTitleFont(new Font("Arial", Font.BOLD, 12));
        this.setBorder(borde);

        // Configurar el fondo
        this.setBackground(new Color(240, 248, 255)); // Alice Blue

        // ===== CAMPO NOMBRE =====
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblNombre = new JLabel(" Nombre:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 11));
        this.add(lblNombre, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        txtNombre = new JTextField(15);
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 11));
        txtNombre.setBackground(Color.WHITE);
        txtNombre.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        this.add(txtNombre, gbc);

        // ===== CAMPO PESO =====
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblPeso = new JLabel(" Peso (kg):");
        lblPeso.setFont(new Font("Arial", Font.BOLD, 11));
        this.add(lblPeso, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.7;
        txtPeso = new JTextField(15);
        txtPeso.setFont(new Font("Arial", Font.PLAIN, 11));
        txtPeso.setBackground(Color.WHITE);
        txtPeso.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        this.add(txtPeso, gbc);

        // ===== CAMPO COMBATES =====
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblCombates = new JLabel(" Combates Ganados:");
        lblCombates.setFont(new Font("Arial", Font.BOLD, 11));
        this.add(lblCombates, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.7;
        
        
        // Ponemos un "0" inicial 
        txtCombates = new JTextField("0", 15); 
        txtCombates.setFont(new Font("Arial", Font.PLAIN, 11));
        
        // Bloqueamos el campo
        txtCombates.setEditable(false); 
        
        // L un fondo gris 
        txtCombates.setBackground(new Color(230, 230, 230)); 
        
        txtCombates.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        
        this.add(txtCombates, gbc);
    }

    /**
     * Retorna el campo de texto del nombre
     * @return JTextField txtNombre
     */
    public JTextField getTxtNombre() {
        return txtNombre;
    }

    /**
     * Retorna el campo de texto del peso
     * @return JTextField txtPeso
     */
    public JTextField getTxtPeso() {
        return txtPeso;
    }

    /**
     * Retorna el campo de texto de combates ganados
     * @return JTextField txtCombates
     */
    public JTextField getTxtCombates() {
        return txtCombates;
    }

    /**
     * Limpia todos los campos del formulario
     */
    public void limpiarFormulario() {
        txtNombre.setText("");
        txtPeso.setText("");
        txtCombates.setText("");
    }
}