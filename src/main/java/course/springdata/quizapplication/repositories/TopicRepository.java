package course.springdata.quizapplication.repositories;

import course.springdata.quizapplication.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic,Long> {
    Topic getTopicById(Long id);

}
