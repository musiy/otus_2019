package sample;

public class Book {

    // наименование
    private String name;

    // список авторов
    private Author[] authors;

    // ISBN
    private String isbn;

    // Всего страниц
    private Long pages;

    public Book(String name, Author[] authors, String isbn, Long pages) {
        this.name = name;
        this.authors = authors;
        this.isbn = isbn;
        this.pages = pages;
    }

    public String getName() {
        return name;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public Long getPages() {
        return pages;
    }
}
