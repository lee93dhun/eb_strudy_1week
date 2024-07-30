package com.study.fileSave;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String path = "E:\\ebrainstudy-template-week-1-main\\request-dummy.txt";
        File multipartData = new File(path);

        Mymultipart mymultipart = new Mymultipart();

        mymultipart.readFile(multipartData);
        // branch test
    }
}
