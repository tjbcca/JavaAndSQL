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
                System.out.println("3. Add a book");
                System.out.println("4. Remove a book");
                System.out.println("5. Exit");
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
                        System.out.print("Enter book name to add: ");
                        String newBookName = scanner.nextLine();
                        System.out.print("Enter author name: ");
                        String newAuthor = scanner.nextLine();
                        addBook(stmt, newBookName, newAuthor);
                        break;
                    case 4:
                        System.out.print("Enter book name to remove: ");
                        String removeBookName = scanner.nextLine();
                        removeBook(stmt, removeBookName);
                        break;
                    case 5:
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
            System.out.println(rs.getInt("BookID") +
                    ". " + rs.getString("BookName") +
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
            String memberName = rs.getString("MemberName");
            if (memberName != null) {
                System.out.println(rs.getString("BookName") + ", Checked out by: " + memberName);
            } else {
                System.out.println(rs.getString("BookName") + ", Available");
            }
        }
    }

    private static void addBook(Statement stmt, String bookName, String author) throws Exception {
        // Check if the book already exists
        String checkQuery = "SELECT COUNT(*) AS count FROM Books WHERE BookName = '" + bookName + "'";
        ResultSet rs = stmt.executeQuery(checkQuery);
        rs.next();
        int count = rs.getInt("count");

        if (count > 0) {
            System.out.println("Book already exists in the library.");
        } else {
            // Get the current maximum BookID
            String maxIdQuery = "SELECT MAX(BookID) AS maxId FROM Books";
            ResultSet rsMaxId = stmt.executeQuery(maxIdQuery);
            rsMaxId.next();
            int maxId = rsMaxId.getInt("maxId");

            // Assign the new BookID
            int newBookId = maxId + 1;

            // Add the new book with the manually assigned BookID
            String insertQuery = "INSERT INTO Books (BookID, BookName, Author) VALUES (" + newBookId + ", '" + bookName + "', '" + author + "')";
            int rowsAffected = stmt.executeUpdate(insertQuery);
            if (rowsAffected > 0) {
                System.out.println("Book added successfully with BookID: " + newBookId);
            } else {
                System.out.println("Failed to add book.");
            }
        }
    }

    private static void removeBook(Statement stmt, String bookName) throws Exception {
        String query = "DELETE FROM Books WHERE BookName = '" + bookName + "'";
        int rowsAffected = stmt.executeUpdate(query);
        if (rowsAffected > 0) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Failed to remove book.");
        }
    }
}