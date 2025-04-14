import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class LibrarySystem {
    public static void main(String[] args) {
    	System.out.println("=== Welcome to library ===");
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
        String password = sc.nextLine();

        try (Scanner fileScanner = new Scanner(new File("users.txt"))) {
            boolean found = false;
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length >= 3 && parts[0].equals(username) && parts[1].equals(password)) {
                    found = true;
                    User user;
                    switch (parts[2]) {
                        case "Admin" -> user = new Admin(username, password);
                        case "Librarian" -> user = new Librarian(username, password);
                        case "Reader" -> user = new Reader(username, password);
                        default -> {
                            System.out.println("Unknown role.");
                            return;
                        }
                    }
                    user.showMenu(sc);
                    break;
                }
            }

            if (!found) System.out.println("Invalid credentials.");

        } catch (IOException e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }
}
