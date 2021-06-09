package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class Book implements DbEntity {
    private final Long id;
    private final String name;
    private final BookAuthor author;
    private final BookGenre genre;
    private final LocalDate date;
    private final int pagesAmount;
    private final int booksAmount;
    private final String description;

    public Book(Long id, String name, BookAuthor author, BookGenre genre, LocalDate date, int pagesAmount, int booksAmount,String description) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.date = date;
        this.pagesAmount = pagesAmount;
        this.booksAmount = booksAmount;
        this.description = description;
    }

    public Book(String name, BookAuthor author, BookGenre genre, LocalDate date, int pagesAmount, String description) {
        this(null, name, author, genre, date, pagesAmount, 1, description);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BookAuthor getAuthor() {
        return author;
    }

    public BookGenre getGenre() {
        return genre;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getPagesAmount() {
        return pagesAmount;
    }

    public String getDescription() {
        return description;
    }

    public int getBooksAmount() {
        return booksAmount;
    }

    public Book updateName(String newName) {
        if (newName == null) {
            return this;
        }
        return new Book(id, newName, author, genre, date, pagesAmount, booksAmount, description);
    }

    public Book updateAuthor(BookAuthor newAuthor) {
        if (newAuthor == null) {
            return this;
        }
        return new Book(id, name, newAuthor, genre, date, pagesAmount, booksAmount, description);
    }

    public Book updateGenre(BookGenre newGenre) {
        if (newGenre == null) {
            return this;
        }
        return new Book(id, name, author, newGenre, date, pagesAmount, booksAmount, description);
    }

    public Book updateDate(LocalDate newDate) {
        if (newDate == null) {
            return this;
        }
        return new Book(id, name, author, genre, newDate, pagesAmount, booksAmount, description);
    }

    public Book updatePagesAmount(int newPagesAmount) {
        return new Book(id, name, author, genre, date, newPagesAmount, booksAmount, description);
    }

    public Book updateDescription(String newDescription) {
        if (newDescription == null) {
            return this;
        }
        return new Book(id, name, author, genre, date, pagesAmount, booksAmount, newDescription);
    }

    public Book updatedBooksAmount(int newBooksAmount) {
        return new Book(id, name, author, genre, date, pagesAmount, newBooksAmount, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return pagesAmount == book.pagesAmount && Objects.equals(id, book.id) && Objects.equals(name, book.name) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre) && Objects.equals(date, book.date) && Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, genre, date, pagesAmount, description);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", date=" + date +
                ", pagesAmount=" + pagesAmount +
                ", description='" + description + '\'' +
                '}';
    }
}
