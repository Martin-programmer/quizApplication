package course.springdata.quizapplication.controller;

import course.springdata.quizapplication.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class AppController implements CommandLineRunner {
    private final InteractionService interactionService;

    @Autowired
    public AppController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        interactionService.seedData();
        Role role = interactionService.promptForRole();
        interactionService.processLoginOrRegister(role);
        if (role == Role.USER) {
            interactionService.handleGameplay();
        }else{
            interactionService.handleAdminPanel();
        }
    }
}