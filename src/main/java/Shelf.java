import Utilities.Code;

import java.util.HashMap;
import java.util.Objects;

public class Shelf {
    public final static int SHELF_NUMBER_ = 0;
    public final static int SUBJECT_ = 2;

    private HashMap<Book, Integer> books = new HashMap<>();
    private int shelfNumber;
    private String subject;

    public Shelf(){

    }

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Shelf shelf = (Shelf) o;
        return getShelfNumber() == shelf.getShelfNumber() && Objects.equals(getSubject(), shelf.getSubject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getShelfNumber(), getSubject());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(shelfNumber).append(" : ").append(subject);
        return sb.toString();
    }

    
    // Returns the number of copies for a specific book, or -1 if absent.
    public int getBookCount(Book book) {
        if(books.containsKey(book)) {
            return books.get(book);
        }
        return -1;
    }

    // Adds a book copy if subject matches and returns the operation result code.
    public Code addBook(Book book) {
        if (books.containsKey(book)) {
            books.put(book, books.get(book) + 1);
            System.out.println(book.toString() + " added to shelf " + toString());
            return Code.SUCCESS;
        } else {
            if (getSubject().equals(book.getSubject())) {
                books.put(book, 1);
                System.out.println(book.toString() + " added to shelf " + toString());
                return Code.SUCCESS;
            }
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    // Removes one copy of a book and returns the operation result code.
    public Code removeBook(Book book) {
        if(books.containsKey(book)) {
            if(books.get(book) == 0) {
                System.out.println("No copies of " + book.toString() + " remain on the shelf " + getSubject());
                return Code.BOOK_NOT_IN_INVENTORY_ERROR;
            }
            else if (books.get(book) >= 1) {
                books.put(book, books.get(book) - 1);
                System.out.println(book.toString() + " successfully removed from shelf " + getSubject());
                return Code.SUCCESS;
            }
        }
        System.out.println(book.toString() + " is not on shelf " + getSubject());
        return Code.BOOK_NOT_IN_INVENTORY_ERROR;
    }

    // Returns a formatted list of books currently on this shelf.
    public String listBooks() {
        StringBuilder sb = new StringBuilder();
        sb.append(books.size()).append(" books on shelf: ").append(toString());
        for(Book book : books.keySet()) {
            sb.append("\n").append(book.toString());
        }
        return sb.toString();
    }
}
