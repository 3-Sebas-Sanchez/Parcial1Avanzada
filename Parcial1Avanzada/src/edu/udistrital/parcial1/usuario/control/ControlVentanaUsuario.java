/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.control;

import edu.udistrital.parcial1.usuario.vista.VentanaUsuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JCheckBox;

/**
 * Controlador de la Vista del Cliente.
 * Delega la creación visual a la ventana y solo gestiona los eventos.
 */
public class ControlVentanaUsuario implements ActionListener {

    private ControlPrincipalUsuario cPrincipal;

    private VentanaUsuario ventana; 

    private String rutaProperties;

    /**
     * Constructor que recibe la inyección del control principal e inicializa la ventana
     *
     * @param cPrincipal ControlPrincipalUsuario
     */
    public ControlVentanaUsuario(ControlPrincipalUsuario cPrincipal) {
        this.cPrincipal = cPrincipal;
        inicializarVentana();
    }

    /**
     * Método que inicializa la ventana y conecta los listeners
     */
    private void inicializarVentana() {
        // La ventana se encarga 
        this.ventana = new VentanaUsuario();

        // Conecta los botones usando los getters de la vista
        ventana.getBtnCargarArchivo().addActionListener(this);
        ventana.getBtnPelear().addActionListener(this);

        ventana.setVisible(true);
    }

    /**
     * Gestiona los eventos generados por los componentes de la interfaz gráfica.
     *
     * @param e Evento
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        // Si presionó el botón de cargar archivo
        if (src == ventana.getBtnCargarArchivo()) {
            cPrincipal.seleccionarProperties();
        } 
        // Si presionó el botón de pelear
        else if (src == ventana.getBtnPelear()) {
            enviarDatosAlPrincipal();
        }
    }

    /**
     * Extrae los datos de la vista y los envía al Controlador Principal
     */
    private void enviarDatosAlPrincipal() {
        // extrae la información
        String nombre = ventana.getTxtNombre().getText();
        String peso = ventana.getTxtPeso().getText();
        String combates = ventana.getTxtCombates().getText();

        // Extrae las técnicas 
        java.util.List<String> tecnicasElegidas = new java.util.ArrayList<>();
        if (ventana.getListaCasillasTecnicas() != null) {
            for (JCheckBox check : ventana.getListaCasillasTecnicas()) {
                if (check.isSelected()) {
                    tecnicasElegidas.add(check.getText());
                }
            }
        }

        // Enviamos todo
        cPrincipal.confirmarTecnicas(tecnicasElegidas.toArray(new String[0]));
        cPrincipal.conectarYPelear(nombre, peso, combates);
    }

    /**
     * Permite seleccionar un archivo properties 
     *
     * @param mensaje Título de la ventana emergente
     * @return Ruta del archivo seleccionado
     */
    public String seleccionar(String mensaje) {
        return ventana.abrirSelectorArchivo(mensaje);
    }

    /**
     * Establece la ruta del archivo properties cargado
     */
    public void setRutaProperties(String ruta) {
        this.rutaProperties = ruta;
        ventana.getLblRutaArchivo().setText("Cargado: " + ruta);
    }

    /**
     * Delega a la vista la creación de las pestañas de categorías
     */
    public void cargarCategorias(String[] categorias) {
        ventana.limpiarPestanas();
        for (String cat : categorias) {
            ventana.crearPestanaCategoria(cat.toUpperCase());
            // pide que busque las técnicas de esta categoría
            cPrincipal.cargarKimaritesPorCategoria(cat); 
        }
    }

    /**
     * Delega a la vista el llenado de los CheckBoxes en la pestaña correspondiente
     */
    public void cargarKimaritesPorCategoria(String[] kimarites) {
        ventana.llenarTecnicasEnUltimaPestana(kimarites);
    }

    // --- Métodos de delegación de mensajes ---

    public void mostrarMensaje(String mensaje) {
        ventana.mostrarMensaje(mensaje);
    }

    public void mostrarAdvertencia(String mensaje) {
        ventana.mostrarAdvertencia(mensaje);
    }

    public void mostrarResultado(String resultado) {
        ventana.mostrarResultado(resultado);
    }
}