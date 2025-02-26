package ir.maktabsharif.webapplication.entity.question;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleChoiceQuestion  extends Question {

//    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Option> options = new ArrayList<>();
//
//    public void addOption(Option option) {
//        options.add(option);
//        option.setQuestion(this);  // ارتباط با سوال
//    }

    @ElementCollection
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    private List<String> options;

    private String correctAnswer;
}
