package by.epam.jwd.web.model;

import java.util.Objects;

/**
 * Entity that represents like that user add to book.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class Like implements DbEntity {
    private final Long id;
    private final Book book;
    private final User user;

    /**
     * Constructor with id.
     * Used when it's need to map database table data to instance.
     * @param id saved like id.
     * @param user saved like user.
     * @param book saved like book.
     */
    public Like(Long id, User user, Book book) {
        this.id = id;
        this.book = book;
        this.user = user;
    }

    /**
     * Constructor without id.
     * Used when it's need to register and save like in database.
     * @param user like user.
     * @param book like book.
     */
    public Like(User user, Book book) {
        this(null, user, book);
    }

    /**
     * Returns saved like id.
     * @return saved like id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns book to what like added.
     * @return like book.
     */
    public Book getBook() {
        return book;
    }

    /**
     * Returns user who add like.
     * @return like user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Check weather two objects are equal to each other.
     * @param o other object.
     * @return {@code true} if objects are equal or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(id, like.id) && Objects.equals(book, like.book) && Objects.equals(user, like.user);
    }

    /**
     * Calculates instance hash code.
     * @return instance hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, book, user);
    }

    /**
     * Makes instance string representation.
     * @return instance string representation.
     */
    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", book=" + book +
                ", user=" + user +
                '}';
    }
}
