package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class Data {
    private final TopicService topicService;
    private final QuestionService questionService;
    private final CorrectAnswerService correctAnswerService;
    private final WrongAnswerService wrongAnswerService;

    @Autowired
    public Data(TopicService topicService,
                QuestionService questionService, CorrectAnswerService correctAnswerService,
                WrongAnswerService wrongAnswerService) {
        this.topicService = topicService;
        this.questionService = questionService;
        this.correctAnswerService = correctAnswerService;
        this.wrongAnswerService = wrongAnswerService;
    }

    public void seedData() throws IOException {
        topicService.seedTopics();
        questionService.seedQuestions();
        wrongAnswerService.seedWrongAnswers();
        correctAnswerService.seedCorrectAnswers();
    }

}
