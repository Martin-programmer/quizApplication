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
        if (questionRepository.count() != 0) {
            return;
        }
        String[] fileContent = fileUtil.readFileContent("src/main/resources/files/questions.txt");
        Arrays.stream(fileContent).forEach(row -> {
            String[] tokens = row.split("/");
            String questionToAdd = tokens[0];
            long topicId = Long.parseLong(tokens[1]);
            Topic topic = topicService.getTopicById(topicId);
            if (topic != null) {
                Question question = new Question();
                question.setQuestion(questionToAdd);
                topic.addQuestion(question); // This automatically sets the topic in the question
                questionRepository.save(question); // Saves the question and updates the topic due to cascade
            }
        });
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionRepository.findQuestionById(id);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public Set<Question> getQuestionsByTopicName(String topicName) {
        return questionRepository.findQuestionsByTopic_TopicName(topicName);
    }

    @Override
    public Set<Question> getTenRandomQuestions() {
        Set<Question> questions = new HashSet<>();
        int questionCount = 10;
        Random random = new Random();
        List<Topic> topics = topicService.getAllTopics();
        while (questionCount > 0){
        for (Topic topic : topics) {
                String topicName = topic.getTopicName();
                List<Question> questionsByTopicTopicName = questionRepository.findQuestionsByTopic_TopicName(topicName).stream().toList();
                int count = random.nextInt(2)+1;
                for (int i = 0; i < count; i++) {
                    int index = random.nextInt(questionsByTopicTopicName.size());
                    questions.add(questionsByTopicTopicName.get(index));
                    questionCount--;
                    if (questionCount == 0){
                        return questions;
                    }
                }
            }
        }
        return questions;
    }
}
