public abstract class Book {
    protected String title;
    protected String author;
    protected String genre;
    protected String isbn;
    protected boolean isAvailable;
    protected String dueDate;

    public Book(String title, String author, String genre, String isbn, boolean isAvailable, String dueDate) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.dueDate = dueDate;
    }

    public abstract String toFileString();
    public abstract String getType();
    
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
}

class PrintedBook extends Book {
	String pages;
    public PrintedBook(String title, String author, String genre, String isbn, boolean isAvailable, String dueDate,String pages) {
        super(title, author, genre, isbn, isAvailable, dueDate);
        this.pages = pages;
    }

    @Override
    public String toFileString() {
        return "Printed," + title + "," + author + "," + genre + "," + isbn + "," + isAvailable + "," + dueDate + "," + pages;
    }

    @Override
    public String getType() {
        return "Printed";
    }
}

class Ebook extends Book {
    private String fileFormat;

    public Ebook(String title, String author, String genre, String isbn, String fileFormat) {
        super(title, author, genre, isbn, true, "N/A"); // Always available
        this.fileFormat = fileFormat;
    }

    @Override
    public String toFileString() {
        return "Ebook," + title + "," + author + "," + genre + "," + isbn + "," + isAvailable + "," + dueDate + "," + fileFormat;
    }

    @Override
    public String getType() {
        return "Ebook";
    }
}
