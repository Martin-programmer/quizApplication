package course.springdata.quizapplication.service;

import course.springdata.quizapplication.entities.Admin;

public interface AdminService {
    boolean registerAdmin(Admin admin);
    boolean loginAdmin(String email, String password);

    Admin getAdminByEmail(String email);
}
