package course.springdata.quizapplication.service;

import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.entities.User;

public interface UserService {
    boolean registerUser(User user);
    boolean loginUser(String email, String password);

    User getUserByEmail(String email);
}
