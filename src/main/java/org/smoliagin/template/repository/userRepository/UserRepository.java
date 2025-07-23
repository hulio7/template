package org.smoliagin.template.repository.userRepository;


import org.smoliagin.template.repository.userRepository.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Specification<User> specification, Pageable pageable);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
