package course.springdata.quizapplication.repositories;

import course.springdata.quizapplication.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question,Long> {
    Question findQuestionById(Long id);

    List<Question> findAll();

    Set<Question> findQuestionsByTopic_TopicName(String topicName);
}
