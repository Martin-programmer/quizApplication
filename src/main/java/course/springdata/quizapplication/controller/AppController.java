package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.controller.authentication.Authentication;
import course.springdata.quizapplication.entities.Admin;
import course.springdata.quizapplication.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class AppController implements CommandLineRunner {
    private final InteractionService interactionService;
    private final Authentication authentication;
    private final AdminPanel adminPanel;
    private final UserPanel userPanel;

    @Autowired
    public AppController(InteractionService interactionService, Authentication authentication, AdminPanel adminPanel,
                         UserPanel userPanel) {
        this.interactionService = interactionService;
        this.authentication = authentication;
        this.adminPanel = adminPanel;
        this.userPanel = userPanel;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        interactionService.seedData();
        Role role = authentication.authenticateUser();
        if (role == Role.USER) {
            userPanel.handleGameplay(authentication.getEmail());
        }else{
            adminPanel.handleAdminPanel();
        }
    }
}