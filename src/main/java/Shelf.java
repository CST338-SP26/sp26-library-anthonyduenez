import Utilities.Code;

import java.util.HashMap;
import java.util.Objects;

public class Shelf {
    public final static int SHELF_NUMBER_ = 0;
    public final static int SUBJECT_ = 2;

    private HashMap<Book, Integer> books = new HashMap<>();
    private int shelfNumber;
    private String subject;

    public Shelf(int shelfNumber, String subject) {
    }

    public HashMap<Book, Integer> getBooks() {
        return null;
    }

    public void setBooks(HashMap<Book, Integer> books) {
    }

    public int getShelfNumber() {
        return 0;
    }

    public void setShelfNumber(int shelfNumber) {
    }

    public String getSubject() {
        return null;
    }

    public void setSubject(String subject) {
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }

    public int getBookCount(Book book) {
        return 0;
    }

    public Code addBook(Book book) {
        return null;
    }

    public Code removeBook(Book book) {
        return null;
    }

    public String listBooks() {
        return "";
    }
}
