package com.grd.gradingbe.repository;

import com.grd.gradingbe.dto.enums.Role;
import com.grd.gradingbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>
{
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findUserByRole(Role role);

    boolean existsByUsername(String admin);
}
