/**
 * Created by ricardoramos on 5/28/16.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

import freemarker.template.Configuration;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import org.h2.tools.Server;
import java.lang.Integer;

public class main {

    public static void main(String[] arg) throws Exception{
        
        ProcessBuilder process = new ProcessBuilder();
        Integer port;
        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }

        setPort(port);

        Server server = null;
        try {
            server = Server.createTcpServer("-tcpAllowOthers").start();
        } catch (SQLException e) {
            System.out.println("FAILED TO START SERVER, CLOSE H2 IF YOU HAVE IT OPENED");
            e.printStackTrace();
        }
        System.out.println("fuck everything");
        Class.forName("org.h2.Driver");
        Connection connection =  DriverManager.getConnection("jdbc:h2:~/test","sa","");
        Statement statement = connection.createStatement();
        Configuration configuration=new Configuration();
        
        System.out.println("fuck everything2");
        statement.execute("CREATE TABLE ESTUDIANTE(MATRICULA INT PRIMARY KEY, NOMBRE VARCHAR(255),APELLIDO VARCHAR(255),TELEFONO VARCHAR(10))");
        
        System.out.println("fuck everything3");
        // String query = String.format("INSERT INTO ESTUDIANTE VALUES(%s,'%s','%s','%s')","20120576","nombre","apellido","telefono");
        // statement.execute(query);
        
        configuration.setClassForTemplateLoading(main.class, "/templates");
        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        get("/agregarEstudiante/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("title", "Agrega un Estudiante");
            attributes.put("edit", 0);

            return new ModelAndView(attributes, "addStudent.html");
        }, freeMarkerEngine);

        get("/editarEstudiante/:matricula", (request, response) -> {

            String matricula = request.params(":matricula");
            if(matricula.contains(","))
            {
                matricula = matricula.replace(",","");
            }
            ResultSet studentsQuery = statement.executeQuery(String.format("SELECT * FROM ESTUDIANTE WHERE matricula = %s LIMIT 1",matricula));
            student student = new student();

            while (studentsQuery.next()){

                student.setMatricula(matricula);
                student.setNombre(studentsQuery.getString("nombre"));
                student.setApellido(studentsQuery.getString("apellido"));
                student.setTelefono(studentsQuery.getString("telefono"));
            }

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("title", "Editar un Estudiante");
            attributes.put("student", student);
            return new ModelAndView(attributes, "updateStudent.html");
        }, freeMarkerEngine);

        get("/estudiante/:matricula", (request, response) -> {
            String matricula = request.params(":matricula");
            if(matricula.contains(","))
            {
                matricula = matricula.replace(",","");
            }
            ResultSet studentsQuery = statement.executeQuery(String.format("SELECT * FROM ESTUDIANTE WHERE matricula = %s LIMIT 1",matricula));

            student student = new student();

            while (studentsQuery.next()){
                student.setMatricula(studentsQuery.getString("matricula"));
                student.setNombre(studentsQuery.getString("nombre"));
                student.setApellido(studentsQuery.getString("apellido"));
                student.setTelefono(studentsQuery.getString("telefono"));
            }

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("student", student);
            return new ModelAndView(attributes, "viewStudent.html");
        }, freeMarkerEngine);

        get("/", (request, response) -> {

            ArrayList<student> students = new ArrayList<student>();
            ResultSet studentsQuery = statement.executeQuery("SELECT * FROM ESTUDIANTE");

            while(studentsQuery.next()){
                student aux = new student();
                aux.setMatricula(studentsQuery.getString("matricula"));
                aux.setNombre(studentsQuery.getString("nombre"));
                aux.setApellido(studentsQuery.getString("apellido"));
                aux.setTelefono(studentsQuery.getString("telefono"));

                students.add(aux);

            }
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("students", students);
            return new ModelAndView(attributes, "listStudents.ftl");
        }, freeMarkerEngine);

        post("/procesarFormulario/", (request, response) -> {

            int edit = Integer.parseInt(request.queryParams("edit"));

            String matricula = request.queryParams("matricula");

            if(matricula.contains(","))
            {
                matricula = matricula.replace(",","");
            }

            String nombre =request.queryParams("nombre");
            String apellido =request.queryParams("apellido");
            String telefono =request.queryParams("telefono");

            if (edit == 1){
                String query = String.format("UPDATE ESTUDIANTE SET nombre = '%s', apellido ='%s', telefono ='%s' WHERE matricula = %s",nombre,apellido,telefono,matricula);
                statement.execute(query);

            }
            else{
                String query = String.format("INSERT INTO ESTUDIANTE VALUES(%s,'%s','%s','%s')",matricula,nombre,apellido,telefono);
                statement.execute(query);
            }


            student student = new student();
            student.setMatricula(matricula);
            student.setNombre(nombre);
            student.setApellido(apellido);
            student.setTelefono(telefono);

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("student", student);

            return new ModelAndView(attributes, "viewStudent.html");
        }, freeMarkerEngine); //

        get("/borrar/:matricula", (request, response) -> {
            String matricula = request.params(":matricula");
            if(matricula.contains(","))
            {
                matricula = matricula.replace(",","");
            }

            statement.execute(String.format("DELETE FROM ESTUDIANTE WHERE matricula = %s",matricula));



            return new ModelAndView(null, "erased.html");
        }, freeMarkerEngine); //
    }
}
