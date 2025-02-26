package ir.maktabsharif.webapplication.entity.dto;



import ir.maktabsharif.webapplication.annotation.UniqueUsername;
import ir.maktabsharif.webapplication.entity.Role;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {


    @Size(min = 4, max = 10,message = "نام کاربری باید بین 4 تا 8 کلمه باشد")
    @UniqueUsername
    private String username;
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "رمز عبور باید حداقل هشت کاراکتر داشته باشد\n" +
                    "رمز عبور باید حداقل شامل یک رقم، یک حروف کوچک، یک حروف بزرگ و یک کاراکتر خاص باشد")
    private String password;

    private String firstName;

    private String lastName;

    private String role;



}
