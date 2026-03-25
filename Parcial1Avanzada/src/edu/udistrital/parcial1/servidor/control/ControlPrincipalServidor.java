package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionBaseDeDatos;
import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionProperties;
import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionServerSocket;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 * @author sebas
 */
public class ControlPrincipalServidor {

    /** Controlador encargado de la lógica de los luchadores y combates. */
    private ControlLuchador controlLuchador;

    /** Controlador encargado de la ventana y eventos de la interfaz del servidor. */
    private ControlVentanaServidor controlVentanaServidor;

    /**
     * Referencia al ServerSocket activo.
     * Se guarda para poder cerrarlo en cuanto se completen los
     * {@value ControlLuchador#MINIMO_LUCHADORES} luchadores requeridos.
     */
    private ServerSocket serverSocket;


    public ControlPrincipalServidor() {
        this.controlLuchador = new ControlLuchador(this);
        this.controlVentanaServidor = new ControlVentanaServidor(this);
        seleccionarProperties();
        iniciarServidorSocket();
    }

    /**
     * Delega a la Vista la apertura del selector de archivo, luego
     * extrae del .properties las credenciales de BD y el puerto del servidor.
     */
    private void seleccionarProperties() {
        File archivo = controlVentanaServidor.seleccionarProperties(
                "Seleccione el archivo Servidor.properties");
 
        if (archivo == null) {
            controlVentanaServidor.mostrarMensaje(
                    "No se seleccionó archivo de configuración. Cerrando...");
            System.exit(0);
        }

        // Leer propiedades a través de ConexionProperties (Modelo)
        ConexionProperties cnxProps = new ConexionProperties(archivo);
        Properties props = cnxProps.conexion();

        // Cargar credenciales de BD desde el mismo archivo
        ConexionBaseDeDatos.cargarCredenciales(archivo.getAbsolutePath());

        // Leer el puerto desde el .properties — nunca hardcodeado
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

    /**
     * Lanza el ciclo de aceptación de conexiones en un hilo separado.
     * Por cada cliente que se conecta crea un {@link ControlHilo} en su propio hilo.
     */
    private void iniciarServidorSocket() {
        Thread hiloServidor = new Thread(() -> {
            try {
                serverSocket = ConexionServerSocket.conexion();
                controlVentanaServidor.mostrarMensaje(
                        "Servidor escuchando en puerto " + ConexionServerSocket.getPuerto());

                // El bucle termina cuando cerrarServerSocket() cierra el ServerSocket
                // (accept() lanza SocketException y salimos limpiamente).
                while (!serverSocket.isClosed()) {
                    Socket clienteSocket = serverSocket.accept();
                    controlVentanaServidor.mostrarMensaje(
                            "Cliente conectado: "
                            + clienteSocket.getInetAddress().getHostAddress());

                    // Crear hilo de atención independiente para este cliente
                    ControlHilo hilo = new ControlHilo(clienteSocket, this);
                    new Thread(hilo).start();
                }

            } catch (Exception e) {
                // Excepción esperada al cerrar el ServerSocket con cerrarServerSocket()
                controlVentanaServidor.mostrarMensaje(
                        "Servidor dejó de aceptar conexiones: " + e.getMessage());
            }
        });

        hiloServidor.setDaemon(true);
        hiloServidor.start();
    }

    /**
     * Cierra el {@link ServerSocket} para que el servidor deje de aceptar
     * nuevas conexiones. Se invoca desde {@link ControlLuchador} en cuanto
     * se completa el cupo de luchadores requeridos.
     */
    public void cerrarServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                controlVentanaServidor.mostrarMensaje(
                        "Cupo completo. El servidor ya no acepta más luchadores.");
            }
        } catch (Exception e) {
            controlVentanaServidor.mostrarMensaje(
                    "Error al cerrar ServerSocket: " + e.getMessage());
        }
    }

    /**
     * Obtiene el controlador de luchadores y combates.
     *
     * @return {@link ControlLuchador}
     */
    public ControlLuchador getControlLuchador() {
        return controlLuchador;
    }

    /**
     * Obtiene el controlador de la ventana del servidor.
     *
     * @return {@link ControlVentanaServidor}
     */
    public ControlVentanaServidor getControlVentanaServidor() {
        return controlVentanaServidor;
    }
}