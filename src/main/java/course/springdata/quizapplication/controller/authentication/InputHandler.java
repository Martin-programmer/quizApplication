package course.springdata.quizapplication.controller.authentication;

import course.springdata.quizapplication.enums.Command;
import course.springdata.quizapplication.enums.Role;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class InputHandler {
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public Role promptForRole() throws IOException {
        System.out.println("Type Admin or User to login/register");
        return Role.valueOf(bufferedReader.readLine().trim().toUpperCase());
    }

    public Command promptForCommand(String message) throws IOException {
        System.out.println(message);
        return Command.valueOf(bufferedReader.readLine().trim().toUpperCase());
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
        System.out.println("Enter your first name, last name, email, and password (separated by '/'): ");
        return bufferedReader.readLine().split("/");
    }
}
