package ir.maktabsharif.webapplication.annotation;

import ir.maktabsharif.webapplication.repository.UsersRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {


    private final UsersRepository userRepository;

    @Autowired
    public UniqueUsernameValidator(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return userRepository.findByUsername(username).isEmpty();
    }
}
