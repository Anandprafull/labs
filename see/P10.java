import java.sql.*;
import java.util.Scanner;

public class JDBCCRUDDemo {
    // Database credentials (MUST BE MODIFIED by user)
    static final String URL = "jdbc:mysql://localhost:3306/demo";
    static final String USER = "root";
    static final String PASS = "password";

    // Helper scanner for inputs outside the try-with-resources blocks
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n-------- JDBC CRUD MENU --------");
            System.out.println("1. INSERT (Create)");
            System.out.println("2. SELECT (Read)");
            System.out.println("3. UPDATE");
            System.out.println("4. DELETE");
            System.out.println("5. EXIT");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1: insertRecord(); break;
                case 2: readRecords(); break;
                case 3: updateRecord(); break;
                case 4: deleteRecord(); break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // CREATE operation
    public static void insertRecord() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "INSERT INTO students (id, name, age) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            System.out.print("Enter ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Age: ");
            int age = sc.nextInt();

            pst.setInt(1, id);
            pst.setString(2, name);
            pst.setInt(3, age);
            int rows = pst.executeUpdate();
            System.out.println(rows + " record inserted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ operation
    public static void readRecords() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT * FROM students";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n--- STUDENT RECORDS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Age: " + rs.getInt("age"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE operation
    public static void updateRecord() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "UPDATE students SET name=?, age=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            System.out.print("Enter ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter New Name: ");
            String name = sc.nextLine();
            System.out.print("Enter New Age: ");
            int age = sc.nextInt();

            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setInt(3, id);
            int rows = pst.executeUpdate();
            System.out.println(rows + " record updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE operation
    public static void deleteRecord() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            System.out.print("Enter ID to delete: ");
            int id = sc.nextInt();
            pst.setInt(1, id);

            int rows = pst.executeUpdate();
            System.out.println(rows + " record deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
