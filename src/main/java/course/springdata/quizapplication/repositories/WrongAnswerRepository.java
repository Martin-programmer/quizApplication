package course.springdata.quizapplication.repositories;

import course.springdata.quizapplication.entities.WrongAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WrongAnswerRepository extends JpaRepository<WrongAnswer, Long> {
}
