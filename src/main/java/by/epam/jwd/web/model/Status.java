package by.epam.jwd.web.model;

public enum Status implements DbEntity{
    ORDERED(1L, "ORDERED"),
    APPROVED(2L, "APPROVED");


    private Long id;
    private String name;

    private Status(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
