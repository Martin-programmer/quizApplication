package course.springdata.quizapplication.service.impl;

import course.springdata.quizapplication.entities.CorrectAnswer;
import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.repositories.CorrectAnswerRepository;
import course.springdata.quizapplication.service.CorrectAnswerService;
import course.springdata.quizapplication.service.QuestionService;
import course.springdata.quizapplication.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

@Service
@Transactional
public class CorrectAnswerServiceImpl implements CorrectAnswerService {

    @Autowired
    private CorrectAnswerRepository correctAnswerRepository;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private QuestionService questionService;

    @Override
    public void seedCorrectAnswers() throws IOException {
        if (correctAnswerRepository.count() != 0){
            return;
        }
        String[] fileContent = fileUtil.readFileContent("src/main/resources/files/correct_answers.txt");
        Arrays.stream(fileContent).forEach(row -> {
            String[] tokens = row.split("/");
            String correctAnswerToAdd = tokens[0];
            long questionId = Long.parseLong(tokens[1]);
            Question questionById = questionService.getQuestionById(questionId);
            if (questionById != null){
                CorrectAnswer correctAnswer = new CorrectAnswer();
                correctAnswer.setCorrectAnswer(correctAnswerToAdd);
                questionById.setCorrectAnswer(correctAnswer);
                correctAnswer.setQuestion(questionById);
                correctAnswerRepository.save(correctAnswer);
            }
        });
    }

    @Override
    public void addNewCorrectAnswer(Question question, CorrectAnswer correctAnswer) {
        question.setCorrectAnswer(correctAnswer);
        correctAnswer.setQuestion(question);
        correctAnswerRepository.saveAndFlush(correctAnswer);
    }
}
