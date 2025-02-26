package ir.maktabsharif.webapplication.entity.question;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
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

}
