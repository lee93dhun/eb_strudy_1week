package com.study.formDataParsing;

import java.io.*;
import java.util.*;

public class MyMultipartReq {
    Map<String, String> map = new HashMap<>();
    List<String> parts = new ArrayList<>();
    String[] reqLines = new String[3];

    public void parse(File file){
        System.out.println("file name == "+file.getName());
        String key;
        String value;

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while (true) {
                // 한줄씩 읽어오기
                String line = br.readLine();
                // line 에 불필요한 문자 제거
                if(line.contains(";")){
                    line = line.replaceAll(";","");
                }
                // 빈문자열이 나오면 종료 (header 추출)
                if (line.isEmpty()) {
                    break;
                }
                // boundary 제거
                int idx;
                if ((idx = line.indexOf("boundary")) != -1) {
                    System.out.println(idx);
                    line = line.substring(0, idx);
                }
                // ':'을 구분자로 map 에 담기
                if (line.contains(": ")) {   // 문자열에 ': '를 포함하고 있으면
                    parts = Arrays.asList(line.split(": "));
                    // parts size 체크
//                    System.out.println("parts.size() = "+parts.size());
                    key = parts.get(0).replaceAll(" ","");
                    value = parts.get(1).replaceAll(" ","");

                    map.put(key,value);
                }else{
                    // 요청라인 공백으로 구분해 배열에 저장
                    reqLines = line.split(" ");
                }
            }
            // map 의 모든 데이터 확인
//            System.out.println(map.entrySet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHeader(String key){
        return map.get(key);
    }
    public String getHttpMethod(){
        return reqLines[0];
    }
    public String getHttpUri(){
        return reqLines[1];

    }
    public String getHttpVer(){
        return reqLines[2];
    }
}
