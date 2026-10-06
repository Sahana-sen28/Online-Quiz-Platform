package quizmaster.service;

import quizmaster.dao.UserDAO;
import quizmaster.model.User;
import quizmaster.util.PasswordUtil;

import java.sql.Timestamp;

public class AuthenticationService {

    private final UserDAO userDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String plainPassword) {
        User user = userDAO.findByUsername(username);
        if (user != null) {
            String hash = PasswordUtil.hashPassword(plainPassword);
            if (hash.equals(user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }

    public boolean register(String username, String plainPassword, String fullName) {
        if (username == null || username.trim().isEmpty() || plainPassword == null || plainPassword.isEmpty() || fullName == null || fullName.trim().isEmpty()) {
            return false;
        }
        if (isUsernameTaken(username)) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtil.hashPassword(plainPassword));
        user.setFullName(fullName);
        user.setRole("STUDENT");
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return userDAO.createUser(user);
    }

    public boolean isUsernameTaken(String username) {
        return userDAO.findByUsername(username) != null;
    }
}
