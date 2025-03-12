package ir.maktabsharif.webapplication.entity.answer;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import jakarta.persistence.*;
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

    double score;

    @ManyToOne
    private AppUser student;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private ExamQuestion question;

    @ManyToOne
    @JoinColumn(name = "student_exam_id")
    private StudentExam studentExam;

    private String answerText;

    private String selectedOption;
}
