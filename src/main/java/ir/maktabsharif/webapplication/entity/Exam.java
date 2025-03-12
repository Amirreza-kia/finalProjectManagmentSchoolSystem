package ir.maktabsharif.webapplication.entity;


import ir.maktabsharif.webapplication.entity.answer.StudentExam;
import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import ir.maktabsharif.webapplication.entity.question.ExamQuestion;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @Column(nullable = false)
    private int duration;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne
    private AppUser teacher;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "exam")
    private List<ExamQuestion> questions = new ArrayList<>();


    @OneToMany(mappedBy = "exam",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<StudentExam> studentExams = new ArrayList<>();


}
