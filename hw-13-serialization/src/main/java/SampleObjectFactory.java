import sample.Author;
import sample.Book;
import sample.BookStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SampleObjectFactory {

    public BookStore createBookStore() {
        byte[] ba = new byte[10];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte) i;
        }
        BookStore bookStore = new BookStore(ba, (byte) 255, 'й',
                (short) 10, 1000, 100_000_000L, 10.f, 3.13159);
        bookStore.setBookList(createBookList());
        return bookStore;
    }

    public List<Book> createBookList() {
        List<Book> books = new ArrayList<>();
        books.add(createBookSample1());
        books.add(createBookSample2());
        books.add(createBookSample3());
        return books;
    }

    public Book createBookSample1() {
        Author[] authors = new Author[]{
                createAuthorMartin(),
                createAuthorWeinberg()
        };
        return new Book("Book1", authors, "123-BS-456-GI", 230L);
    }

    public Book createBookSample2() {
        Author[] authors = new Author[]{
                createAuthorHorstman(),
                createAuthorRassel()
        };
        return new Book("Book2", authors, "VEGA-643-452-SF3", 110L);
    }

    public Book createBookSample3() {
        Author[] authors = new Author[]{
                createAuthorWeinberg(),
                createAuthorRassel(),
                createAuthorMartin(),
                createAuthorPushkin()
        };
        return new Book("Book3", authors, "ATOMIC", 300L);
    }

    public Author createAuthorMartin() {
        return new Author("Robert", "Martin",
                LocalDate.of(1952, 1, 2), 25);
    }

    public Author createAuthorWeinberg() {
        return new Author("Steven", "Weinberg",
                LocalDate.of(1960, 10, 15), 35);
    }

    public Author createAuthorHorstman() {
        return new Author("Key", "Horstman",
                LocalDate.of(1967, 3, 27), 15);
    }

    public Author createAuthorRassel() {
        return new Author("Bertrand", "Russelll",
                LocalDate.of(1845, 12, 11), 45);
    }

    public Author createAuthorPushkin() {
        return new Author("Alexandr", "Pushkin",
                LocalDate.of(1799, 3, 7), 36);
    }
}
