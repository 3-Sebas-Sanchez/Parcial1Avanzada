/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.control;

import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionRandomAccesFile;
import edu.udistrital.parcial1.servidor.modelo.DAO.LuchadorDAO;
import edu.udistrital.parcial1.servidor.modelo.LuchadorDTO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author sebas
 */
public class ControlDohyo {

    // Referencia al controlador principal para actualizar la Vista.
    private ControlPrincipalServidor cPrincipal;

    // DAO para actualizar victorias en BD al terminar cada combate. 
    private LuchadorDAO luchadorDAO;

    // Tamaño fijo de cada registro en el archivo de acceso aleatorio (bytes).
    private static final int TAMANIO_REGISTRO = 200;

    // Probabilidad de que un kimarite saque al rival del Dohyō (20%).
    private static final double PROBABILIDAD_SACAR = 0.20;

    
    public ControlDohyo(ControlPrincipalServidor cPrincipal, LuchadorDAO luchadorDAO) {
        this.cPrincipal = cPrincipal;
        this.luchadorDAO = luchadorDAO;
    }

    /**
     * Inicia y coordina la secuencia completa de combates.
     * El primer combate enfrenta a dos luchadores aleatorios; el ganador
     * se enfrenta al siguiente de la cola hasta agotar todos los participantes.
     *
     * @param participantes Lista de pares (luchador + hilo) que van a combatir
     */
    public void iniciarCombates(List<ParHilo> participantes) {
        List<ParHilo> cola = new ArrayList<>(participantes);
        Collections.shuffle(cola);

        File archivoRaf = new File("data/resultados.dat");
        ConexionRandomAccesFile cnxRaf = new ConexionRandomAccesFile(archivoRaf, "rw");

        try (RandomAccessFile raf = cnxRaf.conexion()) {

            ParHilo ganadorPar = null;

            while (!cola.isEmpty()) {
                if (ganadorPar == null) {
                    // Primer combate: dos luchadores aleatorios
                    ParHilo par1 = cola.remove(0);
                    ParHilo par2 = cola.remove(0);
                    ganadorPar = combatir(par1, par2, raf);
                } else {
                    // El ganador se enfrenta al siguiente de la cola
                    ParHilo retador = cola.remove(0);
                    ganadorPar = combatir(ganadorPar, retador, raf);
                }
            }

            mostrarArchivoResultados(raf);

        } catch (IOException e) {
            cPrincipal.getControlVentanaServidor().mostrarMensaje(
                    "Error con el archivo de resultados: " + e.getMessage());
        }
    }

    /**
     * Ejecuta un combate entre dos luchadores dentro del Dohyō.
     * Los luchadores se turnan para lanzar kimarites aleatorios.
     * El combate termina cuando uno de los dos sale del Dohyō.
     *
     * @param par1 Primer participante
     * @param par2 Segundo participante
     * @param raf  Archivo de acceso aleatorio para registrar resultados
     * @return El {@link ParLuchadorHilo} del ganador
     * @throws IOException Si ocurre un error al escribir en el archivo
     */
    private ParHilo combatir(ParHilo par1, ParHilo par2,
            RandomAccessFile raf) throws IOException {

        LuchadorDTO l1 = par1.getLuchador();
        LuchadorDTO l2 = par2.getLuchador();

        // Preparar el combate
        l1.setRival(l2);
        l2.setRival(l1);
        l1.setDentroDelDohyo(true);
        l2.setDentroDelDohyo(true);

        cPrincipal.getControlVentanaServidor().mostrarMensaje(
                "COMBATE: " + l1.getNombre() + " vs " + l2.getNombre());

        // Bucle de turnos hasta que alguien salga del Dohyō
        while (l1.isDentroDelDohyo() && l2.isDentroDelDohyo()) {

            String kimarite1 = seleccionarKimariteAleatorio(l1);
            boolean saca1 = lanzarKimarite();
            esperar();

            String kimarite2 = seleccionarKimariteAleatorio(l2);
            boolean saca2 = lanzarKimarite();
            esperar();

            cPrincipal.getControlVentanaServidor().mostrarMensaje(
                    l1.getNombre() + " usa " + kimarite1
                    + (saca1 ? " → ¡SACA a " + l2.getNombre() + "!" : " → No saca"));
            cPrincipal.getControlVentanaServidor().mostrarMensaje(
                    l2.getNombre() + " usa " + kimarite2
                    + (saca2 ? " → ¡SACA a " + l1.getNombre() + "!" : " → No saca"));

            if (saca1) l2.setDentroDelDohyo(false);
            if (saca2 && l1.isDentroDelDohyo()) l1.setDentroDelDohyo(false);
        }

        // Determinar ganador y perdedor
        boolean gana1 = l1.isDentroDelDohyo();
        ParHilo ganador = gana1 ? par1 : par2;
        ParHilo perdedor = gana1 ? par2 : par1;

        // Actualizar victorias en BD
        luchadorDAO.incrementarVictorias(ganador.getLuchador().getIdLuchador());
        ganador.getLuchador().setCombatesGanados(
                ganador.getLuchador().getCombatesGanados() + 1);

        cPrincipal.getControlVentanaServidor().mostrarMensaje(
                "GANADOR: " + ganador.getLuchador().getNombre()
                + " | Victorias: " + ganador.getLuchador().getCombatesGanados());

        // Persistir en archivo de acceso aleatorio
        guardarResultadoRaf(raf, ganador.getLuchador(), true);
        guardarResultadoRaf(raf, perdedor.getLuchador(), false);

        // Notificar a los clientes a través de sus hilos
        ganador.getHilo().notificarResultado(true);
        perdedor.getHilo().notificarResultado(false);

        return ganador;
    }

    /**
     * Selecciona aleatoriamente un kimarite del arreglo de técnicas del luchador.
     *
     * @param luchador Luchador del que se elige la técnica
     * @return Nombre del kimarite seleccionado
     */
    private String seleccionarKimariteAleatorio(LuchadorDTO luchador) {
        String[] kimarites = luchador.getKimarites();
        if (kimarites == null || kimarites.length == 0) {
            return "Oshidashi";
        }
        int indice = (int) (Math.random() * kimarites.length);
        return kimarites[indice];
    }

    /**
     * Determina con probabilidad controlada si un kimarite saca al rival del Dohyō.
     * La mayoría de los turnos (80%) no terminan el combate.
     *
     * @return true si el kimarite es efectivo, false si no
     */
    private boolean lanzarKimarite() {
        return Math.random() < PROBABILIDAD_SACAR;
    }

    /**
     * Genera una pausa aleatoria de entre 0 y 500 ms entre turnos,
     * simulando el tiempo de reacción de cada luchador.
     */
    private void esperar() {
        try {
            Thread.sleep((long) (Math.random() * 500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Escribe el registro de un luchador y su resultado en el archivo
     * de acceso aleatorio con tamaño fijo por registro.
     *
     * @param raf      Archivo de acceso aleatorio abierto
     * @param luchador Datos del luchador
     * @param gano     true si ganó, false si perdió
     * @throws IOException Si ocurre un error de escritura
     */
    private void guardarResultadoRaf(RandomAccessFile raf,
            LuchadorDTO luchador, boolean gano) throws IOException {

        raf.seek(raf.length());

        String resultado = gano ? "GANO" : "PERDIO";
        String registro = String.format("%-30s%-10.2f%-5d%-10s",
                luchador.getNombre(),
                luchador.getPeso(),
                luchador.getCombatesGanados(),
                resultado);

        // Ajustar al tamaño fijo con padding
        registro = String.format("%-" + TAMANIO_REGISTRO + "s",
                registro.length() > TAMANIO_REGISTRO
                ? registro.substring(0, TAMANIO_REGISTRO)
                : registro);

        raf.writeUTF(registro);
    }

    /**
     * Lee y muestra por consola y en la interfaz del servidor el contenido
     * completo del archivo de acceso aleatorio al finalizar todos los combates.
     *
     * @param raf Archivo de acceso aleatorio abierto
     * @throws IOException Si ocurre un error de lectura
     */
    private void mostrarArchivoResultados(RandomAccessFile raf) throws IOException {
        raf.seek(0);
        cPrincipal.getControlVentanaServidor().mostrarMensaje(
                "\n RESULTADOS FINALES");
        while (raf.getFilePointer() < raf.length()) {
            String linea = raf.readUTF().trim();
            System.out.println(linea);
            cPrincipal.getControlVentanaServidor().mostrarMensaje(linea);
        }
        cPrincipal.getControlVentanaServidor().mostrarGifInicio();
    }
}