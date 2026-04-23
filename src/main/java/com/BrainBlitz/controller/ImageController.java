package com.BrainBlitz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.BrainBlitz.service.impl.ImageUploadService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageUploadService uploadService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return uploadService.uploadImage(file);
    }
}
