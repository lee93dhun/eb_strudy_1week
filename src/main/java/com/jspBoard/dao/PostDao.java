package com.jspBoard.dao;

import com.jspBoard.entity.PostEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface PostDao {

    ArrayList<PostEntity> getAllPostOrderByRecent();
    String categoryById(int id);
}
