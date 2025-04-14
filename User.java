import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class User {
    protected String username;
    protected String password;
    protected String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public abstract void showMenu(Scanner sc);
}

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, "Admin");
    }

    @Override
    public void showMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Add User");
            System.out.println("2. Remove User");
            System.out.println("3. View All Users");
            System.out.println("0. Logout");
            System.out.print("Your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addUser(sc);
                case 2 -> removeUser(sc);
                case 3 -> displayUsers();
            }
        } while (choice != 0);
    }

    private void addUser(Scanner sc) {
    	System.out.print("Role (Admin/Librarian/Reader): ");
        String role = sc.nextLine();
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        try (FileWriter fw = new FileWriter("users.txt", true)) {
            fw.write(username + "," + password + "," + role + "\n");
            System.out.println("User added successfully.");
        } catch (IOException e) {
            System.out.println("Failed to add user.");
        }
    }

    private void removeUser(Scanner sc) {
        System.out.print("Enter username to remove: ");
        String usernameToRemove = sc.nextLine();

        File userFile = new File("users.txt");
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean found = false;

        try {
            Scanner fileReader = new Scanner(userFile);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(usernameToRemove)) {
                    found = true;
                    continue;
                }
                updatedLines.add(line);
            }
            fileReader.close();

            if (found) {
                FileWriter writer = new FileWriter(userFile, false);
                for (String line : updatedLines) {
                    writer.write(line + "\n");
                }
                writer.close();
                System.out.println("User removed successfully.");
            } else {
                System.out.println("User not found. Nothing was removed.");
            }

        } catch (IOException e) {
            System.out.println("Error occurred while processing file.");
        }
    }

    private void displayUsers() {
    	System.out.println();
        FileReader fr;
		try {
			fr = new FileReader("users.txt");
			Scanner reader = new Scanner(fr);
	        System.out.println("=== USER LIST ===");
	        while (reader.hasNextLine()) {
	            String line = reader.nextLine();
	            String[] parts = line.split(",");
	            if (parts.length == 3) {
	                String username = parts[0];
	                String password = parts[1];
	                String role = parts[2];
	                System.out.println(role + ": " + username + " - " + password);
	            }
	        }
	        reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
}

class Reader extends User {

    public Reader(String username, String password) {
        super(username, password, "Reader");
    }

    @Override
    public void showMenu(Scanner sc) {
        int choice;
        do {
            System.out.println("\n=== READER MENU ===");
            System.out.println("1. View books");
            System.out.println("2. Borrow book");
            System.out.println("3. Return book");
            System.out.println("4. View your borrowed books");
            System.out.println("5. View your transactions");
            System.out.println("0. Logout");
            System.out.print("Your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> displayBooks();
                case 2 -> borrowBook(sc);
                case 3 -> returnBook(sc);
                case 4 -> viewMyBooks();
                case 5 -> viewTransaction();

            }
        } while (choice != 0);
    }

    private void displayBooks() {
        try {
	        FileReader fr = new FileReader("books.txt");
	        Scanner reader = new Scanner(fr);

	        ArrayList<String> printedBooks = new ArrayList<>();
	        ArrayList<String> ebooks = new ArrayList<>();

	        while (reader.hasNextLine()) {
	            String line = reader.nextLine();
	            String[] parts = line.split(",",-1);
	            if (parts.length >= 7) {
	                if (parts.length >= 8 && parts[0].equals("PrintedBook")) {
	                    String title = parts[1];
	                    String author = parts[2];
	                    String genre = parts[3];
	                    String isbn = parts[4];
	                    String available = parts[5].equals("true") ? "Available" : "Borrowed";
	                    String dueDate = parts[6].trim().isEmpty() ? "N/A" : parts[6].trim();
	                    String pages = parts[7];
	                    
	                    printedBooks.add(title + " | By " + author + " | Genre: " + genre + " | ISBN: " + isbn + " | Status: " + available + " | Due: " + dueDate + " | Pages: " + pages);
	                } else if (parts[0].equals("Ebook")) {
	                    String title = parts[1];
	                    String author = parts[2];
	                    String genre = parts[3];
	                    String isbn = parts[4];
	                    String available = parts[5].equals("true") ? "Available" : "N/A";
	                    String format = parts[6];

	                    ebooks.add(title + " | By " + author + " | Genre: " + genre + " | ISBN: " + isbn + " | Status: " + available + " | Format: " + format);
	                }
	            }
	        }

	        System.out.println("\n=== PRINTED BOOKS ===");
	        for (String book : printedBooks) {
	            System.out.println(book);
	        }

	        System.out.println("\n=== EBOOKS ===");
	        for (String ebook : ebooks) {
	            System.out.println(ebook);
	        }

	        reader.close();
	        fr.close();

	    } catch (IOException e) {
	        System.out.println("Could not read book's file.");
	    }
    }

    private void borrowBook(Scanner sc) {
        try {
            File file = new File("books.txt");
            ArrayList<String> lines = new ArrayList<>();
            Scanner reader = new Scanner(file);
            boolean found = false;

            System.out.print("Enter ISBN of the book to borrow: ");
            String isbn = sc.nextLine().trim();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 7 && parts[0].trim().equals("PrintedBook") && parts[4].trim().equals(isbn)) {
                    if (parts[5].trim().equals("true")) {
                        parts[5] = "false";

                        String dueDate = java.time.LocalDate.now().plusDays(14).toString();

                        if (parts.length == 7) {
                        	String[] newPart = {parts[0], parts[1], parts[2], parts[3], parts[4], parts[5],dueDate,parts[6]};
                            parts[6] = dueDate;
                            line = String.join(",", newPart);
                        }else if (parts.length >= 8) {
                        	parts[6] = dueDate;
                        	line = String.join(",", parts);
                        }

                        found = true;
                        System.out.println("Book borrowed successfully.");

                        FileWriter transWriter = new FileWriter("transactions.txt", true);
                        String transaction = this.username + " | BORROW | ISBN: " + isbn + " | Date: " + java.time.LocalDate.now();
                        transWriter.write(transaction + "\n");
                        transWriter.close();
                    } else {
                        System.out.println("Book is not available.");
                    }
                }

                lines.add(line);
            }

            reader.close();

            if (found) {
                FileWriter fw = new FileWriter(file);
                for (String l : lines) fw.write(l + "\n");
                fw.close();
            } else {
                System.out.println("Book with this ISBN not found.");
            }

        } catch (IOException e) {
            System.out.println("Error borrowing book.");
        }
    }

    private void returnBook(Scanner sc) {
        try {
            File file = new File("books.txt");
            ArrayList<String> lines = new ArrayList<>();
            Scanner reader = new Scanner(file);

            boolean bookFound = false;
            boolean isBorrowed = false;

            System.out.print("Enter ISBN of the book to return: ");
            String isbn = sc.nextLine().trim();

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] parts = line.split(",");

                if (parts.length >= 6 && parts[0].trim().equals("PrintedBook") && parts[4].trim().equals(isbn)) {
                    bookFound = true;

                    if (parts[5].trim().equals("false")) {
                        parts[5] = "true";        
                        if(parts.length >= 8) {
                        	parts[6] = "";
                        }
                        isBorrowed = true;
                        System.out.println("Book returned successfully.");

                        FileWriter transWriter = new FileWriter("transactions.txt", true);
                        String transaction = this.username + " | RETURN | ISBN: " + isbn + " | Date: " + LocalDate.now();
                        transWriter.write(transaction + "\n");
                        transWriter.close();
                    }
                    line = String.join(",", parts);
                }

                lines.add(line);
            }

            reader.close();

            if (bookFound) {
                FileWriter fw = new FileWriter(file);
                for (String l : lines) fw.write(l + "\n");
                fw.close();

                if (!isBorrowed) {
                    System.out.println("This book is not currently borrowed.");
                }
            } else {
                System.out.println("Book with this ISBN not found.");
            }

        } catch (IOException e) {
            System.out.println("Error returning book.");
        }
    }

    private void viewMyBooks() {
        try {
            File transFile = new File("transactions.txt");
            File bookFile = new File("books.txt");

            HashMap<String, Integer> loanStatus = new HashMap<>();

            Scanner transReader = new Scanner(transFile);
            while (transReader.hasNextLine()) {
                String line = transReader.nextLine();
                if (!line.startsWith(this.username)) continue;

                String isbn = "";
                if (line.contains("ISBN:")) {
                    String raw = line.substring(line.indexOf("ISBN:") + 5);
                    isbn = raw.split("\\|")[0].trim();
                }


                if (line.contains("BORROW")) {
                    loanStatus.put(isbn, loanStatus.getOrDefault(isbn, 0) + 1);
                } else if (line.contains("RETURN")) {
                    loanStatus.put(isbn, loanStatus.getOrDefault(isbn, 0) - 1);
                }
            }
            transReader.close();

            ArrayList<String> currentlyBorrowed = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : loanStatus.entrySet()) {
                if (entry.getValue() > 0) {
                    currentlyBorrowed.add(entry.getKey());
                }
            }

            if (currentlyBorrowed.isEmpty()) {
                System.out.println("You haven't borrowed any books.");
                return;
            }

            Scanner bookReader = new Scanner(bookFile);
            System.out.println("\n=== Your Borrowed Books ===");
            while (bookReader.hasNextLine()) {
                String line = bookReader.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String isbn = parts[4].trim();
                    if (currentlyBorrowed.contains(isbn)) {
                    	String pages = (parts.length >= 8) ? parts[7].trim() : "Unknown";
                        System.out.println("Title: " + parts[1] + ", Author: " + parts[2] + ", ISBN: " + isbn + ", Pages: " + pages);
                    }
                }
            }
            bookReader.close();

        } catch (IOException e) {
            System.out.println("Error reading borrowed books.");
        }
    }

    private void viewTransaction() {
        System.out.println("\n=== Transaction history for " + this.username + " ===");

        File transFile = new File("transactions.txt");
        boolean hasTransaction = false;

        try (Scanner scanner = new Scanner(transFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(this.username)) {
                    hasTransaction = true;
                    System.out.println(line);
                }
            }

            if (!hasTransaction) {
                System.out.println("No transactions found for user: " + this.username);
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions.");
        }
    }
}
