package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.entities.CorrectAnswer;
import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.Topic;
import course.springdata.quizapplication.entities.WrongAnswer;
import course.springdata.quizapplication.service.CorrectAnswerService;
import course.springdata.quizapplication.service.QuestionService;
import course.springdata.quizapplication.service.TopicService;
import course.springdata.quizapplication.service.WrongAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class AdminPanel {

    private final BufferedReader bufferedReader;
    private final TopicService topicService;
    private final QuestionService questionService;
    private final CorrectAnswerService correctAnswerService;
    private final WrongAnswerService wrongAnswerService;

    @Autowired
    public AdminPanel(TopicService topicService, QuestionService questionService, CorrectAnswerService correctAnswerService,
                      WrongAnswerService wrongAnswerService){
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        this.topicService = topicService;
        this.questionService = questionService;
        this.correctAnswerService = correctAnswerService;
        this.wrongAnswerService = wrongAnswerService;
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
}
