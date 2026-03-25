/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.vista.VentanaServidor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 * @author sebas
 */
public class ControlVentanaServidor implements ActionListener{
    // Referencia al controlador principal.
    private ControlPrincipalServidor cPrincipal;
 
    //Ventana principal del servidor.
    private VentanaServidor ventana;
 
    /**
     * Constructor del controlador de la ventana del servidor.
     *
     * @param cPrincipal Referencia al controlador principal del servidor
     */
    public ControlVentanaServidor(ControlPrincipalServidor cPrincipal) {
        this.cPrincipal = cPrincipal;
        this.ventana = new VentanaServidor();
        mostrarVentana();
    }
 
    /**
     * Hace visible la ventana principal del servidor.
     * Debe llamarse desde el EDT via {@code SwingUtilities.invokeLater}.
     */
    public void mostrarVentana() {
       ventana.setVisible(true);
    }
 
    /**
     * Maneja todos los eventos de botones de la ventana del servidor.
     *
     * @param e Evento de acción disparado por un componente de la UI
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("SalirServidor")) {
            System.exit(0);
        }
    }
 
    /**
     * Muestra un mensaje en el área de resultados de la ventana.
     * Puede llamarse desde cualquier hilo — la ventana lo redirige al EDT.
     *
     * @param mensaje Texto a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        ventana.agregarResultado(mensaje);
    }
 
    /**
     * Agrega el nombre de un luchador a la lista visible de conectados.
     * Puede llamarse desde cualquier hilo.
     *
     * @param nombreLuchador Nombre del luchador recién registrado
     */
    public void agregarLuchadorLista(String nombreLuchador) {
        ventana.agregarLuchadorLista(nombreLuchador);
    }
 
    /**
     * Cambia el GIF del panel izquierdo al estado de espera inicial.
     */
    public void mostrarGifInicio() {
        ventana.mostrarGifInicio();
    }
 
    /**
     * Cambia el GIF del panel izquierdo al estado de entrada de luchador.
     */
    public void mostrarGifEntrada() {
        ventana.mostrarGifEntrada();
    }
 
    /**
     * Cambia el GIF del panel izquierdo al estado de combate activo.
     */
    public void mostrarGifPelea() {
        ventana.mostrarGifPelea();
    }
 
    /**
     * Delega a la ventana la apertura del selector de archivo .properties.
     * El JFileChooser vive en la Vista — el Control solo recibe el File.
     *
     * @param titulo Título del diálogo
     * @return El {@link File} seleccionado, o {@code null} si se canceló
     */
    public File seleccionarProperties(String titulo) {
        return ventana.seleccionarProperties(titulo);
    }
 
    /**
     * Obtiene la ventana del servidor.
     *
     * @return {@link VentanaServidor}
     */
    public VentanaServidor getVentana() {
        return ventana;
    }
}
