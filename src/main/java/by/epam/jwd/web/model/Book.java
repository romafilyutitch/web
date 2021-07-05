package by.epam.jwd.web.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.List;

public class Book implements DbEntity {
    private final Long id;
    private final String name;
    private final Author author;
    private final Genre genre;
    private final LocalDate date;
    private final Integer pagesAmount;
    private final Integer copiesAmount;
    private final String description;
    private final List<Comment> comments;
    private final Integer likes;

    public Book(Long id, String name, Author author, Genre genre, LocalDate date, Integer pagesAmount, Integer copiesAmount, String description, List<Comment> comments, Integer likes) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.date = date;
        this.pagesAmount = pagesAmount;
        this.copiesAmount = copiesAmount;
        this.description = description;
        this.comments = comments;
        this.likes = likes;
    }

    public Book(Long id) {
        this(id, null, null, null,null, null, null, null, null, null);
    }

    public Book(String name, Author author, Genre genre, LocalDate date, int pagesAmount, String description, List<Comment> comments, Integer likes) {
        this(null, name, author, genre, date, pagesAmount, 1, description, comments, likes);
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

    public List<Comment> getComments() {
        return comments;
    }

    public Integer getLikes() {
        return likes;
    }


    public Book updateName(String newName) {
        if (newName == null) {
            return this;
        }
        return new Book(id, newName, author, genre, date, pagesAmount, copiesAmount, description, comments, likes);
    }

    public Book updateAuthor(Author newAuthor) {
        if (newAuthor == null) {
            return this;
        }
        return new Book(id, name, newAuthor, genre, date, pagesAmount, copiesAmount, description, comments, likes);
    }

    public Book updateGenre(Genre newGenre) {
        if (newGenre == null) {
            return this;
        }
        return new Book(id, name, author, newGenre, date, pagesAmount, copiesAmount, description, comments, likes);
    }

    public Book updateDate(LocalDate newDate) {
        if (newDate == null) {
            return this;
        }
        return new Book(id, name, author, genre, newDate, pagesAmount, copiesAmount, description, comments, likes);
    }

    public Book updatePagesAmount(int newPagesAmount) {
        return new Book(id, name, author, genre, date, newPagesAmount, copiesAmount, description, comments, likes);
    }

    public Book updateDescription(String newDescription) {
        if (newDescription == null) {
            return this;
        }
        return new Book(id, name, author, genre, date, pagesAmount, copiesAmount, newDescription, comments, likes);
    }

    public Book updatedBooksAmount(int newBooksAmount) {
        return new Book(id, name, author, genre, date, pagesAmount, newBooksAmount, description, comments, likes);
    }

    public Book updateBookComments(List<Comment> newComments) {
        if (comments == null) {
            return this;
        }
        return new Book(id, name, author, genre, date, pagesAmount, copiesAmount, description, newComments, likes);
    }

    public Book updateLikes(Integer likes) {
        if (likes == null) {
            return this;
        }
        return new Book(id, name, author, genre, date, pagesAmount, copiesAmount, description, comments, likes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(name, book.name) && Objects.equals(author, book.author) && genre == book.genre && Objects.equals(date, book.date) && Objects.equals(pagesAmount, book.pagesAmount) && Objects.equals(copiesAmount, book.copiesAmount) && Objects.equals(description, book.description) && Objects.equals(comments, book.comments) && Objects.equals(likes, book.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, genre, date, pagesAmount, copiesAmount, description, comments, likes);
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
                ", comments=" + comments +
                ", likes=" + likes +
                '}';
    }
}
