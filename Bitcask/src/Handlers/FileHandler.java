package Handlers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import DTO.ActiveFileInfo;

public class FileHandler {

    public FileHandler()  {}

    public List<String> listFilesWithExtension(String directoryPath, String extension) {
        List<String> filePaths = new ArrayList<>();

        // Create a File object for the directory path
        File directory = new File(directoryPath);

        // Validate if the directory exists and is indeed a directory
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return filePaths; // Return empty list of file paths
        }

        File[] fileList = directory.listFiles(); // List all files in the directory

        if (fileList != null) {
            for (File file : fileList) { // Iterate through each file
                if (file.isFile()) {
                    if (file.getName().toLowerCase().endsWith("." + extension.toLowerCase())) {
                        // Add the absolute path of the file to the list
                        filePaths.add(file.getAbsolutePath());
                    }
                }
            }
        }

        return filePaths;
    }

    public ActiveFileInfo getFilePathAndCounter(String hintFilePath){

        int extentionIndex = hintFilePath.lastIndexOf(".");
        int counter;


        Pattern pattern = Pattern.compile("activeFile(\\d+)\\.");
        Matcher matcher = pattern.matcher(hintFilePath);

        if (matcher.find()) {
            // Extract the number and increment it
            String numberStr = matcher.group(1);
            counter = Integer.parseInt(numberStr) + 1;
        } else {
            counter = 0;
        }


        String currentActiveFile = hintFilePath.substring(0, extentionIndex);

        return  new ActiveFileInfo(currentActiveFile,counter);
    }






}
