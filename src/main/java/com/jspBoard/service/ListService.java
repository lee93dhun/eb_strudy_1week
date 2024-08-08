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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListService implements HttpService {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private SqlSessionFactory sqlSessionFactory;

    public ListService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public String doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        PostDao postDao = sqlSession.getMapper(PostDao.class);

        String page = "/WEB-INF/views/list.jsp";
        int boardPerPage = 5;  //  한 페이지에 볼 게시물 수
        int allPostCnt = postDao.getAllPostCnt();
        int maxPage = getMaxPage(allPostCnt, boardPerPage);
        int currentPageIdx = getPageNum(req, maxPage);
        int offset = (currentPageIdx - 1) * boardPerPage;

        Map<String, Integer> pageParam = new HashMap<>();
        pageParam.put("offset", offset);
        pageParam.put("boardPerPage", boardPerPage);

        ArrayList<Map<String, Object>> allCategories = getAllCategories(postDao);
        ArrayList<PostEntity> boardList = getBoardList(req, postDao, pageParam);

        req.setAttribute("allCategories", allCategories);
        req.setAttribute("allPostCnt", allPostCnt);
        req.setAttribute("boardList", boardList);
        req.setAttribute("maxPage", maxPage);
        req.setAttribute("boardPerPage", boardPerPage);
        req.setAttribute("currentPageIdx", currentPageIdx);

        logger.info(boardList.size());
        return page;
    }

    public ArrayList<Map<String, Object>> getAllCategories(PostDao postDao) {
        ArrayList<Map<String, Object>> allCategories = postDao.getAllCategories();
        return allCategories;
    }

    // 게시물 가져오기  -> 기본 || 검색조건
    public ArrayList<PostEntity> getBoardList(
            HttpServletRequest req, PostDao postDao, Map<String, Integer> pageParam) {
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String keyword = req.getParameter("keyword");
        String categoryParam = req.getParameter("category");
        Integer categoryId;

        if (categoryParam != null) {
            categoryId = Integer.parseInt(categoryParam);
        } else {
            categoryId = null;
        }

        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("startDate", startDate);
        searchParam.put("endDate", endDate);
        searchParam.put("categoryId", categoryId);
        searchParam.put("keyword", keyword);

//        boolean allParamsNull = searchParam.values().stream().allMatch(value -> value == null);
//        ArrayList<PostEntity> getPostList;

      /*  if (allParamsNull) {
            logger.info("모든게시물 가져오기 수행"); 
            getPostList = postDao.getPostOrderByRecent(pageParma);
        } else {
            logger.info("조건에 맞는 게시물 가져오기 수행");
            // pageParam , searchParam 묶어주기
//            Map<String,Object> searchPageParam = new HashMap<>(searchParam);
            searchParam.putAll(pageParma);
            // 검색조건과 페이징으로 게시물 가져오기
            getPostList = postDao.getBoardListByParams(searchParam);
        }*/
        searchParam.putAll(pageParam);
        ArrayList<PostEntity> boardList = postDao.getBoardListByParams(searchParam);
        for (PostEntity post : boardList) {
            String categoryName = postDao.categoryById(post.getCategoryId());
            post.setCategoryName(categoryName);
        }
        return boardList;
    }

    public int getMaxPage(int allPostCnt, int viewPostSize) {
        int maxPage = 0;

        maxPage = allPostCnt / viewPostSize;
        if (allPostCnt % viewPostSize != 0) {
            maxPage++;
        }
        logger.info("maxPage = {}", maxPage);
        return maxPage;
    }

    // Uri 에서 페이지 숫자 추출하기
    public int getPageNum(HttpServletRequest req, int maxPage) {
        int currentPageIdx = 1;
        try {   // queryString Null
            //TODO queryString 검토하기 (임시 수정)
            String queryString = "page=" + req.getParameter("page");
            logger.info("queryString = {}", queryString);
            currentPageIdx = Integer.parseInt(queryString.split("=")[1]);
            if (currentPageIdx < 1 || currentPageIdx > maxPage) {
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
            if (currentPageIdx < 1) {
                currentPageIdx = 1;
            } else if (currentPageIdx > maxPage) {
                currentPageIdx = maxPage;
            }
        }
        logger.info("currentPageIdx={}", currentPageIdx);
        return currentPageIdx;
    }


}
