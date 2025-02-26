package ir.maktabsharif.webapplication.entity;

import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Course extends BaseEntity<Long> {

    @Column(nullable = false)
    private String title;
    @Column(nullable = false,unique = true)
    private String courseCode;
    private LocalDate startDate;
    private LocalDate endDate;



    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private AppUser teacher;

    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<AppUser> students;


    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Exam> exams;


    @PrePersist
    public void generateCourseCode() {
        this.courseCode = "CRS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
