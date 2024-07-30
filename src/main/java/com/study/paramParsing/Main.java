package com.study.paramParsing;

public class Main {

    public static void main(String[] args) {

        String queryString = "https://www.ebrainsoft.com/?id=kmc774&favorite=001&favorite=002";

//        String queryString = "https://www.ebrainsoft.com/?id=kmc774&pw=1234&favorite=001&favorite=002&favorite=003";

        MyRequest myRequest = new MyRequest();
        myRequest.parse(queryString);

        // getParam 에 복수의 값을 가진 key 불렀을때
//        System.out.println(myRequest.getParam("favorite"));
        System.out.println(myRequest.getParam("id"));
        System.out.println(myRequest.getParams("favorite"));
        System.out.println(myRequest.getParams("favorite").size());
        System.out.println(myRequest.getParams("favorite").get(0));
        System.out.println(myRequest.getParams("favorite").get(1));


    }
}