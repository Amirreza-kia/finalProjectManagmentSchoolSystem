package ir.maktabsharif.webapplication.entity.question;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import jakarta.persistence.Entity;
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
public class StudentExamAnswer extends BaseEntity<Long> {

    private String answerText;
    private boolean answered;


    @ManyToOne
    private AppUser student;
    @ManyToOne
    private Exam exam;
    @ManyToOne
    private ExamQuestion examQuestion;

}
