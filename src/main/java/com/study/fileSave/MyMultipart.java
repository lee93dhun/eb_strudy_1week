package com.study.fileSave;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyMultipart {



    BufferedReader br;

    List<Map<String, String>> fileInfoList = new ArrayList<>();
    File file;

    /**
     * 파일을 읽어와 boundary 로 구분하여 Map 타입에 담아주는 기능
     * @param mulitpartFile 읽어올 파일 경로를 가지고 있는 File 객체
     *
     */
    // 파일을 읽고 바운더리를 기준 파일단위로 구분하여 map형태로 반환
    public void readFile(File mulitpartFile) {
        String boundary = "";
        try {
            FileReader fr = new FileReader(mulitpartFile);
            br = new BufferedReader(fr);

            boolean capture = false;    // 추출할 문자열을 구분하기 위한 스위치
            String fileData = "";

            while(true){
                String line = br.readLine();

                if(line.contains("boundary")){      // boundary 추출
                    boundary = line.substring(line.indexOf("=")+1);
                }else if(line.equals("--"+boundary+"--")){        // 마지막 boundary, 추출 종료
                    break;
                }
                if(line.contains("--"+boundary)){
                    capture = true;
                }
                if(capture){
                    fileData += line+"\n";
                }

            }
            // 첫번째 boundary 제거 , 불필요한 문자 제거 및 변환
            fileData = fileData
                    .replaceFirst("--"+boundary,"")
                    .replaceFirst("\n", "")
                    .replaceAll(";","\n")
                    .replaceAll("=",": ");
            // boundary 를 기준, 파일 내용별로 배열에 담기
            String[] fileDataArr = fileData.split("--"+boundary);
            transDataParts(fileDataArr);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 파일의 내용을 Map 타입 객체에 담아 List 로 관리하게 함.
     * @param fileDataArr 파일 정보 별로 구분해 놓은 배열
     * @throws IOException 파일의 내용을 읽어오는 중에 발생할 수 있는 예외
     */
    // 데이터 key value 형태로 변화하여 리스트에 담기
    public void transDataParts(String[] fileDataArr) throws IOException {
        Map<String, String> map ;

        for(String fileData : fileDataArr){
            br = new BufferedReader(new StringReader(fileData.trim()));
            map = new HashMap<>();
            String fileContent = "";    // 각 파일 데이터의 내용을 담을 변수
            // 한 줄씩 읽어와 map에 담기
            while(true){
                String key;
                String value;
                boolean capture = false;    // 추출할 파일내용임을 구분하기 위한 스위치

                String line = br.readLine();
                if(line == null) {
                    break;
                }

                // ':' 로 헤더임을 구분하여 map 에 담기
                if(line.contains(":")){
                    String[] pair = line.split(": ");
                    key = pair[0].trim();
                    value = pair[1].trim();
                    map.put(key, value);
                }else{
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

    }

    // Todo 예외처리시 catch 처리 ?

    /**
     * fileName 값을 value 로 가지고 있는 Map 객체 반환하기
     *
     * @param fileName 가져올 name 속성의 값
     * @return 'fileName' 을 통해 찾은 Map 객체 한 개만을 반환
     * @throws 'Map' 타입 'fileInfo' 의 value == fileName 가 2개 이상 경우
     *          fileInfo 의 value 에 fileName 값이 없는경우
     */
    public Map<String, String> getMultipartFile(String fileName){

        List<Map<String, String>> findFileList = new ArrayList<>();

            for(Map<String,String> fileInfo : fileInfoList){
                // value 값이 있다면 List 에 추가
                if(fileInfo.containsValue(fileName)) {
                    findFileList.add(fileInfo);
                }else{
                    try {
                        throw new Exception("Not found file :::: fileName = "+fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                if(findFileList.size() > 1) {
                    System.out.println("find file :: " + findFileList.size());
                    throw new RuntimeException("Too many file found ::::: " + findFileList.size());
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        return findFileList.get(0);
    }

    /**
     * 파일을 로컬에 저장하는 기능
     *
     * @param fileInfo 저장할 파일의 정보를 담고 있는 객체
     * @param path 파일을 저장할 경로
     */
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

    /**
     * 저장할 경로에 디렉토리 존재 여부 체크 후 생성
     *
     * @param path 파일을 저장할 경로
     */
    public void dirCheck(String path){

        file = new File(path);
        File dir = file.getParentFile();

        if(dir != null && !dir.exists()){
            if(dir.mkdirs()){
                System.out.println("디렉토리 생성 :: "+ dir);
            }else{
                System.out.println("디렉토리 생성 실패");
            }
        }
    }

    /**
     * 경로에 파일명 중복이 있는지 체크 후 파일명 변경후 저장
     *
     * @param path 저장할 파일의 경로
     * @return 변경한 파일명이 포함된 경로
     */
    // 파일명이 중복 체크 및 처리
    public String duplicateFile(String path){
        String baseName = file.getName();   // 기존의 파일명
        String dirPath = file.getParent();
        boolean isDuplicateFile = file.exists();    // 파일이 존재? true : false

        // 중복된 파일명이 있으면 변경
        if(isDuplicateFile) {
            System.out.println("파일이 존재합니다. :: " + path);
            int cnt = 1;
            int dotIdx = baseName.lastIndexOf(".");
            String newFileName = "";

            // 파일명과 확장자를 분리해 파일명 수정후 결합
            while(file.exists()){
                String name = baseName.substring(0,dotIdx);
                String extension = baseName.substring(dotIdx);

                newFileName = name+"("+cnt+")"+extension;
                file = new File(dirPath, newFileName);
                cnt++;
            }
        }else{ // 중복된 파일 없으면 경로 유지
            System.out.println("중복된 파일명이 없습니다.");
            return path;
        }
        return file.getPath();
    }

}
