
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

    /**
     * Metodo que busca un usuario por su nick en la BBDD.
     * @param nick del usuario.
     * @return usuario buscado.
     */
    public UsuarioModel buscarPorNick(String nick) {
        String sql = "SELECT * FROM usuarios WHERE nickname = ?";
        UsuarioModel usuario = null;
        try {
            conectar();
            try(PreparedStatement stmt = getConnection().prepareStatement(sql)) {
                stmt.setString(1, nick);
                try(ResultSet cursor = stmt.executeQuery()){
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            cerrar();
        }
        return usuario;
    }

    
    /**
     * Metodo que busca un usuario por su email en la BBDD.
     * @param email del usuario.
     * @return usuario buscado.
     */
    public UsuarioModel buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        UsuarioModel usuario = null;
        try {
            conectar();
            try(PreparedStatement stmt = getConnection().prepareStatement(sql)) {
                stmt.setString(1, email);
                try(ResultSet cursor = stmt.executeQuery()){
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            cerrar();
        }
        return usuario;
    }


    /**
     * Metodo que inserta un nuevo usuario en la BBDD.
     * @param usuario a insertar.
     * @return usuario insertado.
     */
    public boolean insertar(UsuarioModel usuario) {
        String sql = "INSERT INTO usuarios (nickname, email, password, racha, puntos, nivel) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conectar();
            try(PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, usuario.getNickname());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getPassword());
            preparedStatement.setInt(4, usuario.getRacha());
            preparedStatement.setInt(5, usuario.getPuntos());
            preparedStatement.setInt(6, usuario.getNivel());
            return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }


    /**
     * Metodo que actualiza los datos del usuario (nickname, email, password, racha, puntos, nivel).
     * @param usuario con los datos actualizados.
     * @return true si se actualizó correctamente.
     */
    public boolean actualizarUsuario(UsuarioModel usuario) {
        String sql = "UPDATE usuarios SET nickname = ?, email = ?, password = ?, racha = ?, puntos = ?, nivel = ? WHERE id = ?";
        try {
            conectar();
            try(PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, usuario.getNickname());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getPassword());
            preparedStatement.setInt(4, usuario.getRacha());
            preparedStatement.setInt(5, usuario.getPuntos());
            preparedStatement.setInt(6, usuario.getNivel());
            preparedStatement.setInt(7, usuario.getId());
            return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
        
    }


    /**
    * Metodo que elimina un usuario por su ID.
    * @param idUsuario del usuario a eliminar.
    * @return true si se eliminó correctamente.
    */
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try {
            conectar();
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, idUsuario);
            return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
        
    }


    /**
     * Metodo que actualiza los puntos y la racha acumulada del usuario.
     * @param usuario que juega.
     * @return puntos y racha actualizada del usuario.
     */
    public boolean actualizarPuntosYRacha(UsuarioModel usuario) {
        String sql = "UPDATE usuarios SET racha = ?, puntos = ? WHERE id = ?";

        try {
            conectar();
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, usuario.getRacha());
            preparedStatement.setInt(2, usuario.getPuntos());
            preparedStatement.setInt(3, usuario.getId());
            return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
        
    }

    /**
     * Metodo que actualiza la contrasenia antigua por la nueva al enviarse el correo.
     * El usuario solicita via email una nueva contrasenia.
     * @param email con la nueva contrasenia.
     * @param nuevaPassword del usuario.
     * @return nueva contrasenia.
     */
    public boolean actualizarPasswordPorEmail(String email, String nuevaPassword) {
        String sql = "UPDATE usuarios SET password = ? WHERE email = ?";
        boolean actualizada = false;
    
        try {
            conectar();
            try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
                stmt.setString(1, nuevaPassword); // Aquí podrías aplicar hashing si lo deseas
                stmt.setString(2, email);
                int filas = stmt.executeUpdate();
                actualizada = filas > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
    
        return actualizada;
    }

    /**
     * Metodo que obtiene el nivel del usuario de la BBDD.
     * @param idUsuario del usuario.
     * @return nivel del usuario.
     */
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



    /**
     * Metodo que actualiza el nivel en funcion a los puntos que el usuario vaya acumulando.
     * @param usuario del juego.
     * @return nivel actualizado.
     */
    public boolean actualizarNivel(UsuarioModel usuario) {
        String sql = "UPDATE usuarios SET nivel = ? WHERE id = ?";
        try {
            conectar();
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, usuario.getNivel());
            preparedStatement.setInt(2, usuario.getId());
            return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }


        /**
     * Método que actualiza la imagen de perfil del usuario.
     * @param idUsuario ID del usuario.
     * @param imagenBytes imagen convertida a byte[].
     * @return true si se actualiza correctamente.
     */
    public boolean actualizarImagen(int idUsuario, byte[] imagenBytes) {
        String sql = "UPDATE usuarios SET imagen_perfil = ? WHERE id = ?";
        try {
            conectar();
            try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
                stmt.setBytes(1, imagenBytes);
                stmt.setInt(2, idUsuario);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar();
        }
    }


    /**
 * Obtiene la imagen de perfil del usuario como byte[].
 * @param idUsuario ID del usuario.
 * @return imagen en bytes o null.
 */
public byte[] obtenerImagen(int idUsuario) {
    String sql = "SELECT imagen_perfil FROM usuarios WHERE id = ?";
    try {
        conectar();
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("imagen_perfil");
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
