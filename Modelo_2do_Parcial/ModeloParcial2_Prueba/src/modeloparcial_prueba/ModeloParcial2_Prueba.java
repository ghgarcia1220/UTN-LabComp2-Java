package modeloparcial_prueba;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// 1.
abstract class Persona{
    
    protected String nombre;
    protected int edad;
    protected int id;

    public Persona(String nombre, int edad) {
        this.nombre = nombre;
        this.edad = edad;
    }
    
}

// 2.
class Paciente extends Persona{
    
    protected String historialMedico;

    public Paciente(String nombre, int edad, String historialMedico) {
        super(nombre, edad);
        this.historialMedico = historialMedico;
    }
    
}

// 3.
class Doctor extends Persona{
    
    protected String especialidad;

    public Doctor(String nombre, int edad, String especialidad) {
        super(nombre, edad);
        this.especialidad = especialidad;
    }
    
}

// 4.
class Hospital{
    
    protected List<Paciente>listaPacientes;
    protected List<Doctor>listaDoctores;

    public Hospital() {
    }
    

    public Hospital(List<Paciente> listaPacientes, List<Doctor> listaDoctores) {
        this.listaPacientes = listaPacientes;
        this.listaDoctores = listaDoctores;
        conectarBD();
    }
    
    // Metodo para conectar desde la BD y consultar contenido al inicio
    public void conectarBD(){
        String url = "jdbc:mysql://localhost:3306/hospital_bd";
        String usuario = "root";
        String passwd = "";
        
        try{
            Connection conexion = DriverManager.getConnection(url, usuario, passwd);
            
            Statement statement = conexion.createStatement();
            
            String consulta = "SELECT*FROM pacientes";
            ResultSet resultado = statement.executeQuery(consulta);
            
            while(resultado.next()){
                String nombre = resultado.getString("nombre");
                int edad = resultado.getInt("edad");
                String historialMedico = resultado.getString("historial_medico");
                
                listaPacientes.add(new Paciente(nombre, edad, historialMedico));
            }
            
            consulta = "SELECT*FROM doctores";
            ResultSet resultado1 = statement.executeQuery(consulta);
            while(resultado1.next()){
                String nombre = resultado1.getNString("nombre");
                int edad = resultado1.getInt("edad");
                String especialidad = resultado1.getString("especialidad");
                
                listaDoctores.add(new Doctor(nombre, edad, especialidad));
            }
            
            conexion.close();
            statement.close();
            resultado.close();
            
        }catch(SQLException e){
            System.out.println("\nCONEXION FALLIDA\n");
        }
    }
    
    public void guardarBD(){
        
        String url = "jdbc:mysql://localhost:3306/hospital_bd";
        String usuario = "root";
        String passwd = "";
        
        // Guardar los pacientes
        try(Connection conexion = DriverManager.getConnection(url, usuario, passwd)){
            
            String agregarPaciente = "INSERT INTO pacientes (nombre, edad, historial_medico) VALUES (?, ?, ?)";
            try(PreparedStatement preparedStatement = conexion.prepareStatement(agregarPaciente)){
                for(Paciente paciente : listaPacientes){
                    preparedStatement.setString(1, paciente.nombre);
                    preparedStatement.setInt(2, paciente.edad);
                    preparedStatement.setString(3,paciente.historialMedico);
                    preparedStatement.executeUpdate();
                }
            }
        
            
            // Guardar los medicos
            String agregarDoctor = "INSERT INTO doctores(nombre, edad, especialidad) VALUES(?, ?, ?)";
            try(PreparedStatement preparedStatement = conexion.prepareStatement(agregarDoctor)){
                for(Doctor doctor : listaDoctores){
                    preparedStatement.setString(1, doctor.nombre);
                    preparedStatement.setInt(2, doctor.edad);
                    preparedStatement.setString(3, doctor.especialidad);
                    preparedStatement.executeUpdate();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        
    }
    
    // 5.
    public void agregarPaciente(Paciente paciente){
        listaPacientes.add(paciente);
        guardarBD();
    }
    
    // 6.
    public void mostrarListaPacientes(){
        for(Paciente paciente : listaPacientes){
            System.out.println("\nNombre: " + paciente.nombre + "\nEdad: " + paciente.edad + "\nHistorial Medico: " + paciente.historialMedico);
        }
    }
    
    // 7.
    public void asignarDoctorCabecera(Paciente paciente, Doctor doctor){
        paciente.nombre += " (Atendido por Dr/Dra: " +  doctor.nombre + ")";
        guardarBD();
    }
    
    // 8.
    public void mostrarPacientesEntreFechas(LocalDate fechaInicio, LocalDate fechaFin){
        String url = "jdbc:mysql://localhost:3306/hospital_bd";
        String usuario = "root";
        String passwd = "";
        
        try{
            Connection conexion = DriverManager.getConnection(url, usuario, passwd);
            String consulta = "SELECT nombre, edad, historial_medico, fecha_ingreso FROM pacientes WHERE fecha_ingreso BETWEEN ? AND ?";
            try (PreparedStatement preparedStatement = conexion.prepareStatement(consulta)) {
                preparedStatement.setDate(1, Date.valueOf(fechaInicio));
                preparedStatement.setDate(2, Date.valueOf(fechaFin));
                
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    System.out.println("Pacientes ingresados entre " + fechaInicio + " y " + fechaFin + ":");
                    while (resultSet.next()) {
                        String nombre = resultSet.getString("nombre");
                        int edad = resultSet.getInt("edad");
                        String historialMedico = resultSet.getString("historial_medico");
                        LocalDate fechaIngreso = resultSet.getDate("fecha_ingreso").toLocalDate();

                        System.out.println("Nombre: " + nombre + ", Edad: " + edad +
                                ", Historial: " + historialMedico + ", Fecha de ingreso: " + fechaIngreso);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 9.
    public void eliminarPaceitne(String nombrePaciente){
        Connection conexion = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establecer la conexión
            conexion = DriverManager.getConnection("jdbc:mysql://tu_host/tu_base_de_datos", "tu_usuario", "tu_contraseña");

            // Preparar la sentencia SQL para eliminar el paciente por nombre
            String sql = "DELETE FROM pacientes WHERE nombre = ?";
            preparedStatement = conexion.prepareStatement(sql);
            preparedStatement.setString(1, nombrePaciente);

            // Ejecutar la sentencia SQL
            int filasAfectadas = preparedStatement.executeUpdate();
            
            conexion.close();
            preparedStatement.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


public class ModeloParcial2_Prueba {

    
    public static void main(String[] args) {
        Hospital hospital = new Hospital();

        
        Paciente paciente1 = new Paciente("Ana", 30, "Historial de Ana");
        Doctor doctor1 = new Doctor("Mario", 40, "Especialidad Ginecologo");

        
        hospital.agregarPaciente(paciente1);
        hospital.agregarPaciente(new Paciente("Carlos", 25, "Historial de Carlos"));
        hospital.agregarPaciente(new Paciente("Patricia", 35, "Historial de Patricia"));
        hospital.agregarPaciente(new Paciente("Juan", 45, "Historial de Juan"));
        hospital.agregarPaciente(new Paciente("Micaela", 50, "Historial de Micaela"));

        hospital.mostrarListaPacientes();

        
        hospital.asignarDoctorCabecera(paciente1, doctor1);

        
        hospital.mostrarListaPacientes();
    }
    
}
