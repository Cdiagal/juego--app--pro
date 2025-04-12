
package es.cdiagal.quiz.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.cdiagal.quiz.backend.model.abstractas.Conexion;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;

public class PartidaDAO extends Conexion {

    public PartidaDAO(String rutaBD) {
        super(rutaBD);
    }

    public boolean insertarPartida(PartidaModel partida) {
        String sql = "INSERT INTO partidas (id_usuario, puntuacion, aciertos, errores, dificultad, fecha) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, partida.getIdUsuario());
            preparedStatement.setInt(2, partida.getPuntuacion());
            preparedStatement.setInt(3, partida.getAciertos());
            preparedStatement.setInt(4, partida.getErrores());
            preparedStatement.setInt(5, partida.getDificultad());
            preparedStatement.setString(6, partida.getFecha().toString());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<PartidaModel> obtenerPartidasPorUsuario(int idUsuario) {
        List<PartidaModel> partidas = new ArrayList<>();
        String sql = "SELECT * FROM partidas WHERE id_usuario = ? ORDER BY fecha DESC";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idUsuario);
            ResultSet cursor = preparedStatement.executeQuery();
            while (cursor.next()) {
                PartidaModel partida = new PartidaModel(
                    cursor.getInt("id"),
                    cursor.getInt("id_usuario"),
                    cursor.getInt("puntuacion"),
                    cursor.getInt("aciertos"),
                    cursor.getInt("errores"),
                    cursor.getInt("dificultad"),
                    LocalDateTime.parse(cursor.getString("fecha"))
                );
                partidas.add(partida);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return partidas;
    }
}
