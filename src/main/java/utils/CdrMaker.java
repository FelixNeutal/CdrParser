package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CdrMaker {
    public static void main(String[] args) {
        String inputFileLocation = "C:\\Users\\felix\\Desktop\\Git\\CdrParser\\AcmeCdrLayout.txt";
        String outputFileLocation = "C:\\Users\\felix\\Desktop\\Git\\CdrParser\\AcmeCdrLayoutBetter.txt";
        List<String> modifiedLines = new ArrayList<>();
        String finalLine = "";
        int lineNumber = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileLocation))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] splitLine = line.split(",");
                splitLine[1] = splitLine[1].stripLeading();
                splitLine[1] = splitLine[1].stripTrailing();
                splitLine[1] = splitLine[1].replaceAll(" ", "_");
                splitLine[1] = splitLine[1].replaceAll("-", "_");
                finalLine = "private String " + splitLine[1] + ";";
                if (modifiedLines.contains(finalLine)) {
                    finalLine = "private String " + splitLine[1] + lineNumber + ";";
                }
                modifiedLines.add(finalLine);
                System.out.println(finalLine);
                lineNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
