package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.entities.*;
import course.springdata.quizapplication.enums.Command;
import course.springdata.quizapplication.enums.Role;
import course.springdata.quizapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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
    private User user;
    private String email = "";

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
        this.user = null;
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
        if (command == Command.LOGIN){
            System.out.println("Enter your email and password (separated by '/'): ");
        }else{
            System.out.println("Enter your first name, last name, email, and password (separated by '/'): ");
        }
        String input = bufferedReader.readLine();
        String[] tokens = input.split("/");
        if (tokens.length < 2) {
            System.out.println("Invalid input format. Please ensure you provide all required details.");
            return false;
        }
        String firstName = "";
        String lastName = "";
        String email = "";
        String password = "";
        if (command == Command.LOGIN){
            email = tokens[0].trim();
            password = tokens[1].trim();
        }else if (command == Command.REGISTER){
            firstName = tokens[0].trim();
            lastName = tokens[1].trim();
            email = tokens[2].trim();
            password = tokens[3].trim();
        }

        return command == Command.LOGIN ? performLogin(role, email, password) :
                performRegistration(role, firstName, lastName, email, password);
    }

    private Command promptForCommand(String message) throws IOException {
        System.out.println(message);
        return Command.valueOf(bufferedReader.readLine().trim().toUpperCase());
    }

    public void handleGameplay() throws IOException {
        System.out.println("Type PLAY if you want to play or type STATS to see your statistics" +
                " or TOP to see top 5 players!");
        user = userService.getUserByEmail(this.email);
        String command = bufferedReader.readLine();
        while (!command.equals("END")){
            switch (command) {
                case "PLAY" -> {
                    System.out.println("Choose topic of your questions!");
                    StringBuilder sb = new StringBuilder();
                    sb.append("You can play: {all, ");
                    topicService.getAllTopics().forEach(topic -> sb.append(topic.getTopicName()).append(", "));
                    sb.delete(sb.length()-2, sb.length());
                    sb.append("}");
                    System.out.println(sb);
                    System.out.println("Type your selection: ");
                    String selection = bufferedReader.readLine();
                    Set<Question> questions = new HashSet<>();
                    if (selection.equals("all")) {
                        questions.addAll(questionService.getTenRandomQuestions());
                    } else {
                        questions.addAll(questionService.getQuestionsByTopicName(selection));
                    }
                    questions
                            .forEach(question -> {
                                System.out.println("-------------------------------------------------");
                                System.out.println(question.getQuestion());
                                String correctAnswer = question.getCorrectAnswer().getCorrectAnswer();
                                List<String> answers = new ArrayList<>();
                                answers.add(correctAnswer);
                                Set<WrongAnswer> wrongAnswers = question.getWrongAnswers();
                                for (WrongAnswer wrongAnswer : wrongAnswers) {
                                    answers.add(wrongAnswer.getWrongAnswer());
                                }
                                Collections.shuffle(answers);
                                //65 to 68 ASCII SYMBOLS
                                char symbol = 65;
                                char correctSymbol = 'z';
                                for (String answer : answers) {
                                    if (answer.equals(correctAnswer)) {
                                        correctSymbol = symbol;
                                    }
                                    System.out.printf("%c: %s%n", symbol, answer);
                                    symbol++;
                                }
                                try {
                                    System.out.print("Your answer: ");
                                    String userAnswer = bufferedReader.readLine();
                                    if (userAnswer.charAt(0) == correctSymbol) {
                                        System.out.println("CORRECT ANSWER");
                                        user.setPoints(user.getPoints() + 1);
                                    } else {
                                        System.out.println("WRONG ANSWER");
                                        System.out.println("Right answer is: " + correctAnswer);
                                    }
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                    getStatistics();
                }
                case "STATS" -> getStatistics();
                case "TOP" -> getTopFivePlayers();
            }
            System.out.println("Type END if you want to stop program.");
            System.out.println("Type PLAY if you want to play or type STATS to see your statistics" +
                    " or TOP to see top 5 players!");
            command = bufferedReader.readLine();
        }
    }

    public void handleAdminPanel() throws IOException {
        printInstructions();
        String input;
        while (!(input = readInput()).equals("END")) {
            handleCommand(input);
            printInstructions();
        }
    }

    private void printInstructions() {
        System.out.println("Type 'END' to stop the program, or type 'ADD' to add a new question.");
    }

    private String readInput() throws IOException {
        return bufferedReader.readLine().trim();
    }

    private void handleCommand(String command) throws IOException {
        if ("ADD".equals(command)) {
            addQuestionProcess();
        } else {
            System.out.println("Invalid command. Please type 'ADD' to add a new question or 'END' to exit.");
        }
    }

    private void addQuestionProcess() throws IOException {
        Topic topic = handleTopic();
        Question question = createQuestion(topic);
        addAnswers(question);
        System.out.println("Your question is successfully added!");
    }

    private Topic handleTopic() throws IOException {
        List<Topic> allTopics = topicService.getAllTopics();
        allTopics.forEach(topic -> System.out.println(topic.getTopicName()));

        System.out.println("Is the topic of your question listed above? Answer (y/n)");
        String response = readInput();

        if ("y".equalsIgnoreCase(response)) {
            System.out.println("Type the name of your topic!");
            return topicService.getTopicByTopicName(readInput());
        } else if ("n".equalsIgnoreCase(response)) {
            return createNewTopic();
        } else {
            System.out.println("Invalid response. Assuming 'no'.");
            return createNewTopic();
        }
    }

    private Topic createNewTopic() throws IOException {
        System.out.println("Type the name of the new topic:");
        Topic topic = new Topic();
        topic.setTopicName(readInput());
        topicService.addNewTopic(topic);
        return topic;
    }

    private Question createQuestion(Topic topic) throws IOException {
        System.out.println("Type your question:");
        Question question = new Question();
        question.setQuestion(readInput());
        questionService.addNewQuestion(question,topic);
        return question;
    }

    private void addAnswers(Question question) throws IOException {
        System.out.println("Type the correct answer for your question:");
        CorrectAnswer correctAnswer = new CorrectAnswer();
        correctAnswer.setCorrectAnswer(readInput());
        correctAnswerService.addNewCorrectAnswer(question, correctAnswer);

        System.out.println("Now you need to add 3 wrong answers.");
        for (int i = 0; i < 3; i++) {
            System.out.println("Type wrong answer " + (i + 1) + ":");
            WrongAnswer wrongAnswer = new WrongAnswer();
            wrongAnswer.setWrongAnswer(readInput());
            wrongAnswerService.addNewWrongAnswer(question, wrongAnswer);
        }
    }


    private void getTopFivePlayers() {
        List<User> topFiveUsers = userService.getTopFiveUsers();
        for (int i = 1; i <= topFiveUsers.size(); i++) {
            User currentUser = topFiveUsers.get(i - 1);
            System.out.println("--------------------------------------");
            System.out.printf("%d. Name: %s%nPoints: %s%n",i, currentUser.getFirstName(),currentUser.getPoints());
        }
        System.out.println("--------------------------------------");
    }

    private void getStatistics() {
        System.out.println("--------------------------------------");
        System.out.printf("First name: %s%n",user.getFirstName());
        System.out.printf("Last name: %s%n",user.getLastName());
        System.out.printf("Email: %s%n",user.getEmail());
        System.out.printf("Points: %s%n",user.getPoints());
        System.out.println("--------------------------------------");
    }

    private boolean performLogin(Role role, String email, String password) {
        boolean success = false;
        if (role == Role.ADMIN) {
            success = adminService.loginAdmin(email, password);
        } else if (role == Role.USER) {
            success = userService.loginUser(email, password);
        }
        this.email = email;
        return success;
    }



    private boolean performRegistration(Role role, String firstName, String lastName, String email, String password) {
        boolean success = false;
        if (role == Role.ADMIN) {
            success = adminService.registerAdmin(new Admin(firstName, lastName, email, passwordEncoder.encode(password)));
        } else if (role == Role.USER) {
            success = userService.registerUser(new User(firstName, lastName, email, passwordEncoder.encode(password)));
        }
        this.email = email;
        return success;
    }
}
