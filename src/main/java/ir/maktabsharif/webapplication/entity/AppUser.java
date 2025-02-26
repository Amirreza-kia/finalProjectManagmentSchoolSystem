package ir.maktabsharif.webapplication.entity;

import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import ir.maktabsharif.webapplication.entity.question.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public  class AppUser extends BaseEntity<Long> {

    @Column(unique = true)
    @NotNull
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;


    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private RegistrationStatus registrationStatus = RegistrationStatus.PENDING;


    @OneToMany(mappedBy = "teacher")
    private List<Course> teachingCourses;

    @ManyToMany(mappedBy = "students")
    private List<Course> enrolledCourses;






    //بانک سوالات استاد
    @OneToMany(mappedBy = "teacher")
    private List<Question> questions;

}
