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

    public static Status getInstance(Long id) {
        for (Status status : Status.values()) {
            if (status.getId().equals(id)) {
                return status;
            }
        }
        throw new IllegalArgumentException(String.format("There is no enum constraint with id %d", id));
    }

}
