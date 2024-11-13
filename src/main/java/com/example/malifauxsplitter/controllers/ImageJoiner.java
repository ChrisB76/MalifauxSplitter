package com.example.malifauxsplitter.controllers;

import com.example.malifauxsplitter.models.ProcessImage;
import com.example.malifauxsplitter.utilities.FileHelper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class ImageJoiner {

    @Autowired
    private FileHelper fileHelper;
    private String BLANK_TEMPLATE = "malifauxjoiner-blank.jpg";

    String processedPath = ImageJoiner.class.getResource("/processed/").getPath();

    @GetMapping("/join-processed")
    public ResponseEntity joinProcessed() throws URISyntaxException, IOException {

        // read in the files from processed
        // stitch together 1&2 into a consolidated image
        // save to new folder stitched
        Set<String> processedFiles = fileHelper.listFilesUsingJavaIO(processedPath);

        List<ProcessImage> processedImages = new ArrayList<>();
        for(String fp: processedFiles) {

            System.out.println("fp = " + fp);
            String fpName = fp.substring(0, fp.length() - 5);

            var existingImages = processedImages.stream().map(im -> im.getName()).toList();
            if (existingImages.contains(fpName)) {
                // we have found 1 part already, add the last
                var foundImages = processedImages.stream().filter(im -> im.getName().equals(fpName)).toList();
                foundImages.get(0).setBackPath(fp);
            } else {
                ProcessImage image = ProcessImage.builder().name(fpName).frontPath(fp).build();
                processedImages.add(image);
            }
        }

        System.out.println("processedImages = " + processedImages);

        stitchImages(processedImages);

        return ResponseEntity.status(HttpStatus.OK).body("Images have been joined");
    }

    private void stitchImages(List<ProcessImage> processedImages) throws IOException, URISyntaxException {
        //we have a list of models

        for(var pi: processedImages) {
            var blankResource = getClass().getClassLoader().getResource(BLANK_TEMPLATE);
            var resourcePath = blankResource.getPath().split("/" + BLANK_TEMPLATE)[0];
            System.out.println("blankResource = " + blankResource);
            var stitchedName = "stitched/" + pi.getName() + ".jpg";
            var destinationPath = blankResource.getPath().split("/" + BLANK_TEMPLATE)[0] + "/" + stitchedName;

            pi.setFrontImagePath(convertPdfToJpg(resourcePath, resourcePath + "/processed/" + pi.getFrontPath()));
            pi.setBackImagePath(convertPdfToJpg(resourcePath, resourcePath + "/processed/" + pi.getBackPath()));

            BufferedImage image = ImageIO.read(new File(blankResource.toURI()));
            System.out.println("Original Image Dimension: " + image.getWidth() + "x" + image.getHeight());

            BufferedImage frontImage = ImageIO.read(new File(pi.getFrontImagePath()));
            System.out.println("frontImage = " + frontImage.getWidth());
            System.out.println("frontImage.getHeight() = " + frontImage.getHeight());
            BufferedImage backImage = ImageIO.read(new File(pi.getBackImagePath()));
            System.out.println("backImage = " + backImage.getWidth());
            System.out.println("backImage.getHeight() = " + backImage.getHeight());

            BufferedImage joined = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
            Graphics2D graph = joined.createGraphics();
            graph.drawImage(frontImage, 0, 0, null);
            graph.drawImage(backImage, frontImage.getWidth(), 0, null);

            File destFile = new File(destinationPath);
            ImageIO.write(joined, "png", destFile);

        }
    }

    private String convertPdfToJpg(String resourcePath, String sourcePath) {
        try {
            String destinationDir = resourcePath + "/converted/";

            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationDir);
            File outPutFile = null;
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                System.out.println("Folder Created -> "+ destinationFile.getAbsolutePath());
            }
            if (sourceFile.exists()) {
                System.out.println("Images copied to Folder Location: "+ destinationFile.getAbsolutePath());
                PDDocument document = PDDocument.load(sourceFile);
                PDFRenderer pdfRenderer = new PDFRenderer(document);

                int numberOfPages = document.getNumberOfPages();
                System.out.println("Total files to be converting -> "+ numberOfPages);

                String fileName = sourceFile.getName().replace(".pdf", "");
                String fileExtension= "png";
                /*
                 * 600 dpi give good image clarity but size of each image is 2x times of 300 dpi.
                 * Ex:  1. For 300dpi 04-Request-Headers_2.png expected size is 797 KB
                 *      2. For 600dpi 04-Request-Headers_2.png expected size is 2.42 MB
                 */
                int dpi = 300;// use less dpi for to save more space in harddisk. For professional usage you can use more than 300dpi

                for (int i = 0; i < numberOfPages; ++i) {
                    outPutFile = new File(destinationDir + fileName +"."+ fileExtension);
                    BufferedImage bImage = pdfRenderer.renderImageWithDPI(i, dpi, ImageType.RGB);
                    ImageIO.write(bImage, fileExtension, outPutFile);
                }

                document.close();
                System.out.println("Converted Images are saved at -> "+ destinationFile.getAbsolutePath());
                return outPutFile.getPath();
            } else {
                System.err.println(sourceFile.getName() +" File not exists");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
