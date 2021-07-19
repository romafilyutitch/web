package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class Order implements DbEntity {
    private final Long id;
    private final User user;
    private final Book book;
    private final LocalDate orderDate;
    private final Status status;

    public Order(Long id, User user, Book book, LocalDate orderDate, Status status) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.orderDate = orderDate;
        this.status = status;
    }

    public Order(User user, Book book, LocalDate orderDate, Status status) {
        this(null, user, book, orderDate, status);
    }

    public Order(User user, Book book) {
        this(user, book, LocalDate.now(), Status.ORDERED);
    }

    public Order(Long id) {
        this(id, null, null, null, null);
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

    public Status getStatus() {return status;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(user, order.user) && Objects.equals(book, order.book) && Objects.equals(orderDate, order.orderDate) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, book, orderDate, status);
    }

    @Override
    public String toString() {
        return "BookOrder{" +
                "id=" + id +
                ", user=" + user +
                ", book=" + book +
                ", orderDate=" + orderDate +
                ", status=" + status +
                '}';
    }
}
