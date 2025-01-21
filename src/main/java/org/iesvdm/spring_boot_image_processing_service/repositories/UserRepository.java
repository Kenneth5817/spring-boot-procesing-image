package org.iesvdm.spring_boot_image_processing_service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.iesvdm.spring_boot_image_processing_service.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
