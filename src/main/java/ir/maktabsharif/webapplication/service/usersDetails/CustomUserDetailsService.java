package ir.maktabsharif.webapplication.service.usersDetails;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // فرض کنید شناسه کاربر در UserDetails ذخیره شده است
            return ((CustomUserDetails) userDetails).getId();
        }
        throw new ResourceNotFoundException("User not authenticated");
    }

}
