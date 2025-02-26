package ir.maktabsharif.webapplication.entity.question;

import ir.maktabsharif.webapplication.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class Option extends BaseEntity<Long> {

    @Column(nullable = false)
    private String optionText;
    @ManyToOne
    private Question question;

    @Column(nullable = false)
    private boolean isCorrect;


}
