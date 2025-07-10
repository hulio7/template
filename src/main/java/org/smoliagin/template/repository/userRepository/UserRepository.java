package org.smoliagin.template.repository.userRepository;


import org.smoliagin.template.repository.userRepository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
