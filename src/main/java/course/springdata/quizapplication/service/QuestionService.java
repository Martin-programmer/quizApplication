package course.springdata.quizapplication.service;

import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.Topic;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface QuestionService {

    void seedQuestions() throws IOException;

    Question getQuestionById(Long id);

    List<Question> getAllQuestions();

    Set<Question> getQuestionsByTopicName(String topicName);

    Set<Question> getTenRandomQuestions();

    void addNewQuestion(Question question, Topic topic);
}
