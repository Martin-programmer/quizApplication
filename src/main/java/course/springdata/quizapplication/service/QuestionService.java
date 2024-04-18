package course.springdata.quizapplication.service;

import course.springdata.quizapplication.entities.Question;

import java.io.IOException;
import java.util.List;

public interface QuestionService {

    void seedQuestions() throws IOException;

    Question getQuestionById(Long id);

    List<Question> getAllQuestions();
}
