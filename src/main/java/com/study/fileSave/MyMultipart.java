package com.study.fileSave;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMultipart {


    BufferedReader br;
    String[] fileDataArr;
    List<Map<String, String>> parts = new ArrayList<>();

    // 파일을 읽고 바운더리를 기준 파일단위로 구분하여 map형태로 반환
    public void readFile(File file) {
        String boundary = "";
        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);

            boolean capture = false;
            String fileData = "";

            while(true){
                String line = br.readLine();

                // boundary 추출
                if(line.contains("boundary")){
                    boundary = line.substring(line.indexOf("=")+1);
                }else if(line.equals("--"+boundary+"--")){
                    break;
                }
                // boundary 를 기준으로 추출 스위치 작동
                if(line.contains("--"+boundary)){
                    capture = true;
                }
                if(capture){
                    fileData += line+"\n";
                }

            }
            // 처음 boundary 제거 , 필요 없는 문자 제거
            fileData = fileData
                    .replaceFirst("--"+boundary,"")
                    .replaceFirst("\n", "")
                    .replaceAll(";","\n")
                    .replaceAll("=",": ");
            // boundary 기준으로 내용 분리
            fileDataArr = fileData.split("--"+boundary);

            // Map형태의 List에 담기
            transDataParts();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 데이터 key value 형태로 변화하여 리스트에 담기
    public void transDataParts() throws IOException {
        Map<String, String> map ;

        // 파일데이터의 개수만큼 반복
        for(String fileData : fileDataArr){
            br = new BufferedReader(new StringReader(fileData.trim()));
            map = new HashMap<>();
            String fileContent = "";
            // 한 줄씩 읽어와 map에 담기
            while(true){
                String key;
                String value;

                boolean capture = false;
                String line = br.readLine();
                if(line == null) {
                    break;
                }
//                System.out.println("start --- "+line);

                // 파일 섹션의 헤더 담기
                if(line.contains(":")){
                    String[] pair = line.split(": ");
                    key = pair[0].trim();
                    value = pair[1].trim();
                    map.put(key, value);
                }
                else{  // 파일섹션의 본문 담기
                    capture = true;
                }
                if(capture){
                    fileContent += line+"\n";
                }
            }
            map.put("fileContent", fileContent);
            // map 데이터를 List 에 담아 관리하기 위함
            parts.add(map);
        }

        // map 확인
        for(int i=0;i<parts.size();i++){
            System.out.println("======== file "+(i+1)+" ==========");
            System.out.println(parts.get(i));
            System.out.println("==========================");
        }
    }

    // 파일 name 을 매개변수로 해당하는 파일의 정보 가져오기

    // 가져온 파일 정보를 토대로 로컬에 파일 저장 하기
}
