package course.springdata.quizapplication.service.impl;

import course.springdata.quizapplication.entities.Topic;
import course.springdata.quizapplication.repositories.TopicRepository;
import course.springdata.quizapplication.service.TopicService;
import course.springdata.quizapplication.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private FileUtil fileUtil;
    @Override
    public void seedTopics() throws IOException {
        if (topicRepository.count() != 0){
            return;
        }
        String[] fileContent = fileUtil.readFileContent("src/main/resources/files/topics.txt");
        Arrays.stream(fileContent)
                .forEach(topicName -> {
                    Topic topic = new Topic();
                    topic.setTopicName(topicName);
                    topicRepository.saveAndFlush(topic);
                });
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicRepository.getTopicById(id);
    }
}
