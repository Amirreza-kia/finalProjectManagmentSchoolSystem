package ir.maktabsharif.webapplication.entity.answer;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Exam;
import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class StudentExam extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "student_id")
    private AppUser student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @OneToMany(mappedBy = "studentExam", cascade = CascadeType.ALL)
    private List<Answer> answers;

    private double score;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
