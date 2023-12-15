import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/oop_jdbc";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();

            System.out.print("Select operation: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> createStudent();
                case 2 -> readStudents();
                case 3 -> updateStudent();
                case 4 -> deleteStudent();
                case 5 -> {
                    System.out.println("Exiting program.");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static Connection establishConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            throw new RuntimeException("Database connection error", e);
        }
    }

    private static void createStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter firstname: ");
        String firstname = scanner.nextLine();

        System.out.print("Enter lastname: ");
        String lastname = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        String insertQuery = "INSERT INTO students (firstname, lastname, address) VALUES (?, ?, ?)";
        try (Connection connection = establishConnection(); PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, address);
            statement.executeUpdate();
            System.out.println("Record created successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void readStudents() {
        String selectQuery = "SELECT * FROM students";
        try (Connection connection = establishConnection(); PreparedStatement statement = connection.prepareStatement(selectQuery); ResultSet resultSet = statement.executeQuery()) {
            displayTableHeader();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                String address = resultSet.getString("address");
                displayStudentRecord(id, firstname, lastname, address);
            }
            displayTableFooter();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ID to update student: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter new firstname: ");
        String firstname = scanner.nextLine();

        System.out.print("Enter new lastname: ");
        String lastname = scanner.nextLine();

        System.out.print("Enter new address: ");
        String address = scanner.nextLine();

        String updateQuery = "UPDATE students SET firstname=?, lastname=?, address=? WHERE id=?";
        try (Connection connection = establishConnection(); PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, address);
            statement.setInt(4, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Record updated successfully.");
            } else {
                System.out.println("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter record ID to delete: ");
        int id = scanner.nextInt();

        String deleteQuery = "DELETE FROM students WHERE id=?";
        try (Connection connection = establishConnection(); PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Record deleted successfully.");
            } else {
                System.out.println("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
