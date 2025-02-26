package ir.maktabsharif.webapplication.service;

import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.RegistrationStatus;
import ir.maktabsharif.webapplication.entity.Role;
import ir.maktabsharif.webapplication.entity.dto.SignupRequestDto;
import ir.maktabsharif.webapplication.entity.dto.UsersRequestDto;
import ir.maktabsharif.webapplication.exception.ResourceNotFoundException;
import ir.maktabsharif.webapplication.service.usersDetails.CustomUserDetails;

import java.util.List;


public interface UsersService {

    AppUser findById(Long id) throws ResourceNotFoundException;

    void singUp(SignupRequestDto signupRequestDto);

    void approveUser(Long id) throws ResourceNotFoundException;

    List<AppUser> findUsersByRole(Role role) throws ResourceNotFoundException;


    List<AppUser> searchByFirstName(String firstName) throws ResourceNotFoundException;

    List<AppUser> searchByLastName(String lastName);

    List<AppUser> searchByRole(Role role);


    List<AppUser> findUsersByRoleAndRegistrationStatus(Role role, RegistrationStatus registrationStatus);

    void updateUser(Long id, UsersRequestDto updatedTeacher) throws ResourceNotFoundException;

    void deleteUserById(Long id) throws ResourceNotFoundException;


    List<AppUser> findUserByRegistrationStatus(RegistrationStatus registrationStatus) throws ResourceNotFoundException;

    CustomUserDetails getCustomUserDetails();


}
