package ir.maktabsharif.webapplication.entity;


import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import ir.maktabsharif.webapplication.entity.question.Question;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne
    private AppUser teacher;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;



    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Question> questions = new ArrayList<>();

//    @ElementCollection
//    @CollectionTable(name = "exam_questions", joinColumns = @JoinColumn(name = "exam_id"))
//    @Column(name = "question")
//    private List<String> questions = new ArrayList<>();


}
