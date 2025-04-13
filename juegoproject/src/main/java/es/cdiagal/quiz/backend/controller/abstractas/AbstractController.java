package es.cdiagal.quiz.backend.controller.abstractas;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import es.cdiagal.quiz.backend.model.abstractas.Conexion;



public class AbstractController extends Conexion{

static final String PATH_DB =  "src/main/resources/database/quiz.db";

    private Properties propertiesLanguage;
    private static String idiomaActual = "español";
    
    public AbstractController() {
        super(PATH_DB);
    }
    
    //Getter y setter para el idioma actual
    public static String getIdiomaActual(){
        return idiomaActual;
    }
    public static void setIdiomaActual(String idioma){
        idiomaActual = idioma;
    }

    //Getter y setter de los properties para poder usarlos en otras ventanas
    public Properties getPropertiesLanguage() {
        return this.propertiesLanguage;
    }

    public void setPropertiesLanguage(Properties propertiesLanguage) {
        this.propertiesLanguage = propertiesLanguage;
    }

    /**
     * Metodo que carga el idioma que esta alojado en la ruta que se le asigna
     * @param nombreFichero
     * @param idioma
     * @return
     */
    public Properties loadLanguage(String nombreFichero, String idioma) {
        Properties properties = new Properties();
        if (nombreFichero == null || idioma == null){
            return properties;
        }
        
        String path = "src/main/resources/" + nombreFichero + "-" + idioma + ".properties";

        File file = new File(path);
        if (!file.exists() || !file.isFile()){
            System.out.println("Path: " + file.getAbsolutePath());
            return properties;
        }

        try (FileInputStream input =new FileInputStream(file)){
            InputStreamReader isr = new InputStreamReader(input, "UTF-8");
            properties.load(isr);
        } catch (Exception e) {
            e.getStackTrace();
        }
        return properties;
    }


}