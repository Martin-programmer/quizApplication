package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.User;
import course.springdata.quizapplication.entities.WrongAnswer;
import course.springdata.quizapplication.enums.Command;
import course.springdata.quizapplication.enums.Role;
import course.springdata.quizapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Component
public class InteractionService {
    private final UserService userService;
    private final AdminService adminService;
    private final TopicService topicService;
    private final QuestionService questionService;
    private final CorrectAnswerService correctAnswerService;
    private final WrongAnswerService wrongAnswerService;
    private final BufferedReader bufferedReader;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public InteractionService(UserService userService, AdminService adminService, TopicService topicService,
                              QuestionService questionService, CorrectAnswerService correctAnswerService,
                              WrongAnswerService wrongAnswerService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.adminService = adminService;
        this.topicService = topicService;
        this.questionService = questionService;
        this.correctAnswerService = correctAnswerService;
        this.wrongAnswerService = wrongAnswerService;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.passwordEncoder = passwordEncoder;
    }

    public void seedData() throws IOException {
        topicService.seedTopics();
        questionService.seedQuestions();
        wrongAnswerService.seedWrongAnswers();
        correctAnswerService.seedCorrectAnswers();
    }

    public Role promptForRole() throws IOException {
        System.out.println("Type Admin or User to login/register");
        return Role.valueOf(bufferedReader.readLine().trim().toUpperCase());
    }

    public void processLoginOrRegister(Role role) throws Exception {
        Command command = promptForCommand("Login for existing account or Register for new one!");
        boolean success = false;
        while (!success) {
            success = handleCredentialsInput(role, command);
        }
    }

    private boolean handleCredentialsInput(Role role, Command command) throws IOException {
        System.out.println("Enter your first name, last name, email, and password (separated by '/'): ");
        String input = bufferedReader.readLine();
        String[] tokens = input.split("/");
        if (tokens.length < 4) {
            System.out.println("Invalid input format. Please ensure you provide all required details.");
            return false;
        }
        String firstName = tokens[0].trim();
        String lastName = tokens[1].trim();
        String email = tokens[2].trim();
        String password = tokens[3].trim();
        return command == Command.LOGIN ? performLogin(role, email, password) :
                performRegistration(role, firstName, lastName, email, password);
    }

    private Command promptForCommand(String message) throws IOException {
        System.out.println(message);
        return Command.valueOf(bufferedReader.readLine().trim().toUpperCase());
    }

    public void handleGameplay() throws IOException {
        System.out.println("Type PLAY if you want to play or type STATS to see your statistics!");
        String command = bufferedReader.readLine();
        //TODO make stats
        if (command.equals("PLAY")){
            System.out.println("Choose topic of your questions!");
            System.out.println("You can play all, world geography, history, science and nature, " +
                    "sports, movies and entertainment, literature");
            System.out.println("Type your selection: ");
            String selection = bufferedReader.readLine();
            if (selection.equals("all")){
                //TODO:
            }else{
                Set<Question> questionsByTopicName = questionService.getQuestionsByTopicName(selection);
                questionsByTopicName
                        .forEach(question -> {
                            System.out.println("-------------------------------------------------");
                            System.out.println(question.getQuestion());
                            String correctAnswer = question.getCorrectAnswer().getCorrectAnswer();
                            Set<String> answers = new HashSet<>();
                            answers.add(correctAnswer);
                            Set<WrongAnswer> wrongAnswers = question.getWrongAnswers();
                            for (WrongAnswer wrongAnswer : wrongAnswers) {
                                answers.add(wrongAnswer.getWrongAnswer());
                            }
                            answers.forEach(System.out::println);
                            try {
                                System.out.print("Your answer: ");
                                String userAnswer = bufferedReader.readLine();
                                if (userAnswer.equals(correctAnswer)){
                                    System.out.println("CORRECT ANSWER");
                                }else{
                                    System.out.println("WRONG ANSWER");
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }
    }

    private boolean performLogin(Role role, String email, String password) {
        boolean success = false;
        if (role == Role.ADMIN) {
            success = adminService.loginAdmin(email, password);
        } else if (role == Role.USER) {
            success = userService.loginUser(email, password);
        }
        return success;
    }

    private boolean performRegistration(Role role, String firstName, String lastName, String email, String password) {
        boolean success = false;
        if (role == Role.ADMIN) {
            success = adminService.registerAdmin(new Admin(firstName, lastName, email, passwordEncoder.encode(password)));
        } else if (role == Role.USER) {
            success = userService.registerUser(new User(firstName, lastName, email, passwordEncoder.encode(password)));
        }
        return success;
    }
}
