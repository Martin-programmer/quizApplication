package course.springdata.quizapplication.repositories;

import course.springdata.quizapplication.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    boolean existsAdminByEmail(String email);
    Admin findByEmail(String email);
}
