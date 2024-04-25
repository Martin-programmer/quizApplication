package course.springdata.quizapplication.controller.authentication;

import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.entities.User;
import course.springdata.quizapplication.enums.Command;
import course.springdata.quizapplication.enums.Role;
import course.springdata.quizapplication.service.AdminService;
import course.springdata.quizapplication.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Authentication {
    private final UserService userService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;
    private final InputHandler inputHandler;
    @Getter
    private String email = "";

    @Autowired
    public Authentication(UserService userService, AdminService adminService, PasswordEncoder passwordEncoder, InputHandler inputHandler){
        this.userService = userService;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
        this.inputHandler = inputHandler;
    }

    public Role authenticateUser() throws Exception {
        Role role = inputHandler.promptForRole();
        Command command = inputHandler.promptForCommand("Login for existing account or Register for new one!");
        processLoginOrRegister(role, command);
        return role;
    }

    private void processLoginOrRegister(Role role, Command command) throws Exception {
        boolean success = false;
        while (!success) {
            success = inputHandler.handleCredentialsInput(role, command, this);
        }
    }

    public boolean performLogin(Role role, String email, String password) {
        this.email = email;
        if (role == Role.ADMIN) {
            return adminService.loginAdmin(email, password);
        } else if (role == Role.USER) {
            return userService.loginUser(email, password);
        }
        return false;
    }

    public boolean performRegistration(Role role, String firstName, String lastName, String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        this.email = email;
        if (role == Role.ADMIN) {
            return adminService.registerAdmin(new Admin(firstName, lastName, email, encodedPassword));
        } else if (role == Role.USER) {
            return userService.registerUser(new User(firstName, lastName, email, encodedPassword));
        }
        return false;
    }
}
