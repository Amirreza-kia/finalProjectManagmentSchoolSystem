package ir.maktabsharif.webapplication.entity.question;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.Course;
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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
public  class Question extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String questionText;

    private Double defaultScore;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeQuestion typeQuestion;

    @ManyToOne
    @JoinColumn(name = "teachr_id")
    private AppUser teacher;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    //multiple question
    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    private List<String> options;
    private String correctAnswer;


    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamQuestion> examQuestions;

}
