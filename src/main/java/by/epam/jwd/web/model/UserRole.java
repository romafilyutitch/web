package by.epam.jwd.web.model;

import java.util.Arrays;
import java.util.List;

public enum UserRole implements DbEntity {
    READER(1L),
    LIBRARIAN(2L),
    ADMIN(3L),
    UNAUTHORIZED(4L);

    public static final List<UserRole> LIST_OF_ROLES = Arrays.asList(values());
    private final Long id;

    UserRole(Long id) {
        this.id = id;
    }

    public static List<UserRole> rolesAsList() {
        return LIST_OF_ROLES;
    }

    public Long getId() {
        return id;
    }

    public static UserRole getInstance(Long id) {
        for (UserRole userRole : values()) {
            if (userRole.getId().equals(id)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("There is no role with that id");
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
