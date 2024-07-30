package com.study.paramParsing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRequest {

    Map<String, List<String> >map = new HashMap<>();

    public void parse(String queryString){
        System.out.println("queryString value check :: "+queryString);
        int idx = 0;

        // queryString 에서 '?' 인덱스 추출
        idx = queryString.indexOf('?');

        // idx를 활용해 도메인 부분 제거
        String strResult = queryString.substring(idx+1);
        System.out.println("? idx = "+idx +" / "+"strResult = "+strResult);

        // '&'를 기준으로 매개변수 분리
        String[] queries = strResult.split("&");
        System.out.println("queries.length = "+queries.length);



        for(String query : queries) {
            // key 와 value 분리
            String[] keyValue = query.split("=");
            String key = keyValue[0];
            String value = keyValue[1];

            // list 에 key 에 해당 하는 값들을 가져옴
            List<String> list = map.get(key);

            // map에 key가 없으면 list 생성 key, value put
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            // map에 같은 key가 있으면 해당 key가 가리키는 list에 value 추가
            list.add(value);
        }
    }

    public String getParam(String key){
        System.out.println("key = "+key);
        List<String> values = map.get(key);
        // values 의 값이 복수 일때 예외 발생
        if (values.size() > 1) {
            throw new IllegalArgumentException("Multiple values for parameter: " + key);
        }
        return values.get(0);
    }

    public List<String> getParams(String key){
        System.out.println("key = "+key);

        return map.get(key);
    }




}


