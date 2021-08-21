package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity that represents book in application.
 * Book may have multiple copies.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class Book implements DbEntity {
    private final Long id;
    private final String name;
    private final String author;
    private final Genre genre;
    private final LocalDate date;
    private final Integer pagesAmount;
    private final Integer copiesAmount;
    private final String text;
    private final Integer likesAmount;
    private final Integer commentsAmount;

    /**
     * Book constructor with id generated by database.
     * Used when it's need to map database table data to instance.
     * @param id book id from database.
     * @param name book name.
     * @param author book author name.
     * @param genre book genre.
     * @param date book date.
     * @param pagesAmount book pages amount.
     * @param copiesAmount book copies amount.
     * @param text book text.
     * @param likesAmount book likes amount.
     * @param commentsAmount book comments amount.
     */
    public Book(Long id, String name, String author, Genre genre, LocalDate date, Integer pagesAmount, Integer copiesAmount, String text, Integer likesAmount, Integer commentsAmount) {
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

    /**
     * Book constructor without id.
     * Used when it's need to register and save book in database table.
     * @param name book name.
     * @param author book author name.
     * @param genre book genre.
     * @param date book date.
     * @param pagesAmount book pages amount.
     * @param copiesAmount book copies amount.
     * @param text book text.
     * @param likesAmount book likes amount.
     * @param commentsAmount book copies amount.
     */
    public Book(String name, String author, Genre genre, LocalDate date, Integer pagesAmount, Integer copiesAmount, String text, Integer likesAmount, Integer commentsAmount) {
        this(null, name, author, genre, date, pagesAmount, copiesAmount, text, likesAmount, commentsAmount);
    }

    /**
     * Book constructor.
     * @param name book name.
     * @param author book author name.
     * @param genre book genre.
     * @param date book date.
     * @param pagesAmount book pages amount.
     * @param text book text.
     */
    public Book(String name, String author, Genre genre, LocalDate date, Integer pagesAmount, String text) {
        this(null, name, author, genre, date, pagesAmount, 1, text, 0, 0);
    }

    /**
     * Book constructor.
     * @param name book name.
     * @param author book author name.
     * @param genre book genre.
     * @param pagesAmount book pages amount.
     * @param text book text.
     */
    public Book(String name, String author, Genre genre, Integer pagesAmount, String text) {
        this(name, author, genre, LocalDate.now(), pagesAmount, text);
    }

    /**
     * Returns book id generated by database.
     * @return book id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Returns book name.
     * @return book name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns book author.
     * @return book author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns book genre.
     * @return book genre.
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * Returns book date.
     * @return book date.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns book pages amount.
     * @return pages amount.
     */
    public int getPagesAmount() { return pagesAmount; }

    /**
     * Returns book text.
     * @return book text.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns book copies amount.
     * @return copies amount.
     */
    public int getCopiesAmount() {
        return copiesAmount;
    }

    /**
     * Returns likes amount.
     * @return likes amount.
     */
    public int getLikesAmount() {
        return likesAmount;
    }

    /**
     * Returns comments amount.
     * @return comments amount.
     */
    public int getCommentsAmount() {
        return commentsAmount;
    }

    /**
     * Check whether two object are equal to each other.
     * @param o other object.
     * @return {@code true} id objects are equal or {@false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(name, book.name) && Objects.equals(author, book.author) && genre == book.genre && Objects.equals(date, book.date) && Objects.equals(pagesAmount, book.pagesAmount) && Objects.equals(copiesAmount, book.copiesAmount) && Objects.equals(text, book.text) && Objects.equals(likesAmount, book.likesAmount) && Objects.equals(commentsAmount, book.commentsAmount);
    }

    /**
     * Generates instance hash code.
     * @return generated instance hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, genre, date, pagesAmount, copiesAmount, text, likesAmount, commentsAmount);
    }

    /**
     * Makes instance string representation.
     * @return instance string representation.
     */
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
