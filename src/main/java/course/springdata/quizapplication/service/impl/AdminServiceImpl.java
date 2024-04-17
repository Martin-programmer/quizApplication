package course.springdata.quizapplication.service.impl;

import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.repositories.AdminRepository;
import course.springdata.quizapplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean registerAdmin(Admin admin) {
        if (adminRepository.existsAdminByEmail(admin.getEmail())){
            System.out.println("Admin with this email already exists. Please login or try register with other email.");
            return false;
        }
        adminRepository.saveAndFlush(admin);
        System.out.println("Admin successfully registered!");
        return true;
    }

    @Override
    public boolean loginAdmin(String email, String password) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            System.out.println("Admin not found with email: " + email);
            return false;
        }

        if (passwordEncoder.matches(password, admin.getPassword())) {
            System.out.println("Admin successfully logged in!");
            return true;
        } else {
            System.out.println("Incorrect password. Please try again.");
            return false;
        }
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findByEmail(email);
    }
}
