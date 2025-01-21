package org.iesvdm.spring_boot_image_processing_service.services;

import org.springframework.stereotype.Service;

import org.iesvdm.spring_boot_image_processing_service.repositories.UserRepository;
import org.iesvdm.spring_boot_image_processing_service.entities.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() -> new Exception("User not found"));
    }
}
