package by.epam.jwd.web.model;

import java.util.Arrays;
import java.util.List;

/**
 * Enumeration represents user role.
 * Used to define user commands permission and available user actions.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public enum UserRole implements DbEntity {
    READER(1L),
    LIBRARIAN(2L),
    ADMIN(3L),
    UNAUTHORIZED(4L);

    public static final List<UserRole> LIST_OF_ROLES = Arrays.asList(values());
    private final Long id;

    /**
     * Enumeration constructor.
     * @param id enumeration item id.
     */
    UserRole(Long id) {
        this.id = id;
    }

    /**
     * Returns roles in list form.
     * @return all roles in list form.
     */
    public static List<UserRole> rolesAsList() {
        return LIST_OF_ROLES;
    }

    /**
     * Returns enumeration item id.
     * @return enumeration item id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns role that is promotion role to current role.
     * @return promoted role.
     */
    public UserRole promote() {
        final UserRole[] values = values();
        int ordinal = ordinal();
        if (ordinal == UserRole.ADMIN.ordinal()) {
            return values[ordinal];
        } else {
            return values[++ordinal];
        }
    }

    /**
     * Returns role that is demotion role to current role.
     * @return demoted role.
     */
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
