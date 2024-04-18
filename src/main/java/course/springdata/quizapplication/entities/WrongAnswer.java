package course.springdata.quizapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "wrong_answers")
@Getter @Setter
@NoArgsConstructor
public class WrongAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String wrongAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WrongAnswer that = (WrongAnswer) o;
        return Objects.equals(id, that.id) && Objects.equals(wrongAnswer, that.wrongAnswer) && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, wrongAnswer, question);
    }
}
