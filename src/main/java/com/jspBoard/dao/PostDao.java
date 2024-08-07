package com.jspBoard.dao;

import com.jspBoard.entity.PostEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface PostDao {

    ArrayList<PostEntity> getPostOrderByRecent(Map<String,Integer> pageParam);
    String categoryById(int id);

    int getAllPostCnt();
}
