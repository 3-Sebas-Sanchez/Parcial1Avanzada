/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.usuario.modelo.DAO;

import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionProperties;
import edu.udistrital.parcial1.usuario.modelo.Conexion.ConexionSocket;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * DAO para leer configuraciones y técnicas desde un archivo .properties.
 * Optimizado con Caché para evitar lecturas excesivas al disco duro.
 *
 * @author Nath
 */
public class PropertiesDAO {
    
    private ConexionProperties lectorArchivo;
    private File archivoActual;
    
    // Caché para guardar los datos en RAM después de la primera lectura
    private Properties propiedadesCache; 
    
    /**
     * Constructor por defecto
     */
    public PropertiesDAO() {
        this.lectorArchivo = new ConexionProperties();
        this.propiedadesCache = null;
    }

    /**
     * Asigna el archivo y reinicia la caché
     *
     * @param archivo Archivo físico .properties
     */
    public void setArchivoProperties(File archivo) {
        if (archivo == null) {
            throw new IllegalArgumentException("El archivo .properties no puede ser nulo.");
        }
        if (!archivo.exists() || !archivo.isFile() || !archivo.canRead()) {
            throw new IllegalStateException("El archivo no existe o no tiene permisos de lectura: " + archivo.getName());
        }
        this.archivoActual = archivo;
        this.propiedadesCache = null; // Borra la memoria si cambia de archivo
    }
    
    /**
     * Carga toda la informacion del archivo
     *
     * @return Properties cargados en memoria
     */
    public Properties cargarTodas() {
        // SINGLETON: Si ya se leyò antes se devuelve la memoria RAM
        if (propiedadesCache != null) {
            return propiedadesCache;
        }
        
        // Si no, error
        if (archivoActual == null) {
            throw new IllegalStateException("No se ha asignado un archivo .properties con setArchivoProperties().");
        }
        
        lectorArchivo.setArchivo(archivoActual);
        propiedadesCache = lectorArchivo.conexion(); 
        
        if (propiedadesCache == null || propiedadesCache.isEmpty()) {
            throw new IllegalStateException("El archivo .properties está vacío o corrupto.");
        }
        
        return propiedadesCache;
    }

    /**
     * Consulta dentro del .properties usando la caché.
     *
     * @param clave Clave a buscar (ej.: "IP_SOCKET", "PUERTO_SOCKET").
     * @return Valor encontrado (limpio de espacios) o null si no existe.
     */
    public String consultar(String clave) {
        Properties p = cargarTodas(); // Obtiene el caché
        String valor = p.getProperty(clave);
        return valor != null ? valor.trim() : null;
    }
    
    /**
     * Extrae todas las técnicas del archivo y las devuelve como una lista
     */
    public List<String> obtenerKimarites() {
        Properties propiedades = cargarTodas();
        List<String> lista = new ArrayList<>();
        
        for (String clave : propiedades.stringPropertyNames()) {
            if (clave.startsWith("tecnica.")) { 
                lista.add(propiedades.getProperty(clave).trim());
            }
        }
        return lista; 
    }
    
    /**
     * Saca los elementos y asigna los valores para
     * poder conectar al socket del Cliente
     */
    public void configurarConexionSocketDesdeArchivo() {
        String ip = consultar("IP_SOCKET");
        String port = consultar("PUERTO_SOCKET");

        if (ip == null || port == null || ip.isEmpty() || port.isEmpty()) {
            throw new IllegalStateException("Faltan claves de conexión de socket (IP_SOCKET, PUERTO_SOCKET).");
        }

        try {
            int puerto = Integer.parseInt(port);
            ConexionSocket.configurarSocket(ip, puerto);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El PUERTO_SOCKET en el archivo no es un número válido.", e);
        }
    }
    
    /**
     * Obtiene todas las técnicas kimarites organizadas por categoría
     *
     * @return Map con las categorías de técnicas
     */
    public Map<String, String[]> cargarTodasLasTecnicas() {
        Map<String, String[]> tecnicas = new HashMap<>();

        // Definè
        String[] categorias = {
            "basicas", "tropezones", "derribos", "torsiones", 
            "caidasInvertidas", "tecnicasEspeciales", "noTecnicas"
        };

        // Recorre las categorías usando el método consultar()
        for (String cat : categorias) {
            // Reconstruye la clave, ej: "tecnicas.basicas.kimarites"
            String claveBuscada = "tecnicas." + cat + ".kimarites";
            String valor = consultar(claveBuscada);
            
            if (valor != null && !valor.isEmpty()) {
                String[] arregloTecnicas = valor.split(",");
                // Limpia los espacios
                for (int i = 0; i < arregloTecnicas.length; i++) {
                    arregloTecnicas[i] = arregloTecnicas[i].trim();
                }
                tecnicas.put(cat, arregloTecnicas);
            }
        }

        return tecnicas;
    }
    
    /**
     * Obtiene una técnica específica de una categoría
     */
    public String obtenerTecnica(String categoria, int indice) {
        Map<String, String[]> tecnicas = cargarTodasLasTecnicas();
        if (tecnicas.containsKey(categoria)) {
            String[] tecnicasCategoria = tecnicas.get(categoria);
            if (indice >= 0 && indice < tecnicasCategoria.length) {
                return tecnicasCategoria[indice];
            }
        }
        return null;
    }
    
    /**
     * Obtiene todas las técnicas de una categoría específica
     */
    public String[] obtenerTecnicasPorCategoria(String categoria) {
        Map<String, String[]> tecnicas = cargarTodasLasTecnicas();
        if (tecnicas.containsKey(categoria)) {
            return tecnicas.get(categoria);
        }
        return new String[0];
    }
}