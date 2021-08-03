package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class Book implements DbEntity {
    private final Long id;
    private final String name;
    private final Author author;
    private final Genre genre;
    private final LocalDate date;
    private final Integer pagesAmount;
    private final Integer copiesAmount;
    private final String text;
    private final Integer likesAmount;
    private final Integer commentsAmount;

    public Book(Long id, String name, Author author, Genre genre, LocalDate date, Integer pagesAmount, Integer copiesAmount, String text, Integer likesAmount, Integer commentsAmount) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.date = date;
        this.pagesAmount = pagesAmount;
        this.copiesAmount = copiesAmount;
        this.text = text;
        this.likesAmount = likesAmount;
        this.commentsAmount = commentsAmount;
    }

    public Book(String name, Author author, Genre genre, LocalDate date, Integer pagesAmount, Integer copiesAmount, String text, Integer likesAmount, Integer commentsAmount) {
        this(null, name, author, genre, date, pagesAmount, copiesAmount, text, likesAmount, commentsAmount);
    }

    public Book(String name, Author author, Genre genre, LocalDate date, Integer pagesAmount, String text) {
        this(null, name, author, genre, date, pagesAmount, 1, text, 0, 0);
    }

    public Book(String name, Author author, Genre genre, Integer pagesAmount, String text) {
        this(name, author, genre, LocalDate.now(), pagesAmount, text);
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

    public int getPagesAmount() { return pagesAmount; }

    public String getText() {
        return text;
    }

    public int getCopiesAmount() {
        return copiesAmount;
    }

    public int getLikesAmount() {
        return likesAmount;
    }

    public int getCommentsAmount() {
        return commentsAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(name, book.name) && Objects.equals(author, book.author) && genre == book.genre && Objects.equals(date, book.date) && Objects.equals(pagesAmount, book.pagesAmount) && Objects.equals(copiesAmount, book.copiesAmount) && Objects.equals(text, book.text) && Objects.equals(likesAmount, book.likesAmount) && Objects.equals(commentsAmount, book.commentsAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, genre, date, pagesAmount, copiesAmount, text, likesAmount, commentsAmount);
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
                ", text='" + text + '\'' +
                ", likesAmount=" + likesAmount +
                ", commentsAmount=" + commentsAmount +
                '}';
    }
}
