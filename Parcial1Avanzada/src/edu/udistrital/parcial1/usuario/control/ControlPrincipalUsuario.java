/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.control;
import edu.udistrital.parcial1.usuario.modelo.DAO.PropertiesDAO;
import java.io.IOException;
import javax.swing.Timer;
import javax.swing.SwingUtilities;


/**
 * Controlador Principal que orquesta la comunicación entre la Vista,
 * el Socket y la configuración.
 *
 * @author Nath
 */
public class ControlPrincipalUsuario {
    
    private ControlVentanaUsuario controlVista;
    private ControlSocket controlSocket;
    private PropertiesDAO configDAO;      

    private String[] tecnicasLuchador;
    private String archivoProperties;
    
    
    /**
     * Constructor que inicializa todos los componentes
     */
    public ControlPrincipalUsuario() {
        controlVista = new ControlVentanaUsuario(this);
        controlSocket = new ControlSocket(this);
        configDAO = new PropertiesDAO();
        // controlVista.mostrarVentana();
        
        //solicita al usuario cargar el archivo
        cargarArchivoAlInicio();
    }
    
    /**
     * Carga el archivo de propiedades al iniciar la aplicación
     * Muestra el JFileChooser antes de mostrar la ventana principal
     */
    private void cargarArchivoAlInicio() {
        archivoProperties = controlVista.seleccionar("Seleccione el archivo de configuración (Usuario.properties)");

        if (archivoProperties != null) {
            try {
                // Instancia el archivo físico
                configDAO.setArchivoProperties(new java.io.File(archivoProperties));
                
                // Carga los datos usando el caché del DAO
                configDAO.configurarConexionSocketDesdeArchivo();
                
                // Carga las categorías a la vista
                cargarCategorias();
                
                // Actualiza la etiqueta de ruta en la vista
                controlVista.setRutaProperties(archivoProperties);
                
                // Ahora sí muestra la ventana principal
                controlVista.mostrarVentana();
                
                // Informa al usuario que se cargó correctamente
                controlVista.mostrarMensaje(" Archivo de configuración cargado correctamente.");
                
            } catch (Exception e) {
                controlVista.mostrarAdvertencia("Error al cargar el archivo properties: " + e.getMessage() 
                    + "\n\nIntenta nuevamente.");
                // Intenta nuevamente
                cargarArchivoAlInicio();
            }
        } else {
            // Si cancela, intenta nuevamente
            controlVista.mostrarAdvertencia("Debe seleccionar un archivo para continuar.");
            cargarArchivoAlInicio();
        }
    }

    /**
     * Carga las categorias disponibles y las muestra en la vista
     */
    public void cargarCategorias() {
        
        // Obtiene todas las técnicas del DAO
        java.util.Map<String, String[]> todasLasTecnicas = configDAO.cargarTodasLasTecnicas();
        
        // Extrae solo los nombres de las categorías
        String[] categorias = todasLasTecnicas.keySet().toArray(new String[0]);
        
        // Delega a la vista la creación de las pestañas y carga de técnicas
        controlVista.cargarCategorias(categorias);
    }

    /**
     * Carga los kimarites de una categoria y los muestra en la vista
     *
     * @param categoriaId El nombre de la categoría
     */
    public void cargarKimaritesPorCategoria(String categoriaId) {
        
        // Obtiene las técnicas de la categoría desde el DAO
        String[] kimarites = configDAO.obtenerTecnicasPorCategoria(categoriaId);
        
        
        if (kimarites != null && kimarites.length > 0) {
            // Delega a la vista el llenado de los checkboxes
            controlVista.cargarKimaritesPorCategoria(kimarites);
        } else {
            controlVista.mostrarAdvertencia("No se encontraron técnicas para esta categoría.");
        }
    }

    /**
     * Guarda las tecnicas seleccionadas por el luchador
     *
     * @param tecnicas Arreglo de técnicas elegidas
     */
    public void confirmarTecnicas(String[] tecnicas) {
        this.tecnicasLuchador = tecnicas;
        controlVista.mostrarMensaje("Técnicas confirmadas con éxito.");
    }

    /**
     * Conecta al servidor, envía al luchador y espera el resultado
     * @param nombre Nombre del luchador
     * @param peso Peso del luchador (como String)
     * @param combates Combates ganados (como String)
     */
    public void conectarYPelear(String nombre, String peso, String combates) {
        
        // Verifica que se hayan seleccionado técnicas
        if (tecnicasLuchador == null || tecnicasLuchador.length == 0) {
            controlVista.mostrarAdvertencia("Debe confirmar sus tecnicas antes de pelear.");
            return;
        }
        
        //Verifica que los campos no estén vacíos
        if (nombre.trim().isEmpty() || peso.trim().isEmpty() || combates.trim().isEmpty()) {
            controlVista.mostrarAdvertencia("Debe completar todos los campos.");
            return;
        }

        try {
            // Conversión de datos
            double pesoDouble = Double.parseDouble(peso); // Cambiado a double
            int combatesInt = Integer.parseInt(combates);
            
            //Verifica que los valores sean positivos:
            if (pesoDouble <= 0 || combatesInt < 0) {
                controlVista.mostrarAdvertencia("El peso debe ser positivo y los combates no pueden ser negativos.");
                return;
            }
            

            // Intenta conectar primero 
            if (!controlSocket.conectar()) {
                controlVista.mostrarAdvertencia("No se pudo conectar con el servidor de Sumo.");
                return;
            }
            
            // Informa que se conectó
            controlVista.mostrarMensaje("Conectado al servidor. Enviando luchador...");

            // Envia el luchador
            controlSocket.enviarLuchador(nombre, pesoDouble, combatesInt, tecnicasLuchador);
            controlVista.mostrarMensaje("¡Luchador " + nombre + "  Esperando resultados...");

            // Se lanza un Hilo para esperar la respuesta
            new Thread(() -> {
                try {
                    // Se queda esperando la respuesta del servidor en segundo plano
                    String resultado = controlSocket.leerUTF(); 
                    
                    // actualizamos la Vista
                    SwingUtilities.invokeLater(() -> {
                        controlVista.mostrarResultado(resultado);
                        controlSocket.cerrar(); // Desconectamos al terminar
                        
                        // Informamos al usuario que la aplicación se cerrará sola
                        controlVista.mostrarMensaje("El combate ha finalizado. La aplicación se cerrará automáticamente.");
                        
                        // Configuramos un temporizador de 5000 milisegundos (5 segundos)
                        Timer temporizador = new Timer(5000, e -> {
                            System.exit(0); // Apaga la máquina virtual y cierra el programa
                        });
                        temporizador.setRepeats(false); // Para que solo se ejecute una vez
                        temporizador.start(); // Iniciamos el conteo
                    });
                    
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> 
                        controlVista.mostrarAdvertencia("Error durante el combate: " + e.getMessage())
                    );
                }
            }).start();

        } catch (NumberFormatException e) {
            controlVista.mostrarAdvertencia("El peso debe ser numérico y los combates un número entero.");
        }

    }
    

}

