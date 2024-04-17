package course.springdata.quizapplication.service;

import course.springdata.quizapplication.entities.Topic;

import java.io.IOException;

public interface TopicService {
    void seedTopics() throws IOException;

    Topic getTopicById(Long id);
}
