package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class BookOrder implements DbEntity {
    private final Long id;
    private final User user;
    private final Book book;
    private final LocalDate orderDate;

    public BookOrder(Long id, User user, Book book, LocalDate orderDate) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.orderDate = orderDate;
    }

    public BookOrder(User user, Book book, LocalDate orderDate) {
        this(null, user, book, orderDate);
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

    public LocalDate getOrderDate() {
        return orderDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookOrder bookOrder = (BookOrder) o;
        return Objects.equals(id, bookOrder.id) && Objects.equals(user, bookOrder.user) && Objects.equals(book, bookOrder.book) && Objects.equals(orderDate, bookOrder.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, book, orderDate);
    }

    @Override
    public String toString() {
        return "BookOrder{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", orderDate=" + orderDate +
                '}';
    }
}
