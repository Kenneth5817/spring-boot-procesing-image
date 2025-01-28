package org.iesvdm.spring_boot_image_processing_service;

import org.iesvdm.spring_boot_image_processing_service.repositories.ImageRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SpringBootImageProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootImageProcessingServiceApplication.class, args);
	}

	@Autowired
	ImageRepository imageRepository;

	@Test
    public void contextLoads(){
		System.out.println(imageRepository.getNextvalTransformReqSec());
	}
}
