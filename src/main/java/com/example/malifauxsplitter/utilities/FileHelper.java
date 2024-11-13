package com.example.malifauxsplitter.utilities;

import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileHelper {
    public Set<String> listFilesUsingJavaIO(String filepath) {
        String message = String.format("lit files in dir: %s", filepath);
        System.out.println(message);
        return Stream.of(new File(filepath).listFiles())
                .filter(file -> !file.isDirectory())
                .filter(file -> !file.getName().equals(".DS_Store"))
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
