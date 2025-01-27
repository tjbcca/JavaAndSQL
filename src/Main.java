import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:postgresql:postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "6732";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Library System Menu:");
                System.out.println("1. View all books");
                System.out.println("2. Search for a book");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        viewAllBooks(stmt);
                        break;
                    case 2:
                        System.out.print("Enter book name to search: ");
                        String bookName = scanner.nextLine();
                        searchBook(stmt, bookName);
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewAllBooks(Statement stmt) throws Exception {
        ResultSet rs = stmt.executeQuery("SELECT * FROM Books");
        System.out.println("Books Table:");
        while (rs.next()) {
            System.out.println(rs.getString("BookID") + ". " +
                    rs.getString("BookName") +
                    " by " + rs.getString("Author"));
        }
    }

    private static void searchBook(Statement stmt, String bookName) throws Exception {
        String query = "SELECT Books.BookName, Members.MemberName " +
                "FROM Books LEFT JOIN Members ON Books.BookID = Members.BookID " +
                "WHERE Books.BookName LIKE '%" + bookName + "%'";
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Search Results:");
        while (rs.next()) {
            String searchBook = rs.getString("BookName");
            String memberName = rs.getString("MemberName");
            if (memberName != null) {
                System.out.println(searchBook + ", Checked out by: " + memberName);
            } else {
                System.out.println(searchBook + ", Available");
            }
        }
    }
}