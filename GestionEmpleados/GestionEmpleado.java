/*
 * @author Gabriel Garcia
 */
package gestionempleadosprueba;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

    /* >>>>> Clase abstracta Empleado <<<<< */
    abstract class Empleado {
    protected String nombre;
    protected int id;
    protected double sueldoBase;

    /* >>>>> CONSTRUCTOR <<<<< */
    public Empleado(String nombre, int id, double sueldoBase) {
        this.nombre = nombre;
        this.id = id;
        this.sueldoBase = sueldoBase;
    }

    /* >>>>> Método abstracto que calcula el sueldo del empleado <<<<< */
    public abstract double calcularSueldo();
}

/* >>>>> Clase EmpleadoPorHoras, hereda de Empleado <<<<< */
class EmpleadoPorHoras extends Empleado {
    private int horasTrabajadas;

    public EmpleadoPorHoras(String nombre, int id, double sueldoBase, int horasTrabajadas) {
        super(nombre, id, sueldoBase);
        this.horasTrabajadas = horasTrabajadas;
    }

    /* >>>>> Implementación del método calcularSueldo para EmpleadoPorHoras <<<<< */
    @Override
    public double calcularSueldo() {
        return sueldoBase * horasTrabajadas;
    }
}

/* >>>>> Clase EmpleadoAsalariado, hereda de Empleado <<<<< */
class EmpleadoAsalariado extends Empleado {
    public EmpleadoAsalariado(String nombre, int id, double sueldoBase) {
        super(nombre, id, sueldoBase);
    }

    /* >>>>> Implementación del método calcularSueldo para la EmpleadoAsalariado <<<<< */
    @Override
    public double calcularSueldo() {
        return sueldoBase;
    }
}

/* >>>>> Clase EmpleadoComision, hereda de Empleado <<<<< */
class EmpleadoComision extends Empleado implements Impuesto {
    private double ventasRealizadas;
    private double comision = 0.10; // Ejemplo de una comision del 10%
    private double impuesto = 0.12;// Ejemplo de un impuesto del 12%
    
    public EmpleadoComision(String nombre, int id, double sueldoBase, double ventasRealizadas) {
        super(nombre, id, sueldoBase);
        this.ventasRealizadas = ventasRealizadas;
        this.comision = comision;
        this.impuesto = impuesto;
    }

    /* >>>>> Implementación del método calcularSueldo para EmpleadoComision <<<<< */
    @Override
    public double calcularSueldo() {
        return sueldoBase + (comision * ventasRealizadas);
    }

    /* >>>>> Implementación del método calcularImpuesto de la interfaz Impuesto <<<<< */
    @Override
    public double calcularImpuesto() {
        
        return impuesto * calcularSueldo();
    }
}

/* >>>>> Interfaz Impuesto que contiene el método calcularImpuesto <<<<< */
interface Impuesto {
    double calcularImpuesto();
}

/* >>>>> Clase GestorEmpleados <<<<< */
class GestorEmpleados {
    private ArrayList<Empleado> empleados = new ArrayList<>();

    /* >>>>> Método para agregar un empleado a la lista <<<<< */
    public void agregarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    /* >>>>> Método para modificar los datos de un empleado <<<<< */
    public void modificarEmpleado(int indice, Empleado empleado) {
        empleados.set(indice, empleado);
    }

    /* >>>>> Método para eliminar un empleado de la lista <<<<< */
    public void eliminarEmpleado(int indice) {
        empleados.remove(indice);
    }

    /* >>>>> Método para obtener la lista de los empleados <<<<< */
    public ArrayList<Empleado> obtenerEmpleados() {
        return empleados;
    }
}

/* >>>>> CLASE MAIN <<<<< */
public class GestionEmpleado {
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        GestorEmpleados gestor = new GestorEmpleados();

        while (true) {
            System.out.println("\nMENU\n");
            System.out.println("1. Agregar Empleado");
            System.out.println("2. Modificar Empleado");
            System.out.println("3. Eliminar Empleado");
            System.out.println("4. Calcular Sueldo e Impuesto");
            System.out.println("5. Salir");
            System.out.print("\nSeleccione una opcion: ");

            try {
                int opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        agregarEmpleado(gestor, scanner);
                        break;
                    case 2:
                        modificarEmpleado(gestor, scanner);
                        break;
                    case 3:
                        eliminarEmpleado(gestor, scanner);
                        break;
                    case 4:
                        calcularSueldoEImpuesto(gestor, scanner);
                        break;
                    case 5:
                        System.out.println("Saliendo del programa.");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Opción invalida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un número.");
                
            }
        }
    }

    private static void agregarEmpleado(GestorEmpleados gestor, Scanner scanner) {
        System.out.print("Nombre del empleado: ");
        String nombre = scanner.nextLine();
        
        System.out.print("ID del empleado: ");
        int id = scanner.nextInt();
        
        System.out.print("Sueldo base del empleado: ");
        double sueldoBase = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("\nSeleccione el tipo de empleado: ");
        System.out.println("1. Empleado por Horas");
        System.out.println("2. Empleado Asalariado");
        System.out.println("3. Empleado por Comisión");
        
        int tipoEmpleado = scanner.nextInt();
        
        scanner.nextLine();

        Empleado empleado = null;

        switch (tipoEmpleado) {
            case 1:
                System.out.print("Horas trabajadas: ");
                int horasTrabajadas = -1;

                try {
                    horasTrabajadas = scanner.nextInt();
                    if (horasTrabajadas < 0) {
                        System.out.println("Las horas trabajadas deben ser un número positivo.");
                        return;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada no válida para horas trabajadas.");
                    scanner.nextLine(); // Limpiar el búfer de entrada
                    return;
                }

                empleado = new EmpleadoPorHoras(nombre, id, sueldoBase, horasTrabajadas);
                break;
            case 2:
                empleado = new EmpleadoAsalariado(nombre, id, sueldoBase);
                break;
            case 3:
                System.out.print("Ventas realizadas: ");
                double ventasRealizadas = scanner.nextDouble();
                empleado = new EmpleadoComision(nombre, id, sueldoBase, ventasRealizadas);
                break;
            default:
                System.out.println("Tipo de empleado no válido.");
                return;
        }

        gestor.agregarEmpleado(empleado);
        System.out.println("Empleado agregado con éxito.");
    }

    private static void modificarEmpleado(GestorEmpleados gestor, Scanner scanner) {
        System.out.print("ID del empleado a modificar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Empleado> empleados = gestor.obtenerEmpleados();
        int indice = -1;

        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).id == id) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            System.out.println("El ID del empleado no existe.");
            return;
        }

        agregarEmpleado(gestor, scanner);
        gestor.modificarEmpleado(indice, empleados.get(indice));
        System.out.println("Empleado modificado.");
    }

    private static void eliminarEmpleado(GestorEmpleados gestor, Scanner scanner) {
        System.out.print("Ingrese el ID del empleado a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Empleado> empleados = gestor.obtenerEmpleados();
        int indice = -1;

        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).id == id) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            System.out.println("El ID del empleado no existe.");
            return;
        }

        gestor.eliminarEmpleado(indice);
        System.out.println("Empleado eliminado con éxito.");
    }

    private static void calcularSueldoEImpuesto(GestorEmpleados gestor, Scanner scanner) {
        System.out.print("Ingrese el ID del empleado para calcular su sueldo e impuesto: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        ArrayList<Empleado> empleados = gestor.obtenerEmpleados();
        int indice = -1;

        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).id == id) {
                indice = i;
                break;
            }
        }

        if (indice == -1) {
            System.out.println("El ID del empleado no existe.");
            return;
        }

        Empleado empleado = empleados.get(indice);
        double sueldo = empleado.calcularSueldo();
        double impuesto = 0;

        if (empleado instanceof Impuesto) {
            Impuesto impuestoCalculable = (Impuesto) empleado;
            impuesto = impuestoCalculable.calcularImpuesto();
        }

        System.out.println("Sueldo del empleado: $" + sueldo);
        System.out.println("Impuesto a pagar: $" + impuesto);
    }
  
}