import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String JDBC_USER = "root"; // Replace with your DB user
    private static final String JDBC_PASSWORD = "root"; // Replace with your DB password

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n********** Welcome to Library Management System! **********");
            System.out.println("1. Add a New Book");
            System.out.println("2. Update Book Details");
            System.out.println("3. Delete a Book");
            System.out.println("4. Search for a Book");
            System.out.println("5. Add a New Member");
            System.out.println("6. Loan a Book");
            System.out.println("7. Return a Book");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    addNewBook(scanner);
                    break;
                case 2:
                    updateBookDetails(scanner);
                    break;
                case 3:
                    deleteBook(scanner);
                    break;
                case 4:
                    searchForBook(scanner);
                    break;
                case 5:
                    addNewMember(scanner);
                    break;
                case 6:
                    loanBook(scanner);
                    break;
                case 7:
                    returnBook(scanner);
                    break;
                case 8:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);

        scanner.close();
    }

    private static void addNewBook(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter year published: ");
        int year_published = scanner.nextInt();

        String sql = "INSERT INTO books (title, author, publisher, year_published) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, year_published);
            pstmt.executeUpdate();
            System.out.println("Book added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateBookDetails(Scanner scanner) {
        System.out.print("Enter book ID: ");
        int book_id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        System.out.print("Enter new author: ");
        String author = scanner.nextLine();
        System.out.print("Enter new publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter new year published: ");
        int year_published = scanner.nextInt();

        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, year_published = ? WHERE book_id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, year_published);
            pstmt.setInt(5, book_id);
            pstmt.executeUpdate();
            System.out.println("Book details updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteBook(Scanner scanner) {
        System.out.print("Enter book ID: ");
        int book_id = scanner.nextInt();

        String sql = "DELETE FROM books WHERE book_id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book_id);
            pstmt.executeUpdate();
            System.out.println("Book deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void searchForBook(Scanner scanner) {
        System.out.println("Search by: 1. Title 2. Author 3. Year Published");
        int searchOption = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String sql = "";
        String searchValue = "";

        switch (searchOption) {
            case 1:
                System.out.print("Enter title: ");
                searchValue = scanner.nextLine();
                sql = "SELECT * FROM books WHERE title LIKE ?";
                break;
            case 2:
                System.out.print("Enter author: ");
                searchValue = scanner.nextLine();
                sql = "SELECT * FROM books WHERE author LIKE ?";
                break;
            case 3:
                System.out.print("Enter year published: ");
                searchValue = scanner.nextLine();
                sql = "SELECT * FROM books WHERE year_published = ?";
                break;
            default:
                System.out.println("Invalid search option.");
                return;
        }

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (searchOption == 3) {
                pstmt.setInt(1, Integer.parseInt(searchValue));
            } else {
                pstmt.setString(1, "%" + searchValue + "%");
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.printf("ID: %d, Title: %s, Author: %s, Publisher: %s, Year Published: %d%n",
                        rs.getInt("book_id"), rs.getString("title"), rs.getString("author"),
                        rs.getString("publisher"), rs.getInt("year_published"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addNewMember(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
            System.out.println("Member added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loanBook(Scanner scanner) {
        System.out.print("Enter book ID: ");
        int book_id = scanner.nextInt();
        System.out.print("Enter member ID: ");
        int member_id = scanner.nextInt();
        System.out.print("Enter loan date (YYYY-MM-DD): ");
        String loan_date = scanner.next();
        System.out.print("Enter due date (YYYY-MM-DD): ");
        String due_date = scanner.next();

        String sql = "INSERT INTO loans (book_id, member_id, loan_date, return_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, book_id);
            pstmt.setInt(2, member_id);
            pstmt.setDate(3, Date.valueOf(loan_date));
            pstmt.setDate(4, Date.valueOf(due_date));
            pstmt.executeUpdate();
            System.out.println("Book loan recorded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void returnBook(Scanner scanner) {
        System.out.print("Enter loan ID: ");
        int loan_id = scanner.nextInt();
        System.out.print("Enter return date (YYYY-MM-DD): ");
        String returnDate = scanner.next();

        String sql = "UPDATE loans SET return_date = ? WHERE loan_id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(returnDate));
            pstmt.setInt(2, loan_id);
            pstmt.executeUpdate();
            System.out.println("Book return recorded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
