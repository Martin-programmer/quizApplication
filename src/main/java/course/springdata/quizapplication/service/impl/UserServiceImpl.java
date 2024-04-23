package course.springdata.quizapplication.service.impl;

import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.entities.User;
import course.springdata.quizapplication.repositories.UserRepository;
import course.springdata.quizapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registerUser(User user) {
        if (userRepository.existsUserByEmail(user.getEmail())){
            System.out.println("User with this email already exists. Please login or try register with other email.");
            return false;
        }
        userRepository.saveAndFlush(user);
        System.out.println("User successfully registered!");
        return true;
    }

    @Override
    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null){
            System.out.println("User not found with email: " + email);
            return false;
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("User successfully logged in!");
            return true;
        } else {
            System.out.println("Incorrect password. Please try again.");
            return false;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getTopFiveUsers() {
        return userRepository.findAllUsersOrderByPoints().stream().limit(5).collect(Collectors.toList());
    }

}
