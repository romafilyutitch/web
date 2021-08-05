package by.epam.jwd.web.model;

/**
 * Enumeration represents order's current status.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public enum Status implements DbEntity {
    ORDERED(1L),
    APPROVED(2L),
    RETURNED(3L);

    private final Long id;

    /**
     * Enumeration constructor.
     * @param id enumeration id.
     */
    Status(Long id) {
        this.id = id;
    }

    /**
     * Returns enumeration id.
     * @return enumeration id.
     */
    @Override
    public Long getId() {
        return id;
    }

}
