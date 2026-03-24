/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.modelo.Conexion;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * Clase encargada de gestionar la conexión con la base de datos.
 *
 * Permite cargar las credenciales desde un archivo {@code .properties}
 * y establecer una conexión utilizando {@link DriverManager}.

 * @author Nath
 */

public class ConexionBaseDeDatos {
    
    /** Conexión única con la base de datos (Singleton). */
    private static Connection cn = null;
    
    private static String urldb = null;
    private static String usuario = null;
    private static String contrasena = null;
    private static String driver = null; 
    
    /**
     * Carga las credenciales de conexión desde un archivo .properties.
     */
    public static void cargarCredenciales(String rutaProperties) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(rutaProperties)) {
            props.load(fis);
            
            urldb= props.getProperty("db.url");
            usuario = props.getProperty("db.usuario");
            contrasena = props.getProperty("db.contrasena", ""); 
            driver = props.getProperty("db.driver"); 
        } catch (Exception e) {
            throw new RuntimeException("No se puede cargar el archivo properties en la ruta: " + rutaProperties, e);
        }
    }
    
    /**
     * Establece y retorna la conexión unica con la base de datos.
     */
    public static Connection getConexion() {
        try {

            // Solo se crea la conexión si es la primera vez (null) o si se cerró por accidente
            if (cn == null || cn.isClosed()) {
                
                // Registra el driver
                if (driver != null) {
                    Class.forName(driver);
                }
                
                // Establece el puente con MySQL
                cn = DriverManager.getConnection(urldb, usuario, contrasena);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException("No se puede establecer la conexión con la base de datos de Luchadores", ex);
        }
        
        // Si ya existía devuelve la que ya estaba abierta
        return cn; 
    }
    
    /**
     * Cierra la conexión actual con la base de datos si existe.
     */
    public static void desconectar() {
        try {
            if (cn != null && !cn.isClosed()) {
                cn.close();
            }
        } catch (SQLException e) {
            
        }
    }
    
}
