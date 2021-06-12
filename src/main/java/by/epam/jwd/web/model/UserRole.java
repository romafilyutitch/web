package by.epam.jwd.web.model;

import java.util.Arrays;
import java.util.List;

public enum UserRole implements DbEntity {
    READER(1L, "READER"),
    LIBRARIAN(2L, "LIBRARIAN"),
    ADMIN(3L, "ADMIN"),
    UNAUTHORIZED(4L, "UNAUTHORIZED");

    public static final List<UserRole> LIST_OF_ROLES = Arrays.asList(values());
    private final Long id;
    private final String name;

    UserRole(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<UserRole> rolesAsList() {
        return LIST_OF_ROLES;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
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
            if (userRole.getName().equals(name)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("There is no rol with that id");
    }

    public UserRole promote() {
        final UserRole[] values = values();
        int ordinal = ordinal();
        if (ordinal == UserRole.ADMIN.ordinal()) {
            return values[ordinal];
        } else {
            return values[++ordinal];
        }
    }

    public UserRole demote() {
        final UserRole[] values = values();
        int ordinal = ordinal();
        if (ordinal == 0) {
            return values[ordinal];
        } else {
            return values[--ordinal];
        }
    }
}
