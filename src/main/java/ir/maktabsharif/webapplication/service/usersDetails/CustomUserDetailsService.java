package ir.maktabsharif.webapplication.service.usersDetails;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Optional<AppUser> users = usersRepository.findByUsername(username);
            if (users.isEmpty()) {
                throw new ResourceNotFoundException("User Not Found" + username);
            }
            return new CustomUserDetails(users.get());
    }
}
