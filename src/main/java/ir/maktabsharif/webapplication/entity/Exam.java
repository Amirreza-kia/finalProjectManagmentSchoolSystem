package ir.maktabsharif.webapplication.entity;


import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import ir.maktabsharif.webapplication.entity.question.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Exam extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;

    //زمان محاسبه برای ازمون
    @Column(nullable = false)
    private int duration;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    private AppUser teacher;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "exam_examQuestion",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<ExamQuestion> questions = new ArrayList<>();


    @OneToMany(mappedBy = "exam")
    private List<StudentExam> studentExams = new ArrayList<>();


}
