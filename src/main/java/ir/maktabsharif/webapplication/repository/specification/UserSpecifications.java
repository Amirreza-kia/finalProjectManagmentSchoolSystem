package ir.maktabsharif.webapplication.repository.specification;

import ir.maktabsharif.webapplication.entity.AppUser;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<AppUser> hasRole(String role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    public static Specification<AppUser> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
        };
    }

    public static Specification<AppUser> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
        };
    }
}