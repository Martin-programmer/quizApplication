package course.springdata.quizapplication.service.impl;

import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.WrongAnswer;
import course.springdata.quizapplication.repositories.WrongAnswerRepository;
import course.springdata.quizapplication.service.QuestionService;
import course.springdata.quizapplication.service.WrongAnswerService;
import course.springdata.quizapplication.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;

@Service
@Transactional
public class WrongAnswerServiceImpl implements WrongAnswerService {
    @Autowired
    private WrongAnswerRepository wrongAnswerRepository;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private QuestionService questionService;
    @Override
    public void seedWrongAnswers() throws IOException {
        if (wrongAnswerRepository.count() != 0){
            return;
        }
        String[] fileContent = fileUtil.readFileContent("src/main/resources/files/wrong_answers.txt");
        Arrays.stream(fileContent).forEach(row -> {
            String[] tokens = row.split("/");
            String wrongAnswerToAdd = tokens[0];
            long questionId = Long.parseLong(tokens[1]);
            Question questionById = questionService.getQuestionById(questionId);
            if (questionById != null){
                WrongAnswer wrongAnswer = new WrongAnswer();
                wrongAnswer.setWrongAnswer(wrongAnswerToAdd);
                wrongAnswer.setQuestion(questionById);
                questionById.addWrongAnswer(wrongAnswer);
                wrongAnswerRepository.save(wrongAnswer);
            }
        });
    }

    @Override
    public void addNewWrongAnswer(Question question, WrongAnswer wrongAnswer) {
        wrongAnswer.setQuestion(question);
        question.addWrongAnswer(wrongAnswer);
        wrongAnswerRepository.saveAndFlush(wrongAnswer);
    }
}
