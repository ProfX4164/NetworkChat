package ru.nifontov.server.chat.auth;

import java.util.Set;

public class AuthService {

    private static final Set<User> USERS = Set.of(
            new User("login1", "pass1", "username1"),
            new User("login2", "pass2", "username2"),
            new User("login3", "pass1", "username1")
    );

    public String getUserNameByLoginAndPassword(String login, String password) {
        User requiredUser = new User(login, password);
        for (User user : USERS) {
            if (requiredUser.equals(user)) {
                return user.getUserName();
            }
        }
        return null;
    }
}
