import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class BookManager{
	
	public void addBook(Scanner sc) {
	    System.out.print("What type of book (1 - PrintedBook, 2 - Ebook): ");
	    String type = sc.nextLine();

	    System.out.print("Title: ");
	    String title = sc.nextLine();

	    System.out.print("Author: ");
	    String author = sc.nextLine();

	    System.out.print("Genre: ");
	    String genre = sc.nextLine();

	    System.out.print("ISBN: ");
	    String isbn = sc.nextLine();

	    try {
	        FileWriter fw = new FileWriter("books.txt", true);

	        if (type.equals("1")) {
	        	System.out.print("Number of pages: ");
	            String pages = sc.nextLine();
	            String availability = "true";
	            String dueDate = "";
	            fw.write("PrintedBook," + title + "," + author + "," + genre + "," + isbn + "," + availability + "," + dueDate + "," +pages+ "\n");
	            System.out.println("Printed Book added successfully.");

	        } else if (type.equals("2")) {
	            System.out.print("File Format (e.g. PDF, EPUB): ");
	            String format = sc.nextLine();

	            fw.write("Ebook," + title + "," + author + "," + genre + "," + isbn + ",true," + format + "\n");
	            System.out.println("Ebook added successfully.");

	        } else {
	            System.out.println("Invalid book type selected.");
	        }

	        fw.close();
	    } catch (IOException e) {
	        System.out.println("Error writing to books file.");
	    }
	}


	public void displayBooks() {
	    try {
	        FileReader fr = new FileReader("books.txt");
	        Scanner reader = new Scanner(fr);

	        ArrayList<String> printedBooks = new ArrayList<>();
	        ArrayList<String> ebooks = new ArrayList<>();

	        while (reader.hasNextLine()) {
	            String line = reader.nextLine();
	            String[] parts = line.split(",",-1);
	            if (parts.length >= 7) {
	                if (parts[0].equals("PrintedBook")) {
	                    String title = parts[1];
	                    String author = parts[2];
	                    String genre = parts[3];
	                    String isbn = parts[4];
	                    String available = parts[5].equals("true") ? "Available" : "Borrowed";
	                    String dueDate = parts[6].trim().isEmpty() ? "N/A" : parts[6].trim();
	                    String pages = parts.length > 7 ? parts[7].trim() : "Unknown";
	                    
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
	        System.out.println("Could not read book file.");
	    }
	}

	public void removeBook() {
	    Scanner sc = new Scanner(System.in);
	    System.out.print("Enter ISBN of the book to remove: ");
	    String isbnToRemove = sc.nextLine().trim();

	    File file = new File("books.txt");
	    ArrayList<String> updatedLines = new ArrayList<>();
	    boolean found = false;

	    try {
	        Scanner fileReader = new Scanner(file);
	        while (fileReader.hasNextLine()) {
	            String line = fileReader.nextLine();
	            String[] parts = line.split(",", -1);

	            if (parts.length >= 5 && parts[4].trim().equals(isbnToRemove)) {
	                found = true;
	                continue;
	            }

	            updatedLines.add(line);
	        }
	        fileReader.close();
	
	        if (found) {
	            FileWriter writer = new FileWriter(file);
	            for (String updatedLine : updatedLines) {
	                writer.write(updatedLine + "\n");
	            }
	            writer.close();
	            System.out.println("Book with ISBN " + isbnToRemove + " has been removed.");
	        } else {
	            System.out.println("Book with ISBN " + isbnToRemove + " not found.");
	        }
	    } catch (IOException e) {
	        System.out.println("Error while removing the book: " + e.getMessage());
	    }
	}
}

public class Librarian extends User {

    private BookManager bookManager;

    public Librarian(String username, String password) {
        super(username, password, "Librarian");
        this.bookManager = new BookManager();
    }

    @Override
    public void showMenu(Scanner sc) {
        while (true) {
            System.out.println("\n=== LIBRARIAN MENU ===");
            System.out.println("1. Add Book");
            System.out.println("2. Remove Book");
            System.out.println("3. List Books");
            System.out.println("0. Logout");
            System.out.print("Your choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": bookManager.addBook(sc); break;
                case "2": bookManager.removeBook(); break;
                case "3": bookManager.displayBooks(); break;
                case "0": return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}