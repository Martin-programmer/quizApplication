package course.springdata.quizapplication.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
@Getter @Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "correct_answer_id")
    private CorrectAnswer correctAnswer;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<WrongAnswer> wrongAnswers = new HashSet<>(); // Initialize the set here

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question that = (Question) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addWrongAnswer(WrongAnswer wa) {
        wrongAnswers.add(wa);
        wa.setQuestion(this);
    }
}
