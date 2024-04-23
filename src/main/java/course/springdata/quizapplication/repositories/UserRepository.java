package course.springdata.quizapplication.repositories;

import course.springdata.quizapplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsUserByEmail(String email);
    User findByEmail(String email);

    @Query("SELECT u FROM User u ORDER BY u.points DESC")
    List<User> findAllUsersOrderByPoints();
}
