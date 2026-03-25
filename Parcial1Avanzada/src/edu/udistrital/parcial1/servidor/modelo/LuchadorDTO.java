package edu.udistrital.parcial1.servidor.modelo;

/**
 * DTO que representa a un luchador de sumo.
 * Transporta los datos del luchador entre las capas del sistema.
 * No contiene lógica de negocio.
 *
 * @author Nath
 */
public class LuchadorDTO {

    /** Identificador único generado por la base de datos. */
    private int idLuchador;

    /** Nombre del luchador. */
    private String nombre;

    /** Peso del luchador en kilogramos. */
    private double peso;

    /** Número de combates ganados acumulados. */
    private int combatesGanados;

    /** Arreglo de técnicas (kimarites) que domina el luchador. */
    private String[] kimarites;

    /** Referencia al luchador rival en el combate actual. */
    private LuchadorDTO rival;

    /** Estado que indica si el luchador sigue dentro del dohyō. */
    private boolean dentroDelDohyo;

    /**
     * Constructor completo del luchador (usado al recuperar desde BD).
     *
     * @param idLuchador     Identificador de BD
     * @param nombre         Nombre del luchador
     * @param peso           Peso en kg
     * @param combatesGanados Victorias acumuladas
     * @param kimarites      Técnicas del luchador
     * @param rival          Luchador rival
     * @param dentroDelDohyo Estado dentro/fuera del dohyō
     */
    public LuchadorDTO(int idLuchador, String nombre, double peso,
            int combatesGanados, String[] kimarites,
            LuchadorDTO rival, boolean dentroDelDohyo) {
        this.idLuchador = idLuchador;
        this.nombre = nombre;
        this.peso = peso;
        this.combatesGanados = combatesGanados;
        this.kimarites = kimarites;
        this.rival = rival;
        this.dentroDelDohyo = dentroDelDohyo;
    }

    /**
     * Constructor sin id (usado al registrar un nuevo luchador desde el cliente).
     *
     * @param nombre         Nombre del luchador
     * @param peso           Peso en kg
     * @param combatesGanados Victorias acumuladas
     * @param kimarites      Técnicas del luchador
     */
    public LuchadorDTO(String nombre, double peso, int combatesGanados, String[] kimarites) {
        this.nombre = nombre;
        this.peso = peso;
        this.combatesGanados = combatesGanados;
        this.kimarites = kimarites;
        this.rival = null;
        this.dentroDelDohyo = true;
    }

    //Getters 

    /**
     * Obtiene el identificador de BD del luchador.
     * @return id del luchador
     */
    public int getIdLuchador() { return idLuchador; }

    /**
     * Obtiene el nombre del luchador.
     * @return nombre
     */
    public String getNombre() { return nombre; }

    /**
     * Obtiene el peso del luchador.
     * @return peso en kg
     */
    public double getPeso() { return peso; }

    /**
     * Obtiene la cantidad de combates ganados.
     * @return combates ganados
     */
    public int getCombatesGanados() { return combatesGanados; }

    /**
     * Obtiene el arreglo de kimarites del luchador.
     * @return arreglo de técnicas
     */
    public String[] getKimarites() { return kimarites; }

    /**
     * Obtiene el luchador rival.
     * @return rival
     */
    public LuchadorDTO getRival() { return rival; }

    /**
     * Indica si el luchador está dentro del dohyō.
     * @return true si está dentro, false si salió
     */
    public boolean isDentroDelDohyo() { return dentroDelDohyo; }

    //Setters 

    /**
     * Asigna el identificador de BD.
     * @param idLuchador id generado por la BD
     */
    public void setIdLuchador(int idLuchador) { this.idLuchador = idLuchador; }

    /**
     * Asigna el nombre del luchador.
     * @param nombre nombre del luchador
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Asigna el peso del luchador.
     * @param peso peso en kg
     */
    public void setPeso(double peso) { this.peso = peso; }

    /**
     * Asigna la cantidad de combates ganados.
     * @param combatesGanados número de victorias
     */
    public void setCombatesGanados(int combatesGanados) { this.combatesGanados = combatesGanados; }

    /**
     * Asigna el arreglo de kimarites.
     * @param kimarites técnicas del luchador
     */
    public void setKimarites(String[] kimarites) { this.kimarites = kimarites; }

    /**
     * Asigna el rival del luchador.
     * @param rival luchador oponente
     */
    public void setRival(LuchadorDTO rival) { this.rival = rival; }

    /**
     * Asigna el estado dentro/fuera del dohyō.
     * @param dentroDelDohyo true si está dentro
     */
    public void setDentroDelDohyo(boolean dentroDelDohyo) { this.dentroDelDohyo = dentroDelDohyo; }
}