
package es.cdiagal.quiz.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.cdiagal.quiz.backend.model.abstractas.Conexion;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;

public class UsuarioDAO extends Conexion {

    public UsuarioDAO(String rutaBD) {
        super(rutaBD);
    }

    public UsuarioModel buscarPorNick(String nick) {
        String sql = "SELECT * FROM usuarios WHERE nickname = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, nick);
            ResultSet cursor = stmt.executeQuery();
            if (cursor.next()) {
                return new UsuarioModel(
                    cursor.getInt("id"),
                    cursor.getString("nickname"),
                    cursor.getString("email"),
                    cursor.getString("password"),
                    cursor.getInt("racha"),
                    cursor.getInt("puntos"),
                    cursor.getInt("nivel")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertar(UsuarioModel usuario) {
        String sql = "INSERT INTO usuarios (nickname, email, password, racha, puntos) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, usuario.getNickname());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getPassword());
            preparedStatement.setInt(4, usuario.getRacha());
            preparedStatement.setInt(5, usuario.getPuntos());
            preparedStatement.setInt(6, usuario.getNivel());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarPuntosYRacha(UsuarioModel usuario) {
        String sql = "UPDATE usuarios SET racha = ?, puntos = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, usuario.getRacha());
            preparedStatement.setInt(2, usuario.getPuntos());
            preparedStatement.setInt(3, usuario.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public int obtenerNivel(int idUsuario) {
        String sql = "SELECT nivel FROM usuarios WHERE id = ?";
        try {
            conectar();
            try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
                stmt.setInt(1, idUsuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("nivel");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return 1; // Nivel por defecto si no se encuentra el usuario
    }


    public boolean actualizarNivel(UsuarioModel usuario) {
        String sql = "UPDATE usuarios SET nivel = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, usuario.getNivel());
            preparedStatement.setInt(2, usuario.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
