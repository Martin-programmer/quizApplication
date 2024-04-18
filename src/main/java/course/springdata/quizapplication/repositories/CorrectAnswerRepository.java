package course.springdata.quizapplication.repositories;

import course.springdata.quizapplication.entities.CorrectAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorrectAnswerRepository extends JpaRepository<CorrectAnswer,Long> {
}
