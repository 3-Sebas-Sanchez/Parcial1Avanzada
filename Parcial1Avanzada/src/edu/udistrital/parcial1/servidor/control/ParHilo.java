/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.LuchadorDTO;

/**
 *
 * @author sebas
 */
public class ParHilo {
   /** DTO con los datos del luchador. */
    private LuchadorDTO luchador;
 
    /** Hilo que mantiene la comunicación con el cliente del luchador. */
    private ControlHilo hilo;
 
    /**
     * Constructor del par luchador-hilo.
     *
     * @param luchador DTO del luchador
     * @param hilo     Hilo que atiende al cliente
     */
    public ParHilo(LuchadorDTO luchador, ControlHilo hilo) {
        this.luchador = luchador;
        this.hilo = hilo;
    }
 
    /**
     * Obtiene el DTO del luchador.
     *
     * @return {@link LuchadorDTO}
     */
    public LuchadorDTO getLuchador() { return luchador; }
 
    /**
     * Obtiene el hilo que atiende al cliente.
     *
     * @return {@link ControlHilo}
     */
    public ControlHilo getHilo() { return hilo; } 
}
