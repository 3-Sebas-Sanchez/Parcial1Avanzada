/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.modelo;


/**
 *
 * @author Nath
 */
public class LuchadorDTO {
    
    private String nombre;
    private double peso;
    private int combatesGanados;
    private String[] kimarites;
    private LuchadorDTO rival;
    private boolean dentroDelDohyo;
     
    
    /**
     * Constructor del luchador
     */
    public LuchadorDTO(String nombre, double peso, int combatesGanados, String[] kimarites, LuchadorDTO rival, boolean dentroDelDohyo) {
        this.nombre = nombre;
        this.peso = peso;
        this.combatesGanados = combatesGanados;
        this.kimarites = kimarites;
        this.rival = rival;
        this.dentroDelDohyo = dentroDelDohyo;
    }
    
    //Getters

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    public int getCombatesGanados() {
        return combatesGanados;
    }

    public String[] getKimarites() {
        return kimarites;
    }

    public LuchadorDTO getRival() {
        return rival;
    }

    public boolean isDentroDelDohyo() {
        return dentroDelDohyo;
    }
    
    //Setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setCombatesGanados(int combatesGanados) {
        this.combatesGanados = combatesGanados;
    }

    public void setKimarites(String[] kimarites) {
        this.kimarites = kimarites;
    }

    public void setRival(LuchadorDTO rival) {
        this.rival = rival;
    }

    public void setDentroDelDohyo(boolean dentroDelDohyo) {
        this.dentroDelDohyo = dentroDelDohyo;
    }   
    
}
