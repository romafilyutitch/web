package by.epam.jwd.web.model;

public enum UserRole implements DbEntity {
    READER(1L, "Reader"), LIBRARIAN(2L, "Librarian"), ADMIN(3L, "Admin");

    private final Long id;
    private final String name;

    private UserRole(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getRoleName() {
        return name;
    }


    public static UserRole getRoleById(Long id) {
        for (UserRole userRole : values()) {
            if (userRole.getId().equals(id)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("There is no role with that id");
    }

    public static UserRole getRoleByName(String name) {
        for (UserRole userRole : values()) {
            if (userRole.getRoleName().equals(name)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("There is no rol with that id");
    }
}
