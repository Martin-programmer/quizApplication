package course.springdata.quizapplication.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "topics")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "topic_name")
    private String topicName;
    @OneToMany(mappedBy = "topic",targetEntity = Question.class)
    private Set<Question> questions;

    public void addQuestion(Question question){
        this.questions.add(question);
        question.setTopic(this);
    }
}
