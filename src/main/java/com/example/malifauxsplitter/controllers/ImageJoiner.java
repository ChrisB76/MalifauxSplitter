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
//        BufferedImage image = ImageIO.read(new File("E:\\workspaceIndigo2\\crop\\src\\plasma.gif"));
//
//        System.out.println("Original Image Dimension: "+image.getWidth()+"x"+image.getHeight());
//
//        //Get the cropped image
//        BufferedImage firstHalf = image.getSubimage(0, 0, (image.getWidth()/2),image.getHeight());
//        BufferedImage secondHalf = image.getSubimage(image.getWidth()/2, 0, image.getWidth()/2, image.getHeight());
//
//        //Create a file to stream the out buffered image to
//        File croppedFile1 = new File("E:\\workspaceIndigo2\\crop\\src\\half1.png");
//        File croppedFile2 = new File("E:\\workspaceIndigo2\\crop\\src\\half2.png");
//
//        //Write the cropped file
//        ImageIO.write(firstHalf, "png", croppedFile1);
//        ImageIO.write(secondHalf, "png", croppedFile2);
//
//        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//
//        //join
//        BufferedImage joined = new BufferedImage(image.getWidth(),image.getHeight(), image.getType());
//        BufferedImage image1 = ImageIO.read(new File("E:\\workspaceIndigo2\\crop\\src\\half1.png"));
//        BufferedImage image2 = ImageIO.read(new File("E:\\workspaceIndigo2\\crop\\src\\half2.png"));
//
//        Graphics2D graph = joined.createGraphics();
//        graph.drawImage(image1, 0, 0,null);
//        graph.drawImage(image2, image1.getWidth(), 0,null);
//
//        File joinedFile = new File("E:\\workspaceIndigo2\\crop\\src\\joined.png");
//        ImageIO.write(joined, "png", joinedFile);
        return ResponseEntity.status(HttpStatus.OK).body("Images have been joined");
    }
}
