package com.study.fileSave;

import java.io.File;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String path = "E:\\ebrainstudy-template-week-1-main\\request-dummy.txt";

        File multipartData = new File(path);
        MyMultipart myMultipart = new MyMultipart();

        myMultipart.readFile(multipartData);
        Map<String, String> fileInfo = myMultipart.getMultipartFile("text1");
        myMultipart.saveFile(fileInfo, "E:\\ebrainstudy-template-week-1-main\\temp\\test1.txt");

    }
}
