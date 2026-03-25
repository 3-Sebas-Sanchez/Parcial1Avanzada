/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.modelo.Conexion;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * Maneja el ServerSocket del servidor 
 *
 * @author Nath
 */
public class ConexionServerSocket {
    
    private static ServerSocket serverSocket;
    private static int puerto;
    
    /**
     * Metodo que se encarga de configurar el puerto por el cual el servidor va
     * a atender a los usuarios (Aspirantes a Sumo)
     *
     * @param entrada Puerto de entrada
     */
    public static void configurar(int entrada) {
        puerto = entrada;
    }
    
    /**
     * Establece y retorna el ServerSocket único para el proyecto
     *
     * @return ServerSocket
     */
    public static ServerSocket conexion() {
        
        // Verifica si ya está abierto para no crear uno nuevo
        if (serverSocket != null && !serverSocket.isClosed()) {
            return serverSocket;
        }
        
        // Validación 
        if (puerto <= 0) {
            throw new IllegalStateException("El puerto del servidor no está configurado o es inválido.");
        }
        
        try {
            // Crea el serverSocket con el puerto previamente configurado
            serverSocket = new ServerSocket(puerto);
            return serverSocket;
        } catch (IOException e) {

            throw new RuntimeException("No fue posible iniciar el ServerSocket en el puerto: " + puerto, e);
        }
    }
    
    /**
     * Encargado de cerrar el serverSocket de forma segura
     */
    public static void cerrar() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
                
            }
        }
    }
    
    /**
     * Comprueba si el server socket se encuentra activo
     *
     * @return true si está escuchando, false en caso contrario
     */
    public static boolean activo() {
        return serverSocket != null && !serverSocket.isClosed();
    }
    
    /**
     * Obtiene el puerto configurado
     *
     * @return Puerto
     */
    public static int getPuerto() {
        return puerto;
    }
    
}
