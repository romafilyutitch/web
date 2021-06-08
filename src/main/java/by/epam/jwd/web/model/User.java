package by.epam.jwd.web.model;

import java.util.Objects;

public class User implements DbEntity {
    private final Long id;
    private final String login;
    private final String password;
    private final UserRole role;
    private final Subscription userSubscription;

    public User(Long id, String login, String password, UserRole role, Subscription subscription) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.userSubscription = subscription;
    }

    public User(String login, String password) {
        this(null, login, password, UserRole.READER, null);
    }

    public User(String login, String password, UserRole role) {
        this(null, login, password, role, null);
    }

    @Override
    public Long getId() {
        return id;
    }


    public String getLogin() {
        return login;
    }


    public String getPassword() {
        return password;
    }


    public UserRole getRole() {
        return role;
    }


    public Subscription getUserSubscription() {
        return userSubscription;
    }

    public User updateLogin(String newLogin) {
        if (newLogin == null) {
            return this;
        }
        return new User(id, newLogin, password, role, userSubscription);
    }

    public User updatePassword(String newPassword) {
        if (newPassword == null) {
            return this;
        }
        return new User(id, login, newPassword, role, userSubscription);
    }

    public User updateRole(UserRole newRole) {
        if (newRole == null) {
            return this;
        }
        return new User(id, login, password, newRole, userSubscription);
    }

    public User updateSubscription(Subscription newSubscription) {
        if (newSubscription == null) {
            return this;
        }
        return new User(id, login, password, role, newSubscription);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id) && Objects.equals(login, that.login) && Objects.equals(password, that.password) && role == that.role && Objects.equals(userSubscription, that.userSubscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, userSubscription);
    }

    @Override
    public String toString() {
        return "LibraryUser{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", userSubscription=" + userSubscription +
                '}';
    }
}
