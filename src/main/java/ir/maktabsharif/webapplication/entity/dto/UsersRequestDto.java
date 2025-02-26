package ir.maktabsharif.webapplication.entity.dto;


import ir.maktabsharif.webapplication.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class UsersRequestDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;

}
