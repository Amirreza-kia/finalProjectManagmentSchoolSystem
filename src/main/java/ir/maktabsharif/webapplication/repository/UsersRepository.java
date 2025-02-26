package ir.maktabsharif.webapplication.repository;


import ir.maktabsharif.webapplication.entity.AppUser;
import ir.maktabsharif.webapplication.entity.RegistrationStatus;
import ir.maktabsharif.webapplication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {


    Optional<AppUser> findByUsername(String username);

    List<AppUser> findAppUsersByRegistrationStatus(RegistrationStatus registrationStatus);

    List<AppUser> findAppUserByRoleAndRegistrationStatus(Role role, RegistrationStatus registrationStatus);


    @Query("SELECT u FROM AppUser u WHERE u.firstName LIKE %:keyword%")
    List<AppUser> findByFirstNameContainingIgnoreCase(@Param("keyword") String firstName);
    @Query("SELECT u FROM AppUser u WHERE u.lastName LIKE %:keyword%")
    List<AppUser> findByLastNameContainingIgnoreCase(@Param("keyword") String lastName);
    List<AppUser> findUsersByRole(Role role);
    @Query("SELECT u FROM AppUser u WHERE u.firstName LIKE %:firstname% AND u.lastName LIKE %:lastname%")
    List<AppUser> findUsersByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(
            @Param("firstname") String firstName,@Param("lastname") String lastName);
    @Query("SELECT u FROM AppUser u where u.firstName like %:firstName and u.lastName like %:lastName and u.role =:role ")
    List<AppUser> findUsersByFirstNameContainingIgnoreCaseAndRoleAndLastNameContainingIgnoreCase(
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            @Param("role") String role);

}
