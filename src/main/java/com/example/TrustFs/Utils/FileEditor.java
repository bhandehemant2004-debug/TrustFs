package com.example.TrustFs.Utils;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class FileEditor {
    public Path filePath=null;
    public List<String> lines;
    public FileEditor(){}
    public FileEditor(String path) throws IOException {
        this.filePath = Path.of(path);
        if (Files.exists(filePath)) {

            System.out.println("filese doesnt exist");
            this.lines = new ArrayList<>(Files.readAllLines(filePath));
        } else {
            this.lines = new ArrayList<>();
            System.out.println(System.getProperty("user.dir"));
        }
    }
    public void Addline(int lineNumber, String content) {
        lines.add(lineNumber-1, content);
    }
    public void replaceLine(int lineNumber, String newContent) {
        lines.set(lineNumber-1, newContent);
    }
    public void removeLine(int lineNumber) {
        lines.remove(lineNumber-1);
    }
    public void addfirst(String newContent){
        lines.addFirst(newContent);
    }
    public void addLast(String newContent){
        lines.addLast(newContent);
    }
    public void save() {
        try{
            Files.write(filePath, lines);
        }
        catch (Exception e){
            System.out.println("cannot store to the file"+filePath+"due to : "+e);
        }
    }
}
