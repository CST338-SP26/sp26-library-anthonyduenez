import Utilities.Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.time.LocalDate;

public class Library {
    public static final int LENDING_LIMIT = 5;

    private static int libraryCard;
    private String name;
    private List<Reader> readers = new ArrayList<>();
    private HashMap<String, Shelf> shelves = new HashMap<>();
    private HashMap<Book, Integer> books = new HashMap<>();

    public Library(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public Code init(String filename) {
        Scanner scan;

        try {
            scan = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            return Code.FILE_NOT_FOUND_ERROR;
        }

        int bookCount = convertInt(scan.nextLine(), Code.BOOK_COUNT_ERROR);
        if (bookCount < 0) return errorCode(bookCount);

        Code code = initBooks(bookCount, scan);
        if (code != Code.SUCCESS) return code;
        listBooks();

        int shelfCount = convertInt(scan.nextLine(), Code.SHELF_COUNT_ERROR);
        if (shelfCount < 0) return errorCode(shelfCount);

        code = initShelves(shelfCount, scan);
        if (code != Code.SUCCESS) return code;
        listShelves();

        int readerCount = convertInt(scan.nextLine(), Code.READER_COUNT_ERROR);
        if (readerCount < 0) return errorCode(readerCount);

        code = initReader(readerCount, scan);
        if (code != Code.SUCCESS) return code;
        listReaders();

        scan.close();
        return Code.SUCCESS;
    }

    public static int convertInt(String recordCountString, Code code) {
        try {
            return Integer.parseInt(recordCountString);
        } catch (NumberFormatException e) {
            System.out.println("Value which caused the error: " + recordCountString);
            System.out.println("Error message: " + code.getMessage());

            switch (code) {
                case BOOK_COUNT_ERROR:
                    System.out.println("Error: Could not read number of books");
                    break;
                case PAGE_COUNT_ERROR:
                    System.out.println("Error: could not parse page count");
                    break;
                case DATE_CONVERSION_ERROR:
                    System.out.println("Error: Could not parse date component");
                    break;
                default:
                    System.out.println("Error: Unknown conversion error");
            }

            return code.getCode(); // negative value
        }
    }

    public static LocalDate convertDate(String date, Code errorCode) {
        if (date.equals("0000")) {
            return LocalDate.of(1970, 1, 1);
        }

        String[] parts = date.split("-");

        if (parts.length != 3) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }

        try {
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            if (year < 0 || month < 0 || day < 0) {
                System.out.println("Error converting date: Year " + year);
                System.out.println("Error converting date: Month " + month);
                System.out.println("Error converting date: Day " + day);
                System.out.println("Using default date (01-jan-1970)");
                return LocalDate.of(1970, 1, 1);
            }

            return LocalDate.of(year, month, day);

        } catch (Exception e) {
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970, 1, 1);
        }
    }

    private Code initBooks(int bookCount, Scanner scan) {
        if (bookCount < 1) return Code.LIBRARY_ERROR;

        for (int i = 0; i < bookCount; i++) {
            String line = scan.nextLine();
            String[] parts = line.split(",");

            if (parts.length <= Book.DUE_DATE_) {
                return Code.BOOK_RECORD_COUNT_ERROR;
            }

            int pages = convertInt(parts[Book.PAGE_COUNT_], Code.PAGE_COUNT_ERROR);
            if (pages <= 0) return Code.PAGE_COUNT_ERROR;

            LocalDate date = convertDate(parts[Book.DUE_DATE_], Code.DATE_CONVERSION_ERROR);
            if (date == null) return Code.DATE_CONVERSION_ERROR;

            Book book = new Book(parts[Book.ISBN_], parts[Book.TITLE_], parts[Book.SUBJECT_], pages, parts[Book.AUTHOR_], date);

            addBook(book);
        }

        return Code.SUCCESS;
    }

    private Code initShelves(int shelfCount, Scanner scan) {
        if (shelfCount < 1) return Code.SHELF_COUNT_ERROR;

        for (int i = 0; i < shelfCount; i++) {
            String[] parts = scan.nextLine().split(",");

            int shelfNum = convertInt(parts[0], Code.SHELF_NUMBER_PARSE_ERROR);
            if (shelfNum < 0) return Code.SHELF_NUMBER_PARSE_ERROR;

            Shelf shelf = new Shelf(shelfNum, parts[1]);
            addShelf(shelf);
        }

        if (shelves.size() != shelfCount) {
            System.out.println("Number of shelves doesn't match expected");
            return Code.SHELF_NUMBER_PARSE_ERROR;
        }

        return Code.SUCCESS;
    }

    private Code initReader(int readerCount, Scanner scan) {
        if (readerCount <= 0) return Code.READER_COUNT_ERROR;

        for (int i = 0; i < readerCount; i++) {
            String[] parts = scan.nextLine().split(",");

            Reader reader = new Reader(
                    Integer.parseInt(parts[Reader.CARD_NUMBER_]),
                    parts[Reader.NAME_],
                    parts[Reader.PHONE_]
            );

            addReader(reader);

            int bookCount = Integer.parseInt(parts[Reader.BOOK_COUNT_]);
            int index = Reader.BOOK_START_;

            for (int j = 0; j < bookCount; j++) {
                Book book = getBookByISBN(parts[index++]);

                if (book == null) {
                    System.out.println("ERROR");
                    index++;
                    continue;
                }

                LocalDate dueDate = convertDate(parts[index++], Code.DATE_CONVERSION_ERROR);

                checkoutBook(reader, book);
            }
        }

        return Code.SUCCESS;
    }

    public Code addBook(Book book) {
        if (books.containsKey(book)) {
            books.put(book, books.get(book) + 1);
            System.out.println(books.get(book) + " copies of " + book.getTitle() + " in the stacks");
        } else {
            books.put(book, 1);
            System.out.println(book.getTitle() + " added to the stacks.");
        }

        Shelf shelf = shelves.get(book.getSubject());
        if (shelf != null) {
            return shelf.addBook(book);
        }

        System.out.println("No shelf for " + book.getSubject() + " books");
        return Code.SHELF_EXISTS_ERROR;
    }

    public int listBooks() {
        int total = 0;

        for (Book book : books.keySet()) {
            int count = books.get(book);
            total += count;

            System.out.println(count + " copies of " + book);
        }

        return total;
    }

    public int listShelves() {
        return listShelves(false);
    }

    public int listShelves(boolean showBooks) {
        for (Shelf shelf : shelves.values()) {
            System.out.println(shelf);
            if (showBooks) shelf.listBooks();
        }
        return shelves.size();
    }

    public int listReaders() {
        for (Reader r : readers) {
            System.out.println(r);
        }
        return readers.size();
    }

    public Book getBookByISBN(String isbn) {
        for (Book b : books.keySet()) {
            if (b.getISBN().equals(isbn)) {
                return b;
            }
        }
        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    public Code addShelf(Shelf shelf) {
        if (shelves.containsKey(shelf.getSubject())) {
            System.out.println("ERROR: Shelf already exists " + shelf);
            return Code.SHELF_EXISTS_ERROR;
        }

        shelves.put(shelf.getSubject(), shelf);
        return Code.SUCCESS;
    }

    public Code addReader(Reader reader) {
        if (readers.contains(reader)) {
            System.out.println(reader.getName() + " already has an account!");
            return Code.READER_ALREADY_EXISTS_ERROR;
        }

        readers.add(reader);
        System.out.println(reader.getName() + " added to the library!");

        if (reader.getCardNumber() > libraryCard) {
            libraryCard = reader.getCardNumber();
        }

        return Code.SUCCESS;
    }

    public Code checkoutBook(Reader reader, Book book) {
        if (!readers.contains(reader)) {
            System.out.println(reader.getName() + " doesn't have an account here");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }

        if (!books.containsKey(book)) {
            System.out.println("ERROR: could not find " + book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        Shelf shelf = shelves.get(book.getSubject());
        if (shelf == null) {
            System.out.println("no shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }

        return shelf.removeBook(book);
    }

    private Code errorCode(int codeNumber) {
        for (Code code : Code.values()) {
            if (code.getCode() == codeNumber) {
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }
}