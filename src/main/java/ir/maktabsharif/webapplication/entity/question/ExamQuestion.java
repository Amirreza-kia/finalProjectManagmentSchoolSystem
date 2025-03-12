package ir.maktabsharif.webapplication.entity.question;


import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.answer.Answer;
import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamQuestion extends BaseEntity<Long>  {

    @ManyToOne
    @JoinColumn(name = "question_id",referencedColumnName = "id",nullable = false)
    private Question question;

    private double score;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Answer> answers;



    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
