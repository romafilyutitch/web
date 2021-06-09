package by.epam.jwd.web.model;

import java.util.Objects;

public class BookAuthor implements DbEntity {
    private final Long id;
    private final String name;

    public BookAuthor(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public BookAuthor(String name) {
        this(null, name);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAuthor that = (BookAuthor) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "BookAuthor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
