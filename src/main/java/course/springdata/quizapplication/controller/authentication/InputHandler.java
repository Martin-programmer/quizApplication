package course.springdata.quizapplication.controller.authentication;

import course.springdata.quizapplication.enums.Command;
import course.springdata.quizapplication.enums.Role;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class InputHandler {
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public Role promptForRole() throws IOException {
        System.out.println("Type Admin or User to login/register");
        String role = bufferedReader.readLine();
        while (!(role.equals("Admin") || role.equals("User"))){
            System.out.println("Input error! Please type 'Admin' or 'User'!");
            role = bufferedReader.readLine();
        }
        return Role.valueOf(role.trim().toUpperCase());
    }

    public Command promptForCommand(String message) throws IOException {
        System.out.println(message);
        String command = bufferedReader.readLine();
        while (!(command.equals("Login") || command.equals("Register"))){
            System.out.println("Input error! Please type 'Login' or 'Register'!");
            command = bufferedReader.readLine();
        }
        return Command.valueOf(command.trim().toUpperCase());
    }

    public boolean handleCredentialsInput(Role role, Command command, Authentication authentication) throws IOException {
        String[] tokens = command == Command.LOGIN ? promptLoginDetails() : promptRegistrationDetails();
        if (tokens.length < (command == Command.LOGIN ? 2 : 4)) {
            System.out.println("Invalid input format. Please ensure you provide all required details.");
            return false;
        }
        return command == Command.LOGIN ?
                authentication.performLogin(role, tokens[0], tokens[1]) :
                authentication.performRegistration(role, tokens[0], tokens[1], tokens[2], tokens[3]);
    }

    private String[] promptLoginDetails() throws IOException {
        System.out.println("Enter your email and password (separated by '/'): ");
        return bufferedReader.readLine().split("/");
    }

    private String[] promptRegistrationDetails() throws IOException {
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_" +
                "`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-" +
                "\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:" +
                "[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4]" +
                "[0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]" +
                "|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        boolean hasEmailMatch = false;
        boolean hasPasswordMatch = false;
        String[] tokens = null;
        while (!hasEmailMatch && !hasPasswordMatch) {
            System.out.println("Enter your first name, last name, email, and password (separated by '/'): ");
            tokens = bufferedReader.readLine().split("/");
            hasEmailMatch = emailValidation(tokens, pattern, hasEmailMatch);
            hasPasswordMatch = passwordValidation(tokens, hasPasswordMatch);
        }
        return tokens;
    }

    private static boolean passwordValidation(String[] tokens, boolean hasPasswordMatch) {
        String password = tokens[3];
        if (password.length() < 6){
            System.out.println("Password must be at least 6 symbols length!");
        }else{
            hasPasswordMatch = true;
        }
        return hasPasswordMatch;
    }

    private static boolean emailValidation(String[] tokens, Pattern pattern, boolean hasMatch) {
        String email = tokens[2];
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            hasMatch = true;
        }else{
            System.out.println("Email validation error! Try again with correct email!");
        }
        return hasMatch;
    }
}
