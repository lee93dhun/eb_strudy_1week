package com.jspBoard.service;


import com.jspBoard.dao.PostDao;
import com.jspBoard.entity.PostEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListService implements HttpService{

    private final Logger logger = LogManager.getLogger(this.getClass());
    private SqlSessionFactory sqlSessionFactory;

    public ListService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public String doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException{

        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostDao postDao = sqlSession.getMapper(PostDao.class);

        String page = "list.jsp";
        int postsPerPage = 5;  //  한 페이지에 볼 게시물 수
        int allPostCnt = postDao.getAllPostCnt();
        int maxPage = getMaxPage(allPostCnt, postsPerPage);
        int currentPageIdx = getPageNum(req, maxPage);
        int offset = (currentPageIdx - 1) * postsPerPage;

        Map<String, Integer> pageParam = new HashMap<>();
        pageParam.put("offset", offset);
        pageParam.put("postsPerPage", postsPerPage);

        ArrayList<PostEntity> getPostList = getPostList(postDao, pageParam);

        req.setAttribute("allPostCnt", allPostCnt);
        req.setAttribute("postList", getPostList);
        req.setAttribute("maxPage", maxPage);
        req.setAttribute("postsPerPage",postsPerPage);
        req.setAttribute("currentPageIdx", currentPageIdx);

        logger.info(getPostList.size());
        return page;
    }


    public ArrayList<PostEntity> getPostList(PostDao postDao, Map<String, Integer> pageParma){

        ArrayList<PostEntity> getPostList = postDao.getPostOrderByRecent(pageParma);

        for(PostEntity post : getPostList){
            String categoryName = postDao.categoryById(post.getCategoryId());
            post.setCategoryName(categoryName);
        }
        return getPostList;
    }

    public int getMaxPage(int allPostCnt, int viewPostSize){
        int maxPage = 0;

        maxPage = allPostCnt / viewPostSize;
        if(allPostCnt % viewPostSize != 0){
            maxPage++;
        }
        logger.info("maxPage = {}",maxPage);
        return maxPage;
    }

    // Uri 에서 페이지 숫자 추출하기
    public int getPageNum(HttpServletRequest req, int maxPage){
        int currentPageIdx = 1;
        try {   // queryString Null
            String queryString = req.getQueryString();
            currentPageIdx = Integer.parseInt(queryString.split("=")[1]);
            if(currentPageIdx < 1 || currentPageIdx > maxPage){
                throw new IllegalArgumentException();
            }
        } catch (NumberFormatException e) { // 숫자가 아닐때
            logger.error(" @@@@ Invalid page number format", e);
            currentPageIdx = 1;
        } catch (NullPointerException e) {  // null 일때
            logger.error(" @@@@ Query string is null", e);
            currentPageIdx = 1;
        } catch (IllegalArgumentException e) {  // 페이지 범위를 벗어날때
            logger.error(" @@@@ currentPageIdx is out of range", e);
            if(currentPageIdx < 1){
                currentPageIdx = 1;
            }else if(currentPageIdx > maxPage){
                currentPageIdx = maxPage;
            }
        }
        return currentPageIdx;
    }


}
