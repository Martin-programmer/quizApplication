package course.springdata.quizapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "correct_answers")
@Getter @Setter
@NoArgsConstructor
public class CorrectAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String correctAnswer;

    @OneToOne(mappedBy = "correctAnswer",cascade = CascadeType.ALL,orphanRemoval = true)
    private Question question;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorrectAnswer that = (CorrectAnswer) o;
        return Objects.equals(id, that.id) && Objects.equals(correctAnswer, that.correctAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, correctAnswer);
    }
}
