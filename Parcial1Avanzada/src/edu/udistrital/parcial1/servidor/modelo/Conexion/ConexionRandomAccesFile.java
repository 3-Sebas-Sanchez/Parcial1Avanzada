/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.modelo.Conexion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Realiza la conexion al archivo de acceso aleatorio
 * @author Nath
 */
public class ConexionRandomAccesFile {
    
    private File archivo;
    private String modo = "rw"; // lectura/escritura por defecto
    
    //Constructor vacio
    public ConexionRandomAccesFile() {
    }
    
    /**
     * Constructor 
     * @param archivo El archivo
     * @param modo El modo de apertura ("r", "rw", etc.)
     */
    public ConexionRandomAccesFile(File archivo, String modo) {
        this.archivo = archivo;
        this.modo = modo;
    }
    
    /**
     *@return Conexion al raf
     */
    public RandomAccessFile conexion() {
        if (archivo == null) {
            throw new IllegalStateException("Error: No se ha asignado un archivo para el RandomAccessFile.");
        }
        
        try {
            return new RandomAccessFile(archivo, modo);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Error al abrir el archivo RandomAccessFile en la ruta: " + archivo.getAbsolutePath(), e);
        }
    }
}
