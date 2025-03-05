package ir.maktabsharif.webapplication.entity;

import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.entity.question.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Answer extends BaseEntity<Long> {

    private String answerText;
    private boolean correct;

    @ManyToOne
    private AppUser student;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private ExamQuestion question;
}
