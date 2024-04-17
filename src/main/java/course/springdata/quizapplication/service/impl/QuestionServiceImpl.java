package course.springdata.quizapplication.service.impl;

import course.springdata.quizapplication.entities.Question;
import course.springdata.quizapplication.entities.Topic;
import course.springdata.quizapplication.repositories.QuestionRepository;
import course.springdata.quizapplication.service.QuestionService;
import course.springdata.quizapplication.service.TopicService;
import course.springdata.quizapplication.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private TopicService topicService;
    @Override
    public void seedQuestions() throws IOException {
        if (questionRepository.count() != 0){
            return;
        }
        String[] fileContent = fileUtil.readFileContent("src/main/resources/files/questions.txt");

        Arrays.stream(fileContent)
                .forEach(row -> {
                    String[] tokens = row.split("/");
                    String questionToAdd = tokens[0];
                    long topicId = Long.parseLong(tokens[1]);
                    Question question = new Question();
                    question.setQuestion(questionToAdd);
                    Topic topic = topicService.getTopicById(topicId);
                    question.setTopic(topic);
//                    topic.addQuestion(question);
                    questionRepository.saveAndFlush(question);
                });
    }
}
