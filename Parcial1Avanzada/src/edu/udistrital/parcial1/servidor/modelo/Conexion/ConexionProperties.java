/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.modelo.Conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Nath
 */
public class ConexionProperties {
    
    private File archivo;
    
    // Constructor vacio
    public ConexionProperties() {
    }

    /**
     * Constructor que del archivo
     *
     * @param archivo Archivo
     */
    public ConexionProperties(File archivo) {
        this.archivo = archivo;
    }
    
    /**
     * Conecta con el archivo
     *
     * @return Properties
     */
    public Properties conexion() {
        
        if (archivo == null) {
            throw new IllegalStateException("No se ha asociado un"
                    + " archivo .properties.");
        }
        
        // archivo incorrecto
        if (!archivo.exists() || !archivo.isFile()) {
            throw new IllegalStateException("El archivo .properties no "
                    + "existe o no es válido: " + archivo);
        }

        Properties props = new Properties();
        
        //Para despues de su uso se cierre el archivo
        try (FileInputStream aux = new FileInputStream(archivo)) {
            props.load(aux);
            return props;
            
        } catch (IOException ioe) {
            
            throw new RuntimeException("Error crítico al leer el archivo de configuración: " + archivo.getName(), ioe);
        }
    }

    /**
     * Obtiene un archivo
     *
     * @return Archivo
     */
    public File getArchivo() {
        return archivo;
    }

    /**
     * Asigna un archivo
     *
     * @param archivo Archivo
     */
    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
    
}
