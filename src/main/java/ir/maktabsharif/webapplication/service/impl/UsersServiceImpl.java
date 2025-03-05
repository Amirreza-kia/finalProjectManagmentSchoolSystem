package ir.maktabsharif.webapplication.service.impl;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.RegistrationStatus;
import ir.maktabsharif.webapplication.entity.Role;
import ir.maktabsharif.webapplication.entity.dto.SignupRequestDto;
import ir.maktabsharif.webapplication.entity.dto.UsersRequestDto;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.repository.UsersRepository;
import ir.maktabsharif.webapplication.service.UsersService;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersServiceImpl implements UsersService {


    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser findById(Long id) throws ResourceNotFoundException {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    // ❤😊
    @Override
    @Transactional
    public void singUp(SignupRequestDto signupRequestDto) {
        try {
            if (Objects.equals(signupRequestDto.getRole(), "STUDENT")) {
                AppUser student = AppUser.builder()
                        .firstName(signupRequestDto.getFirstName())
                        .lastName(signupRequestDto.getLastName())
                        .username(signupRequestDto.getUsername())
                        .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                        .role(Role.STUDENT)
                        .registrationStatus(RegistrationStatus.PENDING)
                        .build();
                usersRepository.save(student);
            } else if (Objects.equals(signupRequestDto.getRole(), "TEACHER")) {
                AppUser teacher = AppUser.builder()
                        .firstName(signupRequestDto.getFirstName())
                        .lastName(signupRequestDto.getLastName())
                        .username(signupRequestDto.getUsername())
                        .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                        .role(Role.TEACHER)
                        .registrationStatus(RegistrationStatus.PENDING)
                        .build();
                usersRepository.save(teacher);
            }
        }catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException("User Not Found");
        }
    }

    // ❤😊
    @Override
    @Transactional
    public void approveUser(Long id) throws ResourceNotFoundException {
        AppUser appUser = findById(id);
        appUser.setRegistrationStatus(RegistrationStatus.APPROVED);
        usersRepository.save(appUser);
    }

    @Override
    public List<AppUser> findUsersByRole(Role role) throws ResourceNotFoundException {
        List<AppUser> users;
        try {
            users = usersRepository.findUsersByRole(role);
            return users;
        }catch (Exception e){
            throw new ResourceNotFoundException("User list Not Found");
        }
    }


    @Override
    public List<AppUser> searchByFirstName(String firstName) throws ResourceNotFoundException {
        return usersRepository.findByFirstNameContainingIgnoreCase(firstName);
    }

    @Override
    public List<AppUser> searchByLastName(String lastName) {
        return usersRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    @Override
    public List<AppUser> searchByRole(Role role) {
        return usersRepository.findUsersByRole(role);
    }


    @Override
    public List<AppUser> findUsersByRoleAndRegistrationStatus(Role role, RegistrationStatus registrationStatus) {
        return usersRepository.findAppUserByRoleAndRegistrationStatus(role, registrationStatus);
    }


    @Override
    @Transactional
    public void updateUser(Long id, UsersRequestDto updatedTeacher) throws ResourceNotFoundException {
        AppUser appUser = findById(id);
        appUser.setFirstName(updatedTeacher.getFirstName());
        appUser.setLastName(updatedTeacher.getLastName());
        appUser.setRole(updatedTeacher.getRole());
        usersRepository.save(appUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) throws ResourceNotFoundException {
        usersRepository.deleteById(id);
    }


    @Override
    public List<AppUser> findUserByRegistrationStatus(RegistrationStatus registrationStatus) throws ResourceNotFoundException {
        List<AppUser> users;
        try {
            users = usersRepository.findAppUsersByRegistrationStatus(registrationStatus);
            return users;
        }catch (Exception e){
            throw new ResourceNotFoundException("User Not Found");
        }
    }

    @Override
    public CustomUserDetails getCustomUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        throw new RuntimeException("User not authenticated or principal is not CustomUserDetails");
    }


}

