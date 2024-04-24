package course.springdata.quizapplication.service;

import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.WrongAnswer;

import java.io.IOException;

public interface WrongAnswerService {

    void seedWrongAnswers() throws IOException;

    void addNewWrongAnswer(Question question, WrongAnswer wrongAnswer);
}
