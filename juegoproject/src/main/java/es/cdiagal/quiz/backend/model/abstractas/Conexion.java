package  es.cdiagal.quiz.backend.model.abstractas;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Conexion{

    private String rutaArchivoBD;
    private Connection connection;

    public String getRutaArchivoBD() {
        return this.rutaArchivoBD;
    }

    public Connection getConnection(){
        try {
            if(connection == null){
                connection = DriverManager.getConnection("jdbc: sqlite:" + rutaArchivoBD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.connection;
    }


    //Constructor vacío.
    public Conexion(){

    }

    /**
     * Constructor con path de conexion.
     * @param unaRutaArchivoBD ruta de la bbdd.
     * @throws SQLException error controlado.
     */

    public Conexion(String unaRutaArchivoBD){

        if (unaRutaArchivoBD == null || unaRutaArchivoBD.isEmpty()){
            System.out.println("La base de datos es nula o está vacía.");
        }

        File file = new File(unaRutaArchivoBD);
        if (!file.exists()){
            System.out.println("No existe la base de datos: " + rutaArchivoBD);
        }

        this.rutaArchivoBD = unaRutaArchivoBD;
    }

    /**
     * Funcion que abre la conexion a la bbdd.
     * @return conexion.
     * @throws SQLException error controlado.
     */

    public Connection conectar() throws SQLException{
        if(connection == null || connection.isClosed()){
            connection = DriverManager.getConnection("jdbc:sqlite:" + rutaArchivoBD);
        }
        return connection;
    }

    public void cerrar(){
        try {
            if(connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

