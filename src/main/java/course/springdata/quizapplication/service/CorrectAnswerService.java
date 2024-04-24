package course.springdata.quizapplication.service;


import course.springdata.quizapplication.entities.CorrectAnswer;
import course.springdata.quizapplication.entities.Question;

import java.io.IOException;

public interface CorrectAnswerService {

    void seedCorrectAnswers() throws IOException;

    void addNewCorrectAnswer(Question question, CorrectAnswer correctAnswer);
}
