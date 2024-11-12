package com.example.malifauxsplitter.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.runtime.ObjectMethods;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class PdfSplit {
    @GetMapping("/split/{resourceName}")
    public ResponseEntity split(@PathVariable String resourceName) throws URISyntaxException, IOException {
        String[] resourceParts = resourceName.split("\\.");
        try {
            File file = ResourceUtils.getFile("classpath:" + resourceName);
            String parent = file.getParent();
            System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
            PDDocument document = PDDocument.load(file);
            Splitter splitter = new Splitter();
            List<PDDocument> pages = splitter.split(document);
            Iterator<PDDocument> iteration = pages.listIterator();

            int i = 1;
            while(iteration.hasNext()) {
                PDDocument pd = iteration.next();
                pd.save(parent + "/" + resourceParts[0] + i++ + "." + resourceParts[1]);
            }
            document.close();
        } catch (IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Successfully splitted!!");
        
    }

    @GetMapping("/split/unprocessed")
    public ResponseEntity process() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        String unprocessedPath = PdfSplit.class.getResource("/unprocessed/").getPath();
        String processedPath = PdfSplit.class.getResource("/processed/").getPath();

        Set<String> unprocessedFiles = listFilesUsingJavaIO(unprocessedPath);

        List<String> processedFiles = new ArrayList<>();

        for(String fp: unprocessedFiles) {
            String message = String.format("Filepath: %s%s", unprocessedPath, fp);
            System.out.println(message);

            Set<File> output = Stream.of(new File(unprocessedPath).listFiles())
                    .filter(file-> !file.isDirectory())
                    .filter(file->file.getName().equals(fp))
                    .collect(Collectors.toSet());

            File file = output.iterator().next();

            PDDocument document = PDDocument.load(file);
            Splitter splitter = new Splitter();
            List<PDDocument> pages = splitter.split(document);
            Iterator<PDDocument> iteration = pages.listIterator();

            int i = 1;
            while(iteration.hasNext()) {
                PDDocument pd = iteration.next();
                pd.save(processedPath + "/" + fp.split("\\.")[0] + i++ + "." + fp.split("\\.")[fp.split("\\.").length -1]);
            }
            document.close();
        }

        return ResponseEntity.status(HttpStatus.OK).body("Processed and splitted all files");
    }

    private Set<String> listFilesUsingJavaIO(String unprocessedPath) {
        String message = String.format("lit files in dir: %s", unprocessedPath);
        System.out.println(message);
        return Stream.of(new File(unprocessedPath).listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> !file.getName().equals(".DS_Store"))
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
