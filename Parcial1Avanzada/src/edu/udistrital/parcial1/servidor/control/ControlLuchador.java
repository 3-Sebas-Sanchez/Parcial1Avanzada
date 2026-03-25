/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.DAO.LuchadorDAO;
import edu.udistrital.parcial1.servidor.modelo.LuchadorDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sebas
 */
public class ControlLuchador {

    // Referencia al controlador principal.
    private ControlPrincipalServidor cPrincipal;

    //DAO para operaciones de persistencia de luchadores en BD.
    private LuchadorDAO luchadorDAO;

    // Lista de participantes registrados (DTO + hilo de atención).
    private List<ParHilo> participantes;

    // Número mínimo de luchadores para poder iniciar los combates. 
    private static final int MINIMO_LUCHADORES = 6;

    /**
     * Constructor del controlador de luchadores.
     *
     * @param cPrincipal Referencia al controlador principal del servidor
     */
    public ControlLuchador(ControlPrincipalServidor cPrincipal) {
        this.cPrincipal = cPrincipal;
        this.luchadorDAO = new LuchadorDAO();
        this.participantes = new ArrayList<>();
    }

    /**
     * Registra un luchador y su hilo en la lista de participantes.
     * Cuando se alcanza el mínimo requerido, delega al {@link ControlDohyo}
     * para que inicie la secuencia de combates.
     *
     * @param luchador DTO del luchador recién guardado en BD
     * @param hilo     Hilo que atiende al cliente de ese luchador
     */
    public synchronized void agregarLuchador(LuchadorDTO luchador, ControlHilo hilo) {
        participantes.add(new ParHilo(luchador, hilo));
        cPrincipal.getControlVentanaServidor().mostrarMensaje(
                "Luchadores registrados: " + participantes.size()
                + " / " + MINIMO_LUCHADORES);

        if (participantes.size() >= MINIMO_LUCHADORES) {
            // Delegar la lógica del combate al ControlDohyo
            ControlDohyo dohyo = new ControlDohyo(cPrincipal, luchadorDAO);
            List<ParHilo> copia = new ArrayList<>(participantes);
            new Thread(() -> dohyo.iniciarCombates(copia)).start();
        }
    }

    /**
     * Obtiene la lista actual de participantes registrados.
     *
     * @return Lista de {@link ParLuchadorHilo}
     */
    public List<ParHilo> getParticipantes() {
        return participantes;
    }

    /**
     * Obtiene el DAO de luchadores.
     *
     * @return {@link LuchadorDAO}
     */
    public LuchadorDAO getLuchadorDAO() {
        return luchadorDAO;
    }
}
