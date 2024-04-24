package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.controller.authentication.Authentication;
import course.springdata.quizapplication.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class AppController implements CommandLineRunner {
    private final InteractionService interactionService;
    private final Authentication authentication;

    @Autowired
    public AppController(InteractionService interactionService, Authentication authentication) {
        this.interactionService = interactionService;
        this.authentication = authentication;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        interactionService.seedData();
        Role role = authentication.authenticateUser();
        if (role == Role.USER) {
            interactionService.handleGameplay();
        }else{
            interactionService.handleAdminPanel();
        }
    }
}