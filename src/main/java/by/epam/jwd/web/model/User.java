package by.epam.jwd.web.model;

import java.util.Objects;

public class User implements DbEntity {
    private final Long id;
    private final String login;
    private final String password;
    private final UserRole role;
    private final Subscription subscription;

    public User(Long id, String login, String password, UserRole role, Subscription subscription) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.subscription = subscription;
    }

    public User(String login, String password, UserRole role, Subscription subscription) {
        this(null, login, password, role, subscription);
    }

    public User(String login, String password, UserRole role) {
        this(null, login, password, role, null);
    }

    public User(String login , String password) {
        this(login, password, UserRole.READER);
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


    public Subscription getSubscription() {
        return subscription;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(id, that.id) && Objects.equals(login, that.login) && Objects.equals(password, that.password) && role == that.role && Objects.equals(subscription, that.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, subscription);
    }

    @Override
    public String toString() {
        return "LibraryUser{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", userSubscription=" + subscription +
                '}';
    }
}
