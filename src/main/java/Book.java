import java.time.LocalDate;

public class Book {
    public static final int AUTHOR_ = 4;
    public static final int DUE_DATE_ = 5;
    public static final int ISBN_ = 0;
    public static final int PAGE_COUNT_ = 3;
    public static final int SUBJECT_ = 2;
    public static final int TITLE_ = 1;

    private String author;
    private LocalDate DueDate;
    private String ISBN;
    private int pageCount;
    private String Subject;
    private String Title;

    public Book(String ISBN, String Title, String Subject, int PageCount, String Author, LocalDate DueDate){
        this.ISBN = ISBN;
        this.Title = Title;
        this.Subject = Subject;
        this.pageCount = PageCount;
        this.author = Author;
        this.DueDate = DueDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getDueDate() {
        return DueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.DueDate = dueDate;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String isbn) {
        this.ISBN = isbn;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String Subject) {
        this.Subject = Subject;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return Title + " by " + author + " ISBN: " + ISBN;
    }
}