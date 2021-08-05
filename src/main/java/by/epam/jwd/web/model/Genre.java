package by.epam.jwd.web.model;

/**
 * Represents book genre.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public enum Genre implements DbEntity {
    FICTION(1L),
    FANTASY(2L),
    SCIENCE(3L);

    private final Long id;

    /**
     * Enumeration constructor.
     * @param id genre id in database.
     */
    Genre(Long id) {
        this.id = id;
    }

    /**
     * Returns saved in database instance id.
     * @return saved instance id.
     */
    @Override
    public Long getId() {
        return id;
    }
}
