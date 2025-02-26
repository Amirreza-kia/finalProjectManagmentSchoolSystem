package ir.maktabsharif.webapplication.entity.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity <ID extends Serializable> implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Column(name = "create_date_entity")
    private final LocalDateTime createDate = LocalDateTime.now();

}
