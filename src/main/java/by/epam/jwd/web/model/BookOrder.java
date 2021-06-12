package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;

public class BookOrder implements DbEntity {
    private final Long id;
    private final User user;
    private final Book book;
    private final LocalDate orderDate;
    private final Status status;

    public BookOrder(Long id, User user, Book book, LocalDate orderDate, Status status) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.orderDate = orderDate;
        this.status = status;
    }

    public BookOrder(User user, Book book, LocalDate orderDate, Status status) {
        this(null, user, book, orderDate, status);
    }

    public BookOrder(User user, Book book) {
        this(null, user,book, null, null);
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


    public BookOrder updateUser(User newUser) {
        if (newUser == null) {
            return this;
        }
        return new BookOrder(id, newUser, book, orderDate, status);
    }

    public BookOrder updateBook(Book newBook) {
        if(newBook == null) {
            return this;
        }
        return new BookOrder(id, user, newBook, orderDate, status);
    }

    public BookOrder updateOrderDate(LocalDate newDate) {
        if (newDate == null) {
            return this;
        }
        return new BookOrder(id, user, book, newDate, status);
    }

    public BookOrder updateOrderStatus(Status newStatus) {
        if (status == null) {
            return this;
        }
        return new BookOrder(id, user, book, orderDate, newStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookOrder bookOrder = (BookOrder) o;
        return Objects.equals(id, bookOrder.id) && Objects.equals(user, bookOrder.user) && Objects.equals(book, bookOrder.book) && Objects.equals(orderDate, bookOrder.orderDate) && status == bookOrder.status;
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
