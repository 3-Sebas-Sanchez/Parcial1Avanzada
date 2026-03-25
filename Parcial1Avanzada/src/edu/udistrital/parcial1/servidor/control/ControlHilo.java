/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.DAO.LuchadorDAO;
import edu.udistrital.parcial1.servidor.modelo.LuchadorDTO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author sebas
 */
public class ControlHilo implements Runnable{
    // Socket de comunicación con el cliente.
    private Socket socket;
 
    // Stream de entrada de datos desde el cliente. 
    private DataInputStream input;
 
    // Stream de salida de datos hacia el cliente.
    private DataOutputStream output;
 
    // Referencia al controlador principal para comunicarse con el resto del sistema.
    private ControlPrincipalServidor cPrincipalServidor;
 
    // DAO para registrar al luchador en la base de datos.
    private LuchadorDAO luchadorDAO;
 
    // DTO del luchador atendido por este hilo.
    private LuchadorDTO luchador;
 
    /**
     * Constructor del hilo de atención al cliente.
     *
     * @param socket             Socket asignado al cliente
     * @param cPrincipalServidor Referencia al controlador principal del servidor
     */
    public ControlHilo(Socket socket, ControlPrincipalServidor cPrincipalServidor) {
        this.socket = socket;
        this.cPrincipalServidor = cPrincipalServidor;
        this.luchadorDAO = new LuchadorDAO();
    }
    
    /**
     * Ciclo principal del hilo.
     * Recibe los datos del cliente, los persiste en BD y espera
     * hasta que el servidor notifique el resultado del combate.
     */
    @Override
    public void run() {
        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
 
            // 1. Recibir datos del luchador
            String nombre = input.readUTF();
            double peso = input.readDouble();
 
            // Recibir cantidad de kimarites seleccionados
            int cantidadKimarites = input.readInt();
            String[] kimarites = new String[cantidadKimarites];
            for (int i = 0; i < cantidadKimarites; i++) {
                kimarites[i] = input.readUTF();
            }
 
            //2. Crear DTO y guardar en BD
            luchador = new LuchadorDTO(nombre, peso, 0, kimarites);
            boolean guardado = luchadorDAO.insertar(luchador);
 
            if (guardado) {
                // Confirmar al cliente que fue registrado
                output.writeUTF("REGISTRADO");
                output.flush();
 
                cPrincipalServidor.getControlVentanaServidor()
                        .mostrarMensaje("Luchador registrado: " + nombre
                                + " (id=" + luchador.getIdLuchador() + ")");
 
                // Notificar al ControlLuchador para que lo agregue a la lista de participantes
                cPrincipalServidor.getControlLuchador().agregarLuchador(luchador, this);
 
                //3. Esperar resultado del combate 
                // El hilo queda bloqueado hasta que ControlLuchador llame a notificarResultado()
                synchronized (this) {
                    wait();
                }
 
            } else {
                output.writeUTF("ERROR_REGISTRO");
                output.flush();
            }
 
        } catch (IOException | InterruptedException e) {
            cPrincipalServidor.getControlVentanaServidor()
                    .mostrarMensaje("Error en hilo de cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }
    
     /**
     * Notifica al cliente (luchador) si ganó o perdió el combate.
     * Despierta el hilo que estaba esperando el resultado.
     *
     * @param gano true si el luchador ganó, false si perdió
     */
    public synchronized void notificarResultado(boolean gano) {
        try {
            String resultado = gano ? "GANASTE" : "PERDISTE";
            output.writeUTF(resultado);
            output.flush();
        } catch (IOException e) {
            cPrincipalServidor.getControlVentanaServidor()
                    .mostrarMensaje("Error al notificar resultado: " + e.getMessage());
        } finally {
            notifyAll(); // Despierta el wait() en run()
        }
    }
    
      /**
     * Obtiene el DTO del luchador atendido por este hilo.
     *
     * @return {@link LuchadorDTO} del luchador
     */
    public LuchadorDTO getLuchador() {
        return luchador;
    }
     /**
     * Cierra la conexión
     */
    private void cerrarConexion() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();

            }
        } catch (IOException e) {

        }
    }
}
