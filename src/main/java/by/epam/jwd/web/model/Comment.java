package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class Comment implements DbEntity{
    private final Long id;
    private final User user;
    private final Book book;
    private final LocalDate date;
    private final String text;

    public Comment(Long id) {
        this.id = id;
        this.user = null;
        this.book = null;
        this.date = null;
        this.text = null;

    }

    public Comment(Long id, User user, Book book, LocalDate date, String text) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.date = date;
        this.text = text;
    }

    public Comment(User user, Book book, LocalDate date, String text) {
        this(null, user, book, date, text);
    }

    @Override
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(user, comment.user) && Objects.equals(book, comment.book) && Objects.equals(date, comment.date) && Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, book, date, text);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", date=" + date +
                ", text='" + text + '\'' +
                '}';
    }
}
