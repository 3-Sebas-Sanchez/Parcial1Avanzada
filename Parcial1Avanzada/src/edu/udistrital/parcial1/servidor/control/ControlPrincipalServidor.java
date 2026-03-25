package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionBaseDeDatos;
import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionProperties;
import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionServerSocket;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class ControlPrincipalServidor {

    private ControlLuchador controlLuchador;
    private ControlVentanaServidor controlVentanaServidor;

    // Guardamos el ServerSocket para poder cerrarlo al llegar a 6
    private volatile ServerSocket serverSocket;

    public ControlPrincipalServidor() {
        this.controlLuchador = new ControlLuchador(this);
        this.controlVentanaServidor = new ControlVentanaServidor(this);
        seleccionarProperties();
        iniciarServidorSocket();
    }

    private void seleccionarProperties() {
        File archivo = controlVentanaServidor.seleccionarProperties(
                "Seleccione el archivo Servidor.properties");

        if (archivo == null) {
            controlVentanaServidor.mostrarMensaje(
                    "No se seleccionó archivo de configuración. Cerrando...");
            System.exit(0);
        }

        ConexionProperties cnxProps = new ConexionProperties(archivo);
        Properties props = cnxProps.conexion();

        ConexionBaseDeDatos.cargarCredenciales(archivo.getAbsolutePath());

        String puertoStr = props.getProperty("servidor.puerto");
        if (puertoStr == null || puertoStr.trim().isEmpty()) {
            controlVentanaServidor.mostrarMensaje(
                    "Falta la clave 'servidor.puerto' en el archivo de configuración.");
            System.exit(0);
        }

        int puerto = Integer.parseInt(puertoStr.trim());
        ConexionServerSocket.configurar(puerto);

        controlVentanaServidor.mostrarMensaje(
                "Configuración cargada. Servidor en puerto: " + puerto);
    }

    private void iniciarServidorSocket() {
        Thread hiloServidor = new Thread(() -> {
            try {
                serverSocket = ConexionServerSocket.conexion();
                controlVentanaServidor.mostrarMensaje(
                        "Servidor escuchando en puerto " + ConexionServerSocket.getPuerto());

                while (!serverSocket.isClosed()) {
                    Socket clienteSocket = serverSocket.accept();

                    controlVentanaServidor.mostrarMensaje(
                            "Cliente conectado: "
                            + clienteSocket.getInetAddress().getHostAddress());

                    ControlHilo hilo = new ControlHilo(clienteSocket, this);
                    new Thread(hilo).start();
                }

            } catch (Exception e) {
                controlVentanaServidor.mostrarMensaje(
                        "Servidor dejó de aceptar clientes: " + e.getMessage());
            }
        });

        hiloServidor.setDaemon(true);
        hiloServidor.start();
    }

    /**
     * Se llama cuando ya hay 6 luchadores: cierra el ServerSocket.
     */
    public void detenerAceptacionDeClientes() {
        try {
            controlVentanaServidor.mostrarMensaje("⛔ Cupo lleno: cerrando accept() del servidor...");
            ConexionServerSocket.cerrar(); // cierra el singleton
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception ignored) {
        }
    }

    public ControlLuchador getControlLuchador() {
        return controlLuchador;
    }

    public ControlVentanaServidor getControlVentanaServidor() {
        return controlVentanaServidor;
    }
}