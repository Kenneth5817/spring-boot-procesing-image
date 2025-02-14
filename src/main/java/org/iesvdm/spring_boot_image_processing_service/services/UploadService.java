package org.iesvdm.spring_boot_image_processing_service.services;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.iesvdm.spring_boot_image_processing_service.dtos.request.TransformationRequest;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class UploadService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String typeFile = file.getContentType().split("/")[1];
        String fileName = UUID.randomUUID().toString() + "." + typeFile;
        Path filePath = uploadPath.resolve(fileName);
        try (InputStream is = file.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(is);
            ImageIO.write(originalImage, typeFile, filePath.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public byte[] getFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        return Files.readAllBytes(filePath);
    }

    public String transformImage(String imageName, TransformationRequest transformationRequest) {
        Path filePath = Paths.get(uploadDir).resolve(imageName);
        try (InputStream is = Files.newInputStream(filePath)) {
            BufferedImage transformedImage = ImageIO.read(is);

            if (transformationRequest.getResize() != null && transformationRequest.getCrop() == null) {
                transformedImage = Scalr.resize(transformedImage, Scalr.Method.ULTRA_QUALITY,
                        Scalr.Mode.FIT_EXACT, transformationRequest.getResize().getWidth(),
                        transformationRequest.getResize().getHeight());
            }

            if (transformationRequest.getCrop() != null) {
                transformedImage = Scalr.crop(transformedImage, transformationRequest.getCrop().getX(),
                        transformationRequest.getCrop().getY(), transformationRequest.getCrop().getWidth(),
                        transformationRequest.getCrop().getHeight());
            }

            if (transformationRequest.getGrayscale() != null && transformationRequest.getGrayscale()) {
                BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
                transformedImage = op.filter(transformedImage, null);
            }

            if (transformationRequest.getRotate() != null) {
                transformedImage = Scalr.rotate(transformedImage,
                        Scalr.Rotation.valueOf("CW_" + transformationRequest.getRotate()));
            }

            String typeFile = imageName.split("\\.")[1];
            String fileName = UUID.randomUUID().toString() + "." + typeFile;
            try {
                ImageIO.write(transformedImage, typeFile, Paths.get(uploadDir).resolve(fileName).toFile());
                return fileName;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to write transformed image", e);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to transform image", e);
        }
    }
}
