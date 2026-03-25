/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.modelo.Conexion;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * Realiza la conexion al servidor mediante un Socket.
 * Aplica el patrón Singleton para mantener una única conexión activa por cliente.
 *
 * @author Nath
 */
public class ConexionSocket {
    
    private static Socket socket = null; // Nuestra conexion vive de forma global
    
    // Atributos configurables
    private static String ipServidor;
    private static int puertoServidor;
    
    /**
     * Asigna los valores necesarios para establecer la conexion con el servidor
     *
     * @param ip Ip del servidor
     * @param puerto Puerto del servidor
     */
    public static void configurarSocket(String ip, int puerto) {
        ConexionSocket.ipServidor = ip;
        ConexionSocket.puertoServidor = puerto;
    }
    
    /**
     * Establece y retorna la conexion al servidor
     *
     * @return Socket conectado
     */
    public static Socket conexion() {
        
        // Si ya está conectado, devuelve el mismo socket
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            return socket;
        }
        
        // Validación de seguridad
        if (ipServidor == null || ipServidor.isEmpty() || puertoServidor <= 0) {
            throw new IllegalStateException("IP/PUERTO del servidor no están configurados.");
        }
        
        try {
            socket = new Socket(ipServidor, puertoServidor);
            socket.setTcpNoDelay(true); 
            return socket;
        } catch (IOException e) {
            
            throw new RuntimeException("No fue posible conectar al servidor en " + ipServidor + ":" + puertoServidor, e);
        }
    }
    
    /**
     * Cierra la conexion con el socket si esta activa
     */
    public static void cerrarConexion() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                // si falla el cierre
                throw new RuntimeException("Error al cerrar el socket.", e);
            }
        }
    }
    
    /**
     * Comprueba si el socket esta conectado
     * * @return true si está conectado, false en caso contrario
     */
    public static boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
    
    /**
     * Obtiene la ip del servidor
     * * @return Ip del servidor
     */
    public static String getIpServidor() {
        return ipServidor;
    }
    
    /**
     * Obtiene el puerto del servidor
     * * @return Puerto del servidor
     */
    public static int getPuertoServidor() {
        return puertoServidor;
    }
    
}
