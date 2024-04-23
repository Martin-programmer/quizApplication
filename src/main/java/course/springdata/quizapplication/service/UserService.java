package course.springdata.quizapplication.service;

import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.entities.User;

import java.util.List;

public interface UserService {
    boolean registerUser(User user);
    boolean loginUser(String email, String password);

    User getUserByEmail(String email);

    List<User> getTopFiveUsers();
}
