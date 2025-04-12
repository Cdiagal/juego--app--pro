
package es.cdiagal.quiz.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.cdiagal.quiz.backend.model.abstractas.Conexion;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;


/**
 * Clase que gestiona las funcionalidades de las partidas sobre la base de datos
 * @author cdiagal
 * @version 1.0.0
 */

public class PartidaDAO extends Conexion {
    /**
     * Constructor vacio.
     */
    public PartidaDAO(){}

    /**
     * Constructor que llama a la clase AbstractController y sus metodos.
     */
    public PartidaDAO(String rutaBD) {
        super(rutaBD);
    }

    /**
     * Metodo que obtiene una partida del usuario.
     */

    public List<PartidaModel> obtenerPartidasPorUsuario(int idUsuario) {
    List<PartidaModel> partidas = new ArrayList<>();
    String sql = "SELECT * FROM partidas WHERE id_usuario = ? ORDER BY fecha DESC";
        try {
            conectar();
            try (PreparedStatement PreparedStatement = getConnection().prepareStatement(sql)) {
                PreparedStatement.setInt(1, idUsuario);
                try (ResultSet cursor = PreparedStatement.executeQuery()) {
                    while (cursor.next()) {
                        partidas.add(new PartidaModel(
                            cursor.getInt("id"),
                            cursor.getInt("id_usuario"),
                            cursor.getInt("puntuacion"),
                            cursor.getInt("aciertos"),
                            cursor.getInt("errores"),
                            cursor.getInt("dificultad"),
                            LocalDateTime.parse(cursor.getString("fecha"))
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return partidas;
    }

    /**
     * Metodo que obtiene todas las partidas del usuario.
     */
    public List<PartidaModel> obtenerTodasLasPartidas() {
        List<PartidaModel> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas ORDER BY fecha DESC";
        try {
            conectar();
            try (PreparedStatement PreparedStatement = getConnection().prepareStatement(sql);
                ResultSet cursor = PreparedStatement.executeQuery()) {
                while (cursor.next()) {
                    partidas.add(new PartidaModel(
                        cursor.getInt("id"),
                        cursor.getInt("id_usuario"),
                        cursor.getInt("puntuacion"),
                        cursor.getInt("aciertos"),
                        cursor.getInt("errores"),
                        cursor.getInt("dificultad"),
                        LocalDateTime.parse(cursor.getString("fecha"))
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return partidas;
    }
    

    /**
     * Metodo que crea una partida nueva en la base de datos.
     */
    public boolean insertarPartida(PartidaModel partida) {
    String sql = "INSERT INTO partidas (id_usuario, puntuacion, aciertos, errores, dificultad, fecha) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conectar();
            try (PreparedStatement PreparedStatement = getConnection().prepareStatement(sql)) {
                PreparedStatement.setInt(1, partida.getIdUsuario());
                PreparedStatement.setInt(2, partida.getPuntuacion());
                PreparedStatement.setInt(3, partida.getAciertos());
                PreparedStatement.setInt(4, partida.getErrores());
                PreparedStatement.setInt(5, partida.getDificultad());
                PreparedStatement.setString(6, partida.getFecha().toString());
                return PreparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }


    /**
     * Metodo que actualiza la partida del usuario.
     * @param partida.
     * @return partida actualizada.
     */
    public boolean actualizarPartida(PartidaModel partida) {
        String sql = "UPDATE partidas SET puntuacion = ?, aciertos = ?, errores = ?, dificultad = ?, fecha = ? WHERE id = ?";
        try {
            conectar();
            try (PreparedStatement PreparedStatement = getConnection().prepareStatement(sql)) {
                PreparedStatement.setInt(1, partida.getPuntuacion());
                PreparedStatement.setInt(2, partida.getAciertos());
                PreparedStatement.setInt(3, partida.getErrores());
                PreparedStatement.setInt(4, partida.getDificultad());
                PreparedStatement.setString(5, partida.getFecha().toString());
                PreparedStatement.setInt(6, partida.getId());
                return PreparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }
    

    /**
     * Metodo que elimina una partida.
     * @param id de la partida.
     * @return partida eliminada.
     */
    public boolean eliminarPartida(int id) {
        String sql = "DELETE FROM partidas WHERE id = ?";
        try {
            conectar();
            try (PreparedStatement PreparedStatement = getConnection().prepareStatement(sql)) {
                PreparedStatement.setInt(1, id);
                return PreparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }
    
    /**
     * Metodo que obtiene una partida activa para continuarla o finalizarla.
     * @param id del usuario.
     * @return partida activa.
     */
    public PartidaModel obtenerPartidaActiva(int idUsuario) {
        String sql = "SELECT * FROM partidas WHERE id_usuario = ? ORDER BY fecha DESC LIMIT 1";
        try {
            conectar();
            try (PreparedStatement PreparedStatement = getConnection().prepareStatement(sql)) {
                PreparedStatement.setInt(1, idUsuario);
                try (ResultSet cursor = PreparedStatement.executeQuery()) {
                    if (cursor.next()) {
                        LocalDateTime fecha = LocalDateTime.parse(cursor.getString("fecha"));
                        Duration duracion = Duration.between(fecha, LocalDateTime.now());
    
                        if (duracion.toMinutes() < 1) {
                            return new PartidaModel(
                                cursor.getInt("id"),
                                cursor.getInt("id_usuario"),
                                cursor.getInt("puntuacion"),
                                cursor.getInt("aciertos"),
                                cursor.getInt("errores"),
                                cursor.getInt("dificultad"),
                                fecha
                            );
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return null;
    }
    
}
