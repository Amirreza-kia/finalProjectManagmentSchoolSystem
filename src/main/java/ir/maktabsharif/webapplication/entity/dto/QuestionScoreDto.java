package ir.maktabsharif.webapplication.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class QuestionScoreDto {
    private List<Long> questionIds;
    private List<Double> scores;
}
