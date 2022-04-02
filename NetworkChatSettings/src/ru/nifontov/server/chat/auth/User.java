package ru.nifontov.server.chat.auth;

public class User {
    private final String login;
    private final String password;
    private final String userName;

    public User(String login, String password, String userName) {
        this.login = login;
        this.password = password;
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.equals(user.getLogin()) && password.equals(user.getPassword());
    }

    public User(String login, String password) {
        this(login, password, null);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}