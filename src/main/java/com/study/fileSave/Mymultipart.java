package com.study.fileSave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Mymultipart {

    public void readFile(File file) {
        String boundary = "";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            List<String> contents = new ArrayList<String>();

            while(true){
                String line = br.readLine();
                String content = "";
                boolean capture = false;
                // boundary 추출
                if(line.contains("boundary")){
                    boundary = line.substring(line.indexOf("=")+1);
                    System.out.println(boundary);
                }
                // boundary 단위로 추출
                if(line.contains("--"+boundary)){
                    System.out.println(line);
                    capture = true;
                }
                if(capture){
                    content += line;
                    if(line.contains("--"+boundary)){
                        capture = false;
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fileData(){

    }
}
