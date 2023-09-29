import java.io.*;
import java.util.*;

class Persona {
    private String nombre;
    private String dni;
    private String fechaNacimiento;

    public Persona(String nombre, String dni, String fechaNacimiento) {
        this.nombre = nombre;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + "\nDNI: " + dni + "\nFecha de Nacimiento: " + fechaNacimiento;
    }

}

class Doctor extends Persona {
    private String especialidad;

    public Doctor(String nombre, String dni, String fechaNacimiento, String especialidad) {
        super(nombre, dni, fechaNacimiento);
        this.especialidad = especialidad;
    }

    // Getter y setter
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return super.toString() + "\nEspecialidad: " + especialidad;
    }

}

interface Informacion {
    void verHistorialDeEventos();
}

class Paciente extends Persona implements Informacion {
    private long telefono;
    private int tipoSangre;
    private List<String> historialMedico;

    public Paciente(String nombre, String dni, String fechaNacimiento, long telefono, int tipoSangre) {
        super(nombre, dni, fechaNacimiento);
        this.telefono = telefono;
        this.tipoSangre = tipoSangre;
        this.historialMedico = new ArrayList<>();
    }

    // Getters y setters
    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public int getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(int tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public List<String> getHistorialMedico() {
        return historialMedico;
    }

    public void setHistorialMedico(List<String> historialMedico) {
        this.historialMedico = historialMedico;
    }

    public void agregarEventoHistorial(String evento) {
        String fechaActual = obtenerFechaActual();
        historialMedico.add(fechaActual + " - " + evento);
    }

    @Override
    public void verHistorialDeEventos() {
        for (String evento : historialMedico) {
            System.out.println(evento);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "\nTeléfono: " + telefono + "\nTipo de Sangre: " + tipoSangre;
    }

    private String obtenerFechaActual() {
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH) + 1;
        int anio = calendar.get(Calendar.YEAR);
        return String.format("%02d/%02d/%04d", dia, mes, anio);
    }

    public String getDni() {
        return null;
    }
}

class HospitalPerrando {
    private static List<Doctor> doctores = new ArrayList<>();
    private static List<Paciente> pacientes = new ArrayList<>();
    private static String datosHospital;

    public static void main(String[] args) {
        cargarDatosDeContacto();
        cargarDoctores();
        cargarPacientes();

        Scanner scanner = new Scanner(System.in);

        int opcion;
        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    listarDoctores();
                    break;
                case 2:
                    registrarNuevoPaciente(scanner);
                    break;
                case 3:
                    actualizarInformacionPaciente(scanner);
                    break;
                case 4:
                    nuevoHistorialMedico(scanner);
                    break;
                case 5:
                    guardarHistorialPacientesEnArchivo();
                    break;
                case 6:
                    cargarHistorialPacientesDesdeArchivo();
                    break;
                case 7:
                    System.out.println("\n|-- Hasta luego --|\n");
                    break;
                default:
                    System.out.println("Opción no válida. Introduce un número del 1 al 7.");
            }
        } while (opcion != 7);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\tHospital Julio C. Perrando - Av. 9 de Julio 1100 · 0362 444-2399\n");
        System.out.println("MENU:");
        System.out.println("1. Listar Doctores.");
        System.out.println("2. Registrar un nuevo paciente.");
        System.out.println("3. Actualizar información personal de un paciente.");
        System.out.println("4. Nuevo historial para un paciente.");
        System.out.println("5. Guardar Historial de pacientes en archivo.");
        System.out.println("6. Cargar Historial de pacientes desde archivo.");
        System.out.println("7. Salir.");
        System.out.print(">>> Seleccionar una opción: ");
    }

    private static void listarDoctores() {
        System.out.println(">>>> Listado de Doctores <<<<");
        for (Doctor doctor : doctores) {
            System.out.println(doctor);
            System.out.println("=======================================");
        }
    }

    private static void registrarNuevoPaciente(Scanner scanner) {
        System.out.println("Registro de nuevo paciente:");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("DNI: ");
        String dni = scanner.nextLine();
        System.out.print("Fecha de Nacimiento (dd/MM/yyyy): ");
        String fechaNacimiento = scanner.nextLine();
        System.out.print("Teléfono: ");
        long telefono = scanner.nextLong();
        System.out.print("Tipo de Sangre (1: A, 2: B, 3: AB, 4: O): ");
        int tipoSangre = scanner.nextInt();

        Paciente nuevoPaciente = new Paciente(nombre, dni, fechaNacimiento, telefono, tipoSangre);
        pacientes.add(nuevoPaciente);

        System.out.println("Paciente registrado exitosamente.");
    }

    private static void actualizarInformacionPaciente(Scanner scanner) {
        System.out.print("Introduce el DNI del paciente a buscar: ");
        String dniBuscado = scanner.nextLine();

        Paciente pacienteEncontrado = null;
        for (Paciente paciente : pacientes) {
            if (paciente.getDni().equals(dniBuscado)) {
                pacienteEncontrado = paciente;
                break;
            }
        }

        if (pacienteEncontrado != null) {
            System.out.println("Información actual del paciente:");
            System.out.println(pacienteEncontrado);

            System.out.println("Introduce la nueva información del paciente:");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Fecha de Nacimiento (dd/MM/yyyy): ");
            String fechaNacimiento = scanner.nextLine();
            System.out.print("Teléfono: ");
            long telefono = scanner.nextLong();
            System.out.print("Tipo de Sangre (1: A, 2: B, 3: AB, 4: O): ");
            int tipoSangre = scanner.nextInt();

            pacienteEncontrado = new Paciente(nombre, dniBuscado, fechaNacimiento, telefono, tipoSangre);
            pacientes.removeIf(p -> p.getDni().equals(dniBuscado));
            pacientes.add(pacienteEncontrado);

            System.out.println("Información actualizada.");
        } else {
            System.out.println("Paciente no encontrado.");
        }
    }

    private static void nuevoHistorialMedico(Scanner scanner) {
        System.out.print("Introduce el DNI del paciente al que se le agregará el historial: ");
        String dniBuscado = scanner.nextLine();

        Paciente pacienteEncontrado = null;
        for (Paciente paciente : pacientes) {
            if (paciente.getDni().equals(dniBuscado)) {
                pacienteEncontrado = paciente;
                break;
            }
        }

        if (pacienteEncontrado != null) {
            System.out.print("Introduce la observación del historial médico: ");
            String observacion = scanner.nextLine();

            pacienteEncontrado.agregarEventoHistorial(observacion);

            System.out.println("Historial médico actualizado.");
        } else {
            System.out.println("Paciente no encontrado.");
        }
    }

    private static void cargarDatosDeContacto() {
        try {
            File file = new File("datos.txt");
            Scanner scanner = new Scanner(file);
            StringBuilder datos = new StringBuilder(); // Crea y manipula cadenas de caracteres
            while (scanner.hasNextLine()) {
                datos.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            datosHospital = datos.toString();
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo cargar la información de contacto del hospital.");
        }
    }

    private static void cargarDoctores() {
        // Agrega medicos al sistema
        Doctor doc1 = new Doctor("Dra. Fabiola Alvarez", "31646582", "13/07/1985", "Odontologia");
        Doctor doc2 = new Doctor("Dra. Eleonora Aleman", "36756976", "19/02/1992", "Pediatría");
        Doctor doc3 = new Doctor("Dr. Leadro Campos", "28428078", "11/09/1980", "Cardiologia");
        Doctor doc4 = new Doctor("Dr. Tomas Gaitan", "40310656", "22/04/1998", "Fonoaudiologia");
        
        doctores.add(doc1);
        doctores.add(doc2);
        doctores.add(doc3);
        doctores.add(doc4);
    }

    private static void cargarPacientes() {
        
    }

    private static void guardarHistorialPacientesEnArchivo() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("pacientes.txt"));
            outputStream.writeObject(pacientes);
            outputStream.close();
            System.out.println("Historial de pacientes guardado exitosamente en pacientes.txt.");
        } catch (IOException e) {
            System.out.println("Error al guardar el historial de pacientes.");
        }
    }

    private static void cargarHistorialPacientesDesdeArchivo() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("pacientes.txt"));
            pacientes = (List<Paciente>) inputStream.readObject();
            inputStream.close();
            System.out.println("Historial de pacientes cargado exitosamente desde pacientes.txt.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No se pudo cargar el historial de pacientes.");
        }
    }
}