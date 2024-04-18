package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.Topic;
import course.springdata.quizapplication.entities.User;
import course.springdata.quizapplication.enums.Command;
import course.springdata.quizapplication.enums.Role;
import course.springdata.quizapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Set;

@Controller
public class AppController implements CommandLineRunner {
    private final AdminService adminService;
    private final UserService userService;
    private final BufferedReader bufferedReader;
    private final PasswordEncoder passwordEncoder;
    private final TopicService topicService;
    private final QuestionService questionService;
    private final CorrectAnswerService correctAnswerService;

    @Autowired
    public AppController(AdminService adminService, UserService userService,TopicService topicService,
                         QuestionService questionService, CorrectAnswerService correctAnswerService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.userService = userService;
        this.topicService = topicService;
        this.questionService = questionService;
        this.correctAnswerService = correctAnswerService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        topicService.seedTopics();
        questionService.seedQuestions();
        correctAnswerService.seedCorrectAnswers();
        //SEED QUESTIONS AND THEIR TOPICS AND TEST THEM
//        topicService.seedTopics();
//        questionService.seedQuestions();
//        Question questionById = questionService.getQuestionById(1L);
//        System.out.println(questionById.getQuestion());
//        System.out.println(questionById.getTopic().getTopicName());
//        Topic topicById = topicService.getTopicById(1L);
//        topicById.getQuestions().forEach(question -> System.out.println(question.getQuestion()));

        //LOGIN/REGISTER
//        System.out.println("Type Admin or User to login/register");
//        Role role = Role.valueOf(bufferedReader.readLine().trim().toUpperCase());
//        processLoginOrRegister(role);
    }

    private void processLoginOrRegister(Role role) throws Exception {
        System.out.println("Type Login for existing account or Register for new one!");
        Command command = Command.valueOf(bufferedReader.readLine().trim().toUpperCase());
        System.out.println("Enter your first name, last name, email, and password (separated by '/'): ");
        String input = bufferedReader.readLine();
        String[] tokens = input.split("/");

        if (tokens.length < 4) {
            System.out.println("Invalid input format. Please ensure you provide all required details.");
            return;
        }

        String firstName = tokens[0].trim();
        String lastName = tokens[1].trim();
        String email = tokens[2].trim();
        String password = tokens[3].trim();

        if (command == Command.LOGIN) {
            performLogin(role, email, password);
        } else if (command == Command.REGISTER) {
            performRegistration(role, firstName, lastName, email, password);
        }
    }

    private void performLogin(Role role, String email, String password) {
        if (role == Role.ADMIN) {
            adminService.loginAdmin(email, password);
        } else if (role == Role.USER) {
            userService.loginUser(email, password);
        }
    }

    private void performRegistration(Role role, String firstName, String lastName, String email, String password) {
        if (role == Role.ADMIN) {
            adminService.registerAdmin(new Admin(firstName, lastName, email, passwordEncoder.encode(password)));
        } else if (role == Role.USER) {
            userService.registerUser(new User(firstName, lastName, email, passwordEncoder.encode(password)));
        }
    }
}