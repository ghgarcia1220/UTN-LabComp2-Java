package sistemadeventas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 1. Crear la Clase DBHelper
class DBHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/ventas";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Método para obtener una conexión a la base de datos
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // Metodo para ejecutar una consulta sin devolver resultados
    public static void ejecutarConsulta(String consulta) {
        try {
            
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // Crear la declaracion
            try (PreparedStatement statement = connection.prepareStatement(consulta)) {
                // Ejecutar la consulta
                statement.executeUpdate();
            }

            // Cerrar la conexion
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet ejecutarConsultaConResultado(String consulta) {
        try {
          
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            
            PreparedStatement statement = connection.prepareStatement(consulta);

            
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    }
 // 2. Creación de las Clases Producto y Vendedor con Atributos Equivalentes de la Base de Datos
class Producto {
    private int producto_id;
    private String nombre;
    private double precio_por_unidad;
    private int stock;

    public Producto(int producto_id, String nombre, double precio_por_unidad, int stock) {
        this.producto_id = producto_id;
        this.nombre = nombre;
        this.precio_por_unidad = precio_por_unidad;
        this.stock = stock;
    }

    // Métodos getter y setter para los atributos
}

class Vendedor {
    private int vendedor_id;
    private String nombre;
    private String apellido;
    private String dni;
    private String fecha_nacimiento;
    private String fecha_contratacion;

    public Vendedor(int vendedor_id, String nombre, String apellido, String dni, String fecha_nacimiento, String fecha_contratacion) {
        this.vendedor_id = vendedor_id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fecha_nacimiento = fecha_nacimiento;
        this.fecha_contratacion = fecha_contratacion;
    }
    
    // 3. Constructor adicional que contiene la consulta SQL
    public Vendedor(String consultaBusqueda) {
        try {
            
            Connection connection = DBHelper.getConnection();

            
            Statement statement = connection.createStatement();

            
            ResultSet resultSet = statement.executeQuery(consultaBusqueda);

            
            if (resultSet.next()) {
                // Cargar los datos del vendedor desde el resultado de la consulta
                this.vendedor_id = resultSet.getInt("vendedor_id");
                this.nombre = resultSet.getString("nombre");
                this.apellido = resultSet.getString("apellido");
                this.dni = resultSet.getString("dni");
                this.fecha_nacimiento = resultSet.getString("fecha_nacimiento");
                this.fecha_contratacion = resultSet.getString("fecha_contratacion");
            }

            // Cerrar recursos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // getter y setter
    public int getVendedor_id() {
        return vendedor_id;
    }

    public void setVendedor_id(int vendedor_id) {
        this.vendedor_id = vendedor_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getFecha_contratacion() {
        return fecha_contratacion;
    }

    public void setFecha_contratacion(String fecha_contratacion) {
        this.fecha_contratacion = fecha_contratacion;
    }
    
}

// 4. Obtener los datos de un Vendedor
class Comerciales {
    public static Vendedor obtenerVendedorPorID(int vendedorID) {
        Vendedor vendedor = null;

        try {
            // Establecer la conexión a la base de datos
            Connection connection = DBHelper.getConnection();

            // Crear la consulta SQL
            String consulta = "SELECT * FROM vendedores WHERE vendedor_id = " + vendedorID;

            // Crear una declaración
            Statement statement = connection.createStatement();

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery(consulta);

            // Verificar si hay resultados
            if (resultSet.next()) {
                // Crear un objeto Vendedor con los datos obtenidos
                vendedor = new Vendedor(
                        resultSet.getInt("vendedor_id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getString("dni"),
                        resultSet.getString("fecha_nacimiento"),
                        resultSet.getString("fecha_contratacion")
                );
            }

            // Cerrar recursos
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendedor;
    }
    
    //8. Obtener el Listado de Vendedores
    public static ArrayList<Vendedor> listadoDeVendedores() {
        ArrayList<Vendedor> vendedores = new ArrayList<>();

        try {
            
            Connection connection = DBHelper.getConnection();

            
            String consulta = "SELECT * FROM vendedores";

            
            try (Statement statement = connection.createStatement()) {
                
                ResultSet resultSet = statement.executeQuery(consulta);

               
                while (resultSet.next()) {
                    Vendedor vendedor = new Vendedor(
                            resultSet.getInt("vendedor_id"),
                            resultSet.getString("nombre"),
                            resultSet.getString("apellido"),
                            resultSet.getString("dni"),
                            resultSet.getString("fecha_nacimiento"),
                            resultSet.getString("fecha_contratacion")
                    );

                    
                    vendedores.add(vendedor);
                }

                
                resultSet.close();
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vendedores;
    }

}

// 5. Generación de Informe de Productos en Stock
class Productos {
    public static void generarInforme() {
        try {
            
            Connection connection = DBHelper.getConnection();

            
            String consulta = "SELECT * FROM productos";

            
            try (Statement statement = connection.createStatement()) {
                
                ResultSet resultSet = statement.executeQuery(consulta);

                
                System.out.println("Informe de Productos en Stock:");
                System.out.printf("%-20s %-10s %-10s %-10s%n", "Producto", "Stock", "Precio", "Total");
                System.out.println("----------------------------------------------------------");

               
                double valorTotalTodosProductos = 0;

               
                while (resultSet.next()) {
                    String nombreProducto = resultSet.getString("nombre");
                    int stock = resultSet.getInt("stock");
                    double precioPorUnidad = resultSet.getDouble("precio_por_unidad");

                    
                    double valorTotalProducto = stock * precioPorUnidad;

                    
                    System.out.printf("%-20s %-10d %-10.2f %-10.2f%n", nombreProducto, stock, precioPorUnidad, valorTotalProducto);

                    
                    valorTotalTodosProductos += valorTotalProducto;
                }

                
                System.out.println("----------------------------------------------------------");

                
                System.out.printf("Total: %.2f%n", valorTotalTodosProductos);

                
                resultSet.close();
            }

            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 6. Obtener Producto por ID
    public static Producto obtenerProducto(int productoID) {
        Producto producto = null;

        try {
            
            Connection connection = DBHelper.getConnection();

            
            String consulta = "SELECT * FROM productos WHERE producto_id = ?";

           
            try (PreparedStatement preparedStatement = connection.prepareStatement(consulta)) {
                // Establecer el valor del parámetro
                preparedStatement.setInt(1, productoID);

                
                ResultSet resultSet = preparedStatement.executeQuery();

                
                if (resultSet.next()) {
                    
                    producto = new Producto(
                            resultSet.getInt("producto_id"),
                            resultSet.getString("nombre"),
                            resultSet.getDouble("precio_por_unidad"),
                            resultSet.getInt("stock")
                    );
                }

                
                resultSet.close();
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }

    // 7. Obtener el Producto Más Vendido
    public static Producto obtenerProductoMasVendido() {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = DBHelper.getConnection();

            // Consulta SQL para obtener el producto más vendido
            String consulta = "SELECT producto_id, SUM(cantidad_vendida) as total_vendido " +
                              "FROM ventas ";

            // Crear una declaración
            try (Statement statement = connection.createStatement()) {
                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery(consulta);

                
                if (resultSet.next()) {
                    // Obtiene el ID del producto más vendido
                    int productoID = resultSet.getInt("producto_id");

                    return obtenerProducto(productoID);
                }
            }

            // Cerrar la conexión
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}


public class SistemaDeVentas {

    public static void main(String[] args) {
        Vendedor vendedor = Comerciales.obtenerVendedorPorID(1);
        if (vendedor != null) {
            System.out.println("Información del vendedor con ID 1:");
            System.out.println(vendedor.toString());
        } else {
            System.out.println("No se encontró el vendedor con ID 1.");
        }

        // Listar todos los vendedores
        ArrayList<Vendedor> listaVendedores = Comerciales.listadoDeVendedores();
        if (!listaVendedores.isEmpty()) {
            System.out.println("\nLista de Vendedores:");
            for (Vendedor v : listaVendedores) {
                System.out.println(v.toString());
            }
        } else {
            System.out.println("No hay vendedores en la lista.");
        }

        // Obtener el producto más vendido
        Producto productoMasVendido = Productos.obtenerProductoMasVendido();
        if (productoMasVendido != null) {
            System.out.println("\nProducto más vendido:");
            System.out.println(productoMasVendido.toString());
        } else {
            System.out.println("No se encontró el producto más vendido.");
        }

        // Generar un informe de productos en stock
        Productos.generarInforme();
    }
    
}
