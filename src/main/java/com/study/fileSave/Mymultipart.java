package com.study.fileSave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Mymultipart {
    public void readFile(File file) {
        String boundary = "";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String header = "";
            String content = "";
            while(true){
                String line = br.readLine();
                boolean capture = false;
                // boundary 추출
                if(line.contains("boundary")){
                    boundary = line.substring(line.indexOf("=")+1);
                    System.out.println(boundary);
                }
                if(line.contains("--"+boundary)){
                    System.out.println(line);
                    capture = true;
                }
                if(capture){
                    header += line;
                    if (line.isEmpty()) {
                        content += line;
                    }
                    capture = false;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
