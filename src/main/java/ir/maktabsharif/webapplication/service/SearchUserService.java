package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import ir.maktabsharif.webapplication.repository.specification.UserSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchUserService {
    private final UsersRepository usersRepository;
    public SearchUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<AppUser> searchUsers(String role, String firstName, String lastName) {
        Specification<AppUser> spec = Specification
                .where(UserSpecifications.hasRole(role))
                .and(UserSpecifications.hasFirstName(firstName))
                .and(UserSpecifications.hasLastName(lastName));
        return usersRepository.findAll(spec);
    }
}
