package com.study.formDataParsing;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String path = "E:\\ebrainstudy-template-week-1-main\\request-dummy.txt";
        File multipartData = new File(path);

        MyMultipartReq myMultipartReq = new MyMultipartReq();
        myMultipartReq.parse(multipartData);

        System.out.println(
            myMultipartReq.getHttpMethod()+"\n"+
            myMultipartReq.getHttpUri()+"\n"+
            myMultipartReq.getHttpVer()+"\n"+
            myMultipartReq.getHeader("Host")+"\n"+
            myMultipartReq.getHeader("User-Agent")+"\n"+
            myMultipartReq.getHeader("Content-Length")
        );
    }


}
