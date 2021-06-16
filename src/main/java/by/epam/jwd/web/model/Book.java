package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class Book implements DbEntity {
    private final Long id;
    private final String name;
    private final Author author;
    private final Genre genre;
    private final LocalDate date;
    private final int pagesAmount;
    private final int copiesAmount;
    private final String description;

    public Book(Long id, String name, Author author, Genre genre, LocalDate date, int pagesAmount, int copiesAmount, String description) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.date = date;
        this.pagesAmount = pagesAmount;
        this.copiesAmount = copiesAmount;
        this.description = description;
    }

    public Book(String name, Author author, Genre genre, LocalDate date, int pagesAmount, String description) {
        this(null, name, author, genre, date, pagesAmount, 1, description);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
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

    public int getCopiesAmount() {
        return copiesAmount;
    }


    public Book updateName(String newName) {
        if (newName == null) {
            return this;
        }
        return new Book(id, newName, author, genre, date, pagesAmount, copiesAmount, description);
    }

    public Book updateAuthor(Author newAuthor) {
        if (newAuthor == null) {
            return this;
        }
        return new Book(id, name, newAuthor, genre, date, pagesAmount, copiesAmount, description);
    }

    public Book updateGenre(Genre newGenre) {
        if (newGenre == null) {
            return this;
        }
        return new Book(id, name, author, newGenre, date, pagesAmount, copiesAmount, description);
    }

    public Book updateDate(LocalDate newDate) {
        if (newDate == null) {
            return this;
        }
        return new Book(id, name, author, genre, newDate, pagesAmount, copiesAmount, description);
    }

    public Book updatePagesAmount(int newPagesAmount) {
        return new Book(id, name, author, genre, date, newPagesAmount, copiesAmount, description);
    }

    public Book updateDescription(String newDescription) {
        if (newDescription == null) {
            return this;
        }
        return new Book(id, name, author, genre, date, pagesAmount, copiesAmount, newDescription);
    }

    public Book updatedBooksAmount(int newBooksAmount) {
        return new Book(id, name, author, genre, date, pagesAmount, newBooksAmount, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return pagesAmount == book.pagesAmount && copiesAmount == book.copiesAmount && Objects.equals(id, book.id) && Objects.equals(name, book.name) && Objects.equals(author, book.author) && genre == book.genre && Objects.equals(date, book.date) && Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, genre, date, pagesAmount, copiesAmount, description);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", genre=" + genre +
                ", date=" + date +
                ", pagesAmount=" + pagesAmount +
                ", copiesAmount=" + copiesAmount +
                ", description='" + description + '\'' +
                '}';
    }
}
