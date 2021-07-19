package by.epam.jwd.web.model;

public enum Status implements DbEntity {
    ORDERED(1L),
    APPROVED(2L),
    RETURNED(3L);


    private final Long id;

    Status(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

}
