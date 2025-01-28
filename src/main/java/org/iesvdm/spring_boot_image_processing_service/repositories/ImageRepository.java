package org.iesvdm.spring_boot_image_processing_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.iesvdm.spring_boot_image_processing_service.entities.Image;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {

@NativeQuery("SELECT nextval('transform_req_sec')")
    Integer getNextvalTransformReqSec();
}
