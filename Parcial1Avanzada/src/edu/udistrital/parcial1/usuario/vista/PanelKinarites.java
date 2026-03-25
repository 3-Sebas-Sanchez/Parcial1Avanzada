/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel de Kimarites - Técnicas de lucha disponibles
 * Ubicación: Centro-Este (East) de la ventana
 * Contiene pestañas con las diferentes categorías de técnicas
 *
 * @author Nath
 */
public class PanelKinarites extends JPanel {

    private JTabbedPane pestanasTecnicas;
    private List<JCheckBox> listaCasillasTecnicas;

    /**
     * Constructor que inicializa el panel con sus componentes
     */
    public PanelKinarites() {
        inicializarComponentes();
    }

    /**
     * Inicializa todos los componentes visuales del panel
     */
    private void inicializarComponentes() {
        // Configurar el layout
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurar el fondo
        this.setBackground(new Color(240, 248, 255)); // Alice Blue

        // Crear la etiqueta de título
        JLabel lblTitulo = new JLabel("Paso 3: Selecciona tus Kimarites (Técnicas)");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 13));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(25, 25, 112)); // Midnight Blue
        this.add(lblTitulo, BorderLayout.NORTH);

        // Crear el panel con pestañas
        pestanasTecnicas = new JTabbedPane();
        pestanasTecnicas.setFont(new Font("Arial", Font.PLAIN, 11));
        pestanasTecnicas.setBackground(new Color(255, 255, 255));
        
        this.add(pestanasTecnicas, BorderLayout.CENTER);

        // Inicializar la lista de checkboxes
        this.listaCasillasTecnicas = new ArrayList<>();
    }

    /**
     * Crea una nueva pestaña para una categoría de técnicas
     * 
     * @param nombreCategoria Nombre de la categoría (ej: "BÁSICAS")
     */
    public void crearPestana(String nombreCategoria) {
        // Crear un panel con scroll para la pestaña
        JPanel panelCategoria = new JPanel();
        panelCategoria.setLayout(new BoxLayout(panelCategoria, BoxLayout.Y_AXIS));
        panelCategoria.setBackground(new Color(255, 255, 255));
        panelCategoria.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crear scroll pane
        JScrollPane scrollPane = new JScrollPane(panelCategoria);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Agregar la pestaña
        pestanasTecnicas.addTab(nombreCategoria, scrollPane);
        
        // Guardar referencia al panel para poder agregar checkboxes después
        panelCategoria.putClientProperty("panelPrincipal", panelCategoria);
    }

    /**
     * Llena la última pestaña creada con los kimarites (checkboxes)
     * 
     * @param kimarites Array de técnicas a mostrar
     */
    public void llenarTecnicasEnUltimaPestana(String[] kimarites) {
        if (pestanasTecnicas.getTabCount() == 0) {
            return;
        }

        // Obtener la última pestaña
        int indexUltimaPestana = pestanasTecnicas.getTabCount() - 1;
        JScrollPane scrollPane = (JScrollPane) pestanasTecnicas.getComponentAt(indexUltimaPestana);
        JPanel panelCategoria = (JPanel) scrollPane.getViewport().getView();

        // Limpiar el panel antes de llenar
        panelCategoria.removeAll();

        // Agregar checkboxes para cada kimarite
        if (kimarites != null && kimarites.length > 0) {
            for (String kimarite : kimarites) {
                JCheckBox checkBox = new JCheckBox(kimarite);
                checkBox.setFont(new Font("Arial", Font.PLAIN, 10));
                checkBox.setBackground(new Color(255, 255, 255));
                checkBox.setFocusPainted(false);
                checkBox.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                
                panelCategoria.add(checkBox);
                listaCasillasTecnicas.add(checkBox);
            }

            // Agregar espacio flexible al final
            panelCategoria.add(Box.createVerticalGlue());
        }

        // Refrescar el panel
        panelCategoria.revalidate();
        panelCategoria.repaint();
    }

    /**
     * Limpia todas las pestañas y reinicia la lista de checkboxes
     */
    public void limpiarPestanas() {
        pestanasTecnicas.removeAll();
        listaCasillasTecnicas.clear();
    }

    /**
     * Retorna la lista de checkboxes con todas las técnicas
     * @return List<JCheckBox> listaCasillasTecnicas
     */
    public List<JCheckBox> getListaCasillasTecnicas() {
        return listaCasillasTecnicas;
    }

    /**
     * Retorna el panel con pestañas
     * @return JTabbedPane pestanasTecnicas
     */
    public JTabbedPane getPestanasTecnicas() {
        return pestanasTecnicas;
    }
}