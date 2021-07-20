package by.epam.jwd.web.model;

import java.util.Objects;

public class Like implements DbEntity {
    private final Long id;
    private final Book book;
    private final User user;

    public Like(Long id, User user, Book book) {
        this.id = id;
        this.book = book;
        this.user = user;
    }

    public Like(User user, Book book) {
        this(null, user, book);
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return Objects.equals(id, like.id) && Objects.equals(book, like.book) && Objects.equals(user, like.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, user);
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", book=" + book +
                ", user=" + user +
                '}';
    }
}
