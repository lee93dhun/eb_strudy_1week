package com.study.fileSave;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMultipart {


    BufferedReader br;
    String[] fileDataArr;
    List<Map<String, String>> fileInfoList = new ArrayList<>();
    File file;

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
                }else{  // 파일섹션의 본문 담기
                    capture = true;
                }
                if(capture){
                    fileContent += line+"\n";
                }
            }
            map.put("fileContent", fileContent.trim());
            // map 데이터를 List 에 담아 관리하기 위함
            fileInfoList.add(map);
        }

        // map 확인
//        for(int i = 0; i< fileInfoList.size(); i++){
//            System.out.println("======== file "+(i+1)+" ==========");
//            System.out.println(fileInfoList.get(i));
//            System.out.println("==========================");
//        }
    }

    // Todo
    // 파일명을 못찾았을때 처리 방법
    // 예외 처리시 에러 발생


    // 파일 name 을 매개변수로 해당하는 파일의 정보 가져오기
    public Map<String, String> getMultipartFile(String fileName){

        List<Map<String, String>> findFileList = new ArrayList<>();

            for(Map<String,String> fileInfo : fileInfoList){
                // value 값이 있다면 List 에 추가
                if(fileInfo.containsValue(fileName)) {
                    findFileList.add(fileInfo);
                }else{
                    System.out.println("Not found file :::: fileName = " +fileName);
                }
            }
            // 찾은 fileInfo 가 2개 이상일때 예외 발생
        try {
            if(findFileList.size() > 1) {
                System.out.println("find file :: " + findFileList.size());
                throw new RuntimeException("Too many file found ::::: " + findFileList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 확인용
//        for(Map<String,String> findFile : findFileList){
//            System.out.println(findFile);
//        }
        return findFileList.get(0);
    }

    // 가져온 파일 정보를 바탕으로 로컬에 파일 저장 하기
    public void saveFile(Map<String, String> fileInfo, String path){

        String content = fileInfo.get("fileContent");

        dirCheck(path);
        String duplicateFilePath = duplicateFile(path);
        try {
            FileWriter writer = new FileWriter(duplicateFilePath);

            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 디렉토리 존재 여부 확인
    public void dirCheck(String path){

        file = new File(path);
        File dir = file.getParentFile();
        System.out.println("dir"+dir);

        if(dir != null && !dir.exists()){
            if(dir.mkdirs()){
                System.out.println("디렉토리 생성 :: "+ dir);
            }else{
                System.out.println("디렉토리 생성 실패");
            }
        }
    }

    // 파일명이 중복 체크 및 처리
    public String duplicateFile(String path){
        String baseName = file.getName();
        String dirPath = file.getParent();
        boolean isDuplicateFile = file.exists();

        // 중복된 파일명이 있으면 변경
        if(isDuplicateFile) {
            System.out.println("파일이 존재합니다. :: " + path);
            int cnt = 1;
            int dotIdx = baseName.lastIndexOf(".");
            String newFileName = "";

            while(file.exists()){   // 파일이 중복되지 않을때까지 반복
                String name = baseName.substring(0,dotIdx);
                String extension = baseName.substring(dotIdx);
                newFileName = name+"("+cnt+")"+extension;
                System.out.println(newFileName);
                file = new File(dirPath, newFileName);
                cnt++;
            }
        }else{ // 없으면 경로 유지
            System.out.println("중복된 파일명이 없습니다.");
            return path;
        }
        return file.getPath();
    }

}
