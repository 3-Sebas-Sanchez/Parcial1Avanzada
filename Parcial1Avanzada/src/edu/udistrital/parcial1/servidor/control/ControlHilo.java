package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.DAO.LuchadorDAO;
import edu.udistrital.parcial1.servidor.modelo.LuchadorDTO;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ControlHilo implements Runnable {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private ControlPrincipalServidor cPrincipalServidor;
    private LuchadorDAO luchadorDAO;

    private LuchadorDTO luchador;

    public ControlHilo(Socket socket, ControlPrincipalServidor cPrincipalServidor) {
        this.socket = socket;
        this.cPrincipalServidor = cPrincipalServidor;
        this.luchadorDAO = new LuchadorDAO();
    }

    @Override
    public void run() {
        try {
            input  = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // 1) PING/PONG
            String ping = input.readUTF();
            if ("PING".equalsIgnoreCase(ping)) {
                output.writeUTF("PONG");
                output.flush();
            }

            // 2) Cabecera
            String cabecera = input.readUTF();
            if (!"NUEVO_LUCHADOR".equalsIgnoreCase(cabecera)) {
                output.writeUTF("ERROR_PROTOCOLO");
                output.flush();
                return;
            }

            // 3) Datos
            String nombre = input.readUTF();
            double peso = input.readDouble();
            int combatesGanados = input.readInt();

            int cantidadKimarites = input.readInt();
            String[] kimarites = new String[cantidadKimarites];
            for (int i = 0; i < cantidadKimarites; i++) {
                kimarites[i] = input.readUTF();
            }

            // 4) DTO + BD
            luchador = new LuchadorDTO(nombre, peso, combatesGanados, kimarites);
            boolean guardado = luchadorDAO.insertar(luchador);

            if (!guardado) {
                output.writeUTF("ERROR_REGISTRO");
                output.flush();
                return;
            }

            // 5) Intentar entrar al torneo (máximo 6)
            boolean aceptado = cPrincipalServidor.getControlLuchador()
                    .intentarRegistrarParticipante(luchador, this);

            if (!aceptado) {
                output.writeUTF("TORNEO_LLENO");
                output.flush();
                return;
            }

            // UI/log
            cPrincipalServidor.getControlVentanaServidor()
                    .mostrarMensaje("✅ Luchador aceptado al torneo: " + nombre
                            + " (id=" + luchador.getIdLuchador() + ")");
            cPrincipalServidor.getControlVentanaServidor()
                    .agregarLuchadorLista(nombre + " | " + peso + " kg");

            // Confirmación al cliente
            output.writeUTF("REGISTRADO");
            output.flush();

            // 6) Esperar resultado
            synchronized (this) {
                wait();
            }

        } catch (IOException | InterruptedException e) {
            cPrincipalServidor.getControlVentanaServidor()
                    .mostrarMensaje("Error en hilo de cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    public synchronized void notificarResultado(boolean gano) {
        try {
            String resultado = gano ? "GANASTE" : "PERDISTE";
            output.writeUTF(resultado);
            output.flush();
        } catch (IOException e) {
            cPrincipalServidor.getControlVentanaServidor()
                    .mostrarMensaje("Error al notificar resultado: " + e.getMessage());
        } finally {
            notifyAll();
        }
    }

    public LuchadorDTO getLuchador() {
        return luchador;
    }

    private void cerrarConexion() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {
        }
    }
}