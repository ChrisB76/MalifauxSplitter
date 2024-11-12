package com.example.malifauxsplitter.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@RestController
public class ImageJoiner {

    @GetMapping("/join-processed")
    public ResponseEntity joinProcessed() {

        return ResponseEntity.status(HttpStatus.OK).body("Images have been joined");
    }
}
