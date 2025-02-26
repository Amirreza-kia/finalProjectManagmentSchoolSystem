package ir.maktabsharif.webapplication;


import ir.maktabsharif.webapplication.entity.Admin;
import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.RegistrationStatus;
import ir.maktabsharif.webapplication.entity.Role;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer  {


    private final UsersRepository usersRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (usersRepository.findByUsername("admin").isEmpty()) {
            Admin admin = Admin.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .firstName("admin")
                    .lastName("admin")
                    .registrationStatus(RegistrationStatus.APPROVED)
                    .role(Role.ADMIN)
                    .build();
            usersRepository.save(admin);
        }
        if (usersRepository.findByUsername("teacher").isEmpty()) {
            AppUser teacher = AppUser.builder()
                    .username("teacher")
                    .password(passwordEncoder.encode("teacher"))
                    .firstName("no")
                    .lastName("teacher")
                    .registrationStatus(RegistrationStatus.APPROVED)
                    .role(Role.TEACHER)
                    .build();
            usersRepository.save(teacher);
        }























//        if (usersRepository.findByUsername("ali").isEmpty()) {
//            AppUser ali = new AppUser();
//            ali.setUsername("ali");
//            ali.setPassword(passwordEncoder.encode("1234"));
//            ali.setRole(Role.TEACHER);
//            ali.setFirstName("ali");
//            ali.setLastName("najafi");
//            ali.setStatus("Pending");
//            usersRepository.save(ali);
//        }
//        if (usersRepository.findByUsername("Ebrahimi").isEmpty()) {
//            AppUser ebrahimi = new AppUser();
//            ebrahimi.setUsername("ebrahimi");
//            ebrahimi.setPassword(passwordEncoder.encode("1234"));
//            ebrahimi.setRole(Role.TEACHER);
//            ebrahimi.setFirstName("amirhossien");
//            ebrahimi.setLastName("ebrahimi");
//            ebrahimi.setStatus("Pending");
//            usersRepository.save(ebrahimi);
//        }
//        if (usersRepository.findByUsername("mehdi").isEmpty()) {
//            AppUser mehdi = new AppUser();
//            mehdi.setUsername("mehdi");
//            mehdi.setPassword(passwordEncoder.encode("1234"));
//            mehdi.setRole(Role.STUDENT);
//            mehdi.setFirstName("mehdi");
//            mehdi.setLastName("mosavi");
//            mehdi.setStatus("Pending");
//            usersRepository.save(mehdi);
//        }if (usersRepository.findByUsername("milad").isEmpty()) {
//            AppUser milad = new AppUser();
//            milad.setUsername("milad");
//            milad.setPassword(passwordEncoder.encode("1234"));
//            milad.setRole(Role.STUDENT);
//            milad.setFirstName("milad");
//            milad.setLastName("faraji");
//            milad.setStatus("Pending");
//            usersRepository.save(milad);
//        }
    }
}
