package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.DAO.LuchadorDAO;
import edu.udistrital.parcial1.servidor.modelo.LuchadorDTO;
import java.util.ArrayList;
import java.util.List;

public class ControlLuchador {

    private final ControlPrincipalServidor cPrincipal;
    private final LuchadorDAO luchadorDAO;
    private final List<ParHilo> participantes;

    private static final int MAX_LUCHADORES = 6;

    private boolean torneoIniciado = false;

    public ControlLuchador(ControlPrincipalServidor cPrincipal) {
        this.cPrincipal = cPrincipal;
        this.luchadorDAO = new LuchadorDAO();
        this.participantes = new ArrayList<>();
    }

    /**
     * Intenta registrar al luchador en la lista del torneo.
     * @return true si fue aceptado, false si el torneo ya está lleno/iniciado.
     */
    public synchronized boolean intentarRegistrarParticipante(LuchadorDTO luchador, ControlHilo hilo) {

        // Si ya inició o ya está lleno, NO aceptar
        if (torneoIniciado || participantes.size() >= MAX_LUCHADORES) {
            return false;
        }

        participantes.add(new ParHilo(luchador, hilo));

        cPrincipal.getControlVentanaServidor().mostrarMensaje(
                "Luchadores registrados: " + participantes.size() + " / " + MAX_LUCHADORES);

        // Si acabamos de llegar a 6, iniciar torneo
        if (participantes.size() == MAX_LUCHADORES) {
            torneoIniciado = true;

            cPrincipal.getControlVentanaServidor().mostrarMensaje(
                    "🏮 Cupo completo (" + MAX_LUCHADORES + "). Iniciando torneo...");

            // IMPORTANTÍSIMO: cerrar el ServerSocket para que NO acepte más
            cPrincipal.detenerAceptacionDeClientes();

            ControlDohyo dohyo = new ControlDohyo(cPrincipal, luchadorDAO);
            List<ParHilo> copia = new ArrayList<>(participantes);

            new Thread(() -> dohyo.iniciarCombates(copia)).start();
        }

        return true;
    }

    public synchronized boolean isTorneoIniciado() {
        return torneoIniciado;
    }

    public synchronized int getCupoActual() {
        return participantes.size();
    }
}