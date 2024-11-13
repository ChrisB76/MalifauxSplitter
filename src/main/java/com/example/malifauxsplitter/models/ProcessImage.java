package com.example.malifauxsplitter.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessImage {
    public String frontImagePath;
    public String backImagePath;
    private String frontPath;
    private String backPath;
    private String name;
}
