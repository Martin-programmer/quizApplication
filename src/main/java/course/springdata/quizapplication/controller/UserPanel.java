package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.Topic;
import course.springdata.quizapplication.entities.User;
import course.springdata.quizapplication.entities.WrongAnswer;
import course.springdata.quizapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class UserPanel {
    private final BufferedReader bufferedReader;
    private final TopicService topicService;
    private final QuestionService questionService;
    private User user;
    private final UserService userService;
    private String email = "";

    @Autowired
    public UserPanel(TopicService topicService, QuestionService questionService, UserService userService){
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.topicService = topicService;
        this.questionService = questionService;
        this.user = null;
        this.userService = userService;
    }
    public void handleGameplay(String email) throws IOException {
        this.email = email;
        System.out.println("Type PLAY if you want to play or type STATS to see your statistics" +
                " or TOP to see top 5 players!");
        this.user = userService.getUserByEmail(this.email);
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
                    boolean isSelectedTopicInTopics = false;
                    while (!isSelectedTopicInTopics){
                        for (Topic topic : topicService.getAllTopics()) {
                            if (topic.getTopicName().equals(selection)) {
                                isSelectedTopicInTopics = true;
                                break;
                            }
                        }
                        if (!isSelectedTopicInTopics){
                        System.out.println("This topic does not exists in game. Please try again!");
                        System.out.println(sb);
                        selection = bufferedReader.readLine();
                        }
                    }
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
                                StringBuilder allAnswers = new StringBuilder();
                                for (String answer : answers) {
                                    if (answer.equals(correctAnswer)) {
                                        correctSymbol = symbol;
                                    }
                                    if (symbol == 'D'){
                                        allAnswers.append(String.format("%c: %s", symbol, answer));
                                    }else {
                                        allAnswers.append(String.format("%c: %s%n", symbol, answer));
                                    }
                                    symbol++;
                                }
                                System.out.println(allAnswers);
                                try {
                                    System.out.print("Your answer: ");
                                    String userAnswer = bufferedReader.readLine();
                                    while (!(userAnswer.equals("A") || userAnswer.equals("B") || userAnswer.equals("C")
                                    || userAnswer.equals("D"))){
                                        System.err.println("You answer should be: A, B, C or D!");
                                        System.out.println(question.getQuestion());
                                        System.out.println(allAnswers);
                                        System.out.print("Your answer: ");
                                        userAnswer = bufferedReader.readLine();
                                    }
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
}
