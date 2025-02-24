package org.example.doandemo3_2.repository;

import org.example.doandemo3_2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByVerificationToken(String verificationToken);
    Optional<User> findByEmail(String verificationToken);
    boolean existsByEmail(String email);


}