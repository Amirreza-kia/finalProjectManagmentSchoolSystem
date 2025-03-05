package ir.maktabsharif.webapplication.entity;


import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean completed;

}
