/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.control;


import edu.udistrital.parcial1.usuario.modelo.Conexion.ConexionSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Control encargado de manejar la comunicación del cliente con el servidor.
 * Envía los datos del luchador y espera la respuesta del servidor.
 *
 * @author Nath
 */
public class ControlSocket {
    
    // Inyección de dependencia para comunicarse de vuelta con la Vista
    private ControlPrincipalUsuario cPrincipal;

    private Socket socketActual; 
    private DataOutputStream salida;
    private DataInputStream entrada;

    /**
     * Constructor que recibe la inyeccion del control Principal
     *
     * @param cGeneral Control General
     */
    public ControlSocket(ControlPrincipalUsuario cGeneral) {
        this.cPrincipal = cGeneral;
    }
    
    /**
     * Intenta establecer conexión y realiza el protocolo PING/PONG
     * * @return true si el servidor respondió PONG, false si falló
     */
    public boolean conectar() {
        try {
            // Solicita la conexion al servidor 
            socketActual = ConexionSocket.conexion();
            
            // Metodo interno que inicializa los Output e Input
            inicializarStreams(socketActual);
            
            // Protocolo para saber si esta conectado al server
            enviarUTF("PING");
            
            // Espera una respuesta
            String resp = leerUTF();
            
            return resp != null && (resp.equalsIgnoreCase("PONG") || resp.toUpperCase().startsWith("OK"));
            
        } catch (RuntimeException ex) {
            cerrar(); // Si ocurre algun error termina la conexion y limpia
            return false;
        }
    }
    
    /**
     * Inicializa los flujos en el orden correcto
     */
    private void inicializarStreams(Socket socket) {
        try {
            salida = new DataOutputStream(socket.getOutputStream());
            salida.flush();
            entrada = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error al crear los flujos de red.", e);
        }
    }
    
    /**
     * Métodos de utilidad rápida para textos (usados por el PING/PONG)
     */
    public void enviarUTF(String mensaje) {
        try {
            salida.writeUTF(mensaje);
            salida.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error al enviar mensaje UTF.", e);
        }
    }
    
    public String leerUTF() {
        try {
            return entrada.readUTF();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer mensaje UTF.", e);
        }
    }
    
    /**
     * Envía los datos del luchador al servidor usando tipos primitivos
     */
    public void enviarLuchador(String nombre, double peso, int combatesGanados, String[] kimarites) {
        try {
            // Avisa qué tipo de dato viene
            enviarUTF("NUEVO_LUCHADOR"); 
            
            salida.writeUTF(nombre);
            salida.writeDouble(peso);  
            salida.writeInt(combatesGanados);

            if (kimarites == null) {
                salida.writeInt(0); 
            } else {
                salida.writeInt(kimarites.length); 
                for (String kimarite : kimarites) {
                    salida.writeUTF(kimarite);
                }
            }
            salida.flush();
            
        } catch (IOException e) {
            throw new RuntimeException("Error al enviar los datos del luchador al servidor.", e);
        }
    }
    
    /**
     * Cierra todas las conexiones
     */
    public void cerrar() {
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            ConexionSocket.cerrarConexion();
        } catch (IOException e) {
            throw new RuntimeException("Error al intentar cerrar los flujos de red: " + e.getMessage(), e);
        }
    }
    
}