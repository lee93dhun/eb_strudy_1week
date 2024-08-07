package com.jspBoard.service;


import com.jspBoard.dao.PostDao;
import com.jspBoard.entity.PostEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ListService implements HttpService{

    private final Logger logger = LogManager.getLogger(this.getClass());
    private SqlSessionFactory sqlSessionFactory;

    public ListService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public String doService(HttpServletRequest req, HttpServletResponse resp){
        logger.info("doService 실행 ");
        String page = "list.jsp";

        ArrayList<PostEntity> allList = getAllPostList();

        if(allList == null){
            // 게시글이 없을때
        } else {
            // 게시글이 1개라도 있을때
            req.setAttribute("allList", allList);
        }
        logger.info(allList.size());
        return page;
    }

    public ArrayList<PostEntity> getAllPostList(){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostDao postDao = sqlSession.getMapper(PostDao.class);
        ArrayList<PostEntity> getAllPostList = postDao.getAllPostOrderByRecent();
        for(PostEntity post : getAllPostList){
            String categoryName = postDao.categoryById(post.getCategoryId());
            post.setCategoryName(categoryName);
        }

        return getAllPostList;
    }
}
