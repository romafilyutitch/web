package by.epam.jwd.web.model;

import java.util.Objects;

public class Author implements DbEntity {
    private final Long id;
    private final String name;

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(Long id) {
        this(id, null);
    }

    public Author(String name) {
        this(null, name);
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Author updateName(String newName) {
        if (newName == null) {
            return this;
        }
        return new Author(id, newName);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author that = (Author) o;
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
