import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ActividadM3Prueba {
    
    private static final String URL = "jdbc:mysql://localhost:3306/listtask2";
    private static final String USER = "root";
    private static final String PASSWORD = "Lucio12345";

    public static void main(String[] args) {
        Connection connection = null;
        Scanner scanner = new Scanner(System.in);

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");

            while (true) {
                System.out.println("1. Agregar tarea");
                System.out.println("2. Mostrar tareas");
                System.out.println("3. Marcar tarea como completada");
                System.out.println("4. Actualizar tarea");
                System.out.println("5. Eliminar tarea");
                System.out.println("6. Salir");
                System.out.print("Selecciona una opción: ");

                int option = getUserChoice(scanner);

                switch (option) {
                    case 1:
                        System.out.print("Ingresa la tarea: ");
                        String taskDescription = scanner.nextLine();
                        insertTask(connection, taskDescription);
                        System.out.println("Tarea agregada.");
                        System.out.println("");
                        break;
                    case 2:
                        System.out.println("Tareas pendientes:");
                        listTasks(connection);
                        System.out.println("");
                        break;
                    case 3:
                        System.out.print("Ingrese el número de la tarea completada: ");
                        int completedTask = getUserChoice(scanner);
                        markTaskAsCompleted(connection, completedTask);
                        System.out.println("");
                        break;
                    case 4:
                        System.out.print("Ingrese el número de la tarea que desea actualizar: ");
                        int taskIdToUpdate = getUserChoice(scanner);
                        System.out.print("Ingrese la nueva descripción: ");
                        String newDescription = scanner.nextLine();
                        updateTaskDescription(connection, taskIdToUpdate, newDescription);
                        System.out.println("");
                        break;
                    case 5:
                        System.out.print("Ingrese el número de la tarea que desea eliminar: ");
                        int taskIdToDelete = getUserChoice(scanner);
                        deleteTask(connection, taskIdToDelete);
                        System.out.println("");
                        break;
                    case 6:
                        System.out.println("¡Adiós!");
                        System.exit(0);
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                        System.out.println("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertTask(Connection connection, String taskDescription) {
        String insertQuery = "INSERT INTO task (description, completed) VALUES (?, 0)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, taskDescription);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void listTasks(Connection connection) {
        String selectQuery = "SELECT idtask, description FROM task WHERE completed = 0";
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(selectQuery)) {
            int count = 1;
            while (resultSet.next()) {
                int taskId = resultSet.getInt("idtask");
                String description = resultSet.getString("description");
                System.out.println(count + ". [ID: " + taskId + "] " + description);
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void markTaskAsCompleted(Connection connection, int taskID) {
        String updateQuery = "UPDATE task SET completed = 1 WHERE idtask = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, taskID);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Tarea marcada como completada.");
            } else {
                System.out.println("La tarea no se encontró o ya está completada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getUserChoice(Scanner scanner) {
        int choice = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consumir la nueva línea
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada no válida. Introduce un número.");
                scanner.next(); // Limpiar la entrada incorrecta
            }
        }

        return choice;
    }
    public static void updateTaskDescription(Connection connection, int taskID, String newDescription) {
        String updateQuery = "UPDATE task SET description = ? WHERE idtask = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newDescription);
            preparedStatement.setInt(2, taskID);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Descripción de la tarea actualizada.");
            } else {
                System.out.println("La tarea no se encontró.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(Connection connection, int taskID) {
        String deleteQuery = "DELETE FROM task WHERE idtask = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, taskID);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("Tarea eliminada.");
            } else {
                System.out.println("La tarea no se encontró.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}






