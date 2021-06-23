package by.epam.jwd.web.model;

import java.util.Objects;

public enum Genre implements DbEntity {
    FICTION(1L),
    FANTASY(2L),
    SCIENCE(3L);

    private final Long id;

    Genre(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public static Genre getInstance(Long id) {
        for (Genre genre : Genre.values()) {
            if (genre.getId().equals(id)) {
                return genre;
            }
        }
        throw new IllegalArgumentException(String.format("There is no enum constant with id %d", id));
    }

}
