package com.jspBoard.dao;

import com.jspBoard.dto.BoardListDto;
import com.jspBoard.dto.BoardListParamDto;
import com.jspBoard.dto.CategoryDto;
import com.jspBoard.entity.CategoryEntity;
import com.jspBoard.entity.PostEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostDao {

    List<CategoryEntity> getCategoryDtoList();

    int getPostCount(BoardListParamDto boardListParamDto);

    List<BoardListDto> getBoardListByParams(BoardListParamDto boardListParamDto);

    String categoryById(int id);
}