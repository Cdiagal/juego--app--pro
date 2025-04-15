
package es.cdiagal.quiz.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.cdiagal.quiz.backend.model.abstractas.Conexion;
import es.cdiagal.quiz.backend.model.entities.PreguntaModel;

public class PreguntaDAO extends Conexion {

    public PreguntaDAO(String rutaBD) {
        super(rutaBD);
    }

    public List<PreguntaModel> obtenerPreguntasPorDificultad(int dificultad, int cantidad) {
        List<PreguntaModel> preguntas = new ArrayList<>();
        String sql = "SELECT id, enunciado, opcionA, opcionB, opcionC, opcionD, respuesta_correcta, dificultad " +
                        "FROM preguntas WHERE dificultad = ? ORDER BY RANDOM() LIMIT ?";
        
        try {
            conectar();
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, dificultad);
                preparedStatement.setInt(2, cantidad);
                try (ResultSet cursor = preparedStatement.executeQuery()){

                    while (cursor.next()) {
                        PreguntaModel pregunta = new PreguntaModel(
                            cursor.getInt("id"),
                            cursor.getString("enunciado"),
                            cursor.getString("opcionA"),
                            cursor.getString("opcionB"),
                            cursor.getString("opcionC"),
                            cursor.getString("opcionD"),
                            cursor.getString("respuesta_correcta"),
                            cursor.getInt("dificultad")
                        );
                        preguntas.add(pregunta);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar();
        }
        return preguntas;
    }


    public boolean insertarPregunta(PreguntaModel pregunta) {
        String sql = "INSERT INTO preguntas (enunciado, opcionA, opcionB, opcionC, opcionD, respuesta_correcta, dificultad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, pregunta.getEnunciado());
            preparedStatement.setString(2, pregunta.getOpcionA());
            preparedStatement.setString(3, pregunta.getOpcionB());
            preparedStatement.setString(4, pregunta.getOpcionC());
            preparedStatement.setString(5, pregunta.getOpcionD());
            preparedStatement.setString(6, pregunta.getRespuestaCorrecta());
            preparedStatement.setInt(7, pregunta.getDificultad());
            return preparedStatement.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
