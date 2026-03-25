/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.control;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author sebas
 */
public class ControlHilo implements Runnable{
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private ControlPrincipalServidor cPrincipalServidor;
    private String usuarioAutenticado = null;

    public ControlHilo(Socket socket, ControlPrincipalServidor cPrincipalServidor) {
        this.socket = socket;
        this.cPrincipalServidor = cPrincipalServidor;
    }
    
    @Override
    public void run(){
        try{
            input = new DataInputStream(socket.getInputStream());
        }catch(IOException e){
        }finally{
            cerrarConexion();
        }
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
