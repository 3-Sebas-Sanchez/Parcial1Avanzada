/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.udistrital.parcial1.servidor.modelo.DAO;

import edu.udistrital.parcial1.servidor.modelo.Conexion.ConexionBaseDeDatos;
import edu.udistrital.parcial1.servidor.modelo.LuchadorDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO encargado de todas las operaciones de acceso a datos
 * de la entidad {@link LuchadorDTO} en la base de datos.
 * Aplica el patrón DAO para desacoplar la persistencia del resto del sistema.
 *
 * @author sebas
 */
public class LuchadorDAO {

    /**
     * Inserta un nuevo luchador en la base de datos.
     * Los kimarites NO se guardan en BD; viven en el archivo .properties.
     *
     * @param luchador DTO con los datos del luchador a registrar
     * @return true si la inserción fue exitosa, false en caso contrario
     */
    public boolean insertar(LuchadorDTO luchador) {
        String sql = "INSERT INTO luchador (nombre, peso, victorias) VALUES (?, ?, ?)";

        try (Connection cn = ConexionBaseDeDatos.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, luchador.getNombre());
            ps.setDouble(2, luchador.getPeso());
            ps.setInt(3, luchador.getCombatesGanados());

            int filas = ps.executeUpdate();

            // Recupera el id generado por AUTO_INCREMENT y lo asigna al DTO
            if (filas > 0) {
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        luchador.setIdLuchador(claves.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el luchador: " + luchador.getNombre(), e);
        }

        return false;
    }

    /**
     * Obtiene todos los luchadores registrados en la base de datos.
     *
     * @return Lista de {@link LuchadorDTO} con todos los luchadores
     */
    public List<LuchadorDTO> obtenerTodos() {
        String sql = "SELECT id_luchador, nombre, peso, victorias FROM luchador";
        List<LuchadorDTO> lista = new ArrayList<>();

        try (Connection cn = ConexionBaseDeDatos.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LuchadorDTO dto = new LuchadorDTO(
                        rs.getInt("id_luchador"),
                        rs.getString("nombre"),
                        rs.getDouble("peso"),
                        rs.getInt("victorias"),
                        new String[0],   // kimarites se cargan desde .properties
                        null,            // rival se asigna en tiempo de combate
                        true             // al recuperar, se asume dentro del dohyō
                );
                lista.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los luchadores", e);
        }

        return lista;
    }

    /**
     * Obtiene un luchador específico por su identificador en la BD.
     *
     * @param idLuchador Identificador del luchador
     * @return {@link LuchadorDTO} encontrado, o null si no existe
     */
    public LuchadorDTO obtenerPorId(int idLuchador) {
        String sql = "SELECT id_luchador, nombre, peso, victorias FROM luchador WHERE id_luchador = ?";

        try (Connection cn = ConexionBaseDeDatos.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idLuchador);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LuchadorDTO(
                            rs.getInt("id_luchador"),
                            rs.getString("nombre"),
                            rs.getDouble("peso"),
                            rs.getInt("victorias"),
                            new String[0],
                            null,
                            true
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el luchador con id: " + idLuchador, e);
        }

        return null;
    }

    /**
     * Incrementa en uno las victorias del luchador indicado.
     * Se llama al terminar cada combate con el luchador ganador.
     *
     * @param idLuchador Identificador del luchador ganador
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean incrementarVictorias(int idLuchador) {
        String sql = "UPDATE luchador SET victorias = victorias + 1 WHERE id_luchador = ?";

        try (Connection cn = ConexionBaseDeDatos.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idLuchador);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar victorias del luchador id: " + idLuchador, e);
        }
    }

    /**
     * Verifica si ya existe un luchador con el nombre dado en la BD.
     * Útil para evitar registros duplicados.
     *
     * @param nombre Nombre a verificar
     * @return true si el nombre ya existe, false si está disponible
     */
    public boolean existeNombre(String nombre) {
        String sql = "SELECT COUNT(*) FROM luchador WHERE nombre = ?";

        try (Connection cn = ConexionBaseDeDatos.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia del nombre: " + nombre, e);
        }

        return false;
    }

    /**
     * Cuenta cuántos luchadores hay registrados en la BD.
     * Se usa para verificar que haya al menos 6 antes de iniciar combates.
     *
     * @return Número total de luchadores registrados
     */
    public int contarLuchadores() {
        String sql = "SELECT COUNT(*) FROM luchador";

        try (Connection cn = ConexionBaseDeDatos.getConexion();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar luchadores en la BD", e);
        }

        return 0;
    }
}