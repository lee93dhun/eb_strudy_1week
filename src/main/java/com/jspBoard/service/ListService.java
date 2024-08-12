package com.jspBoard.service;


import com.jspBoard.dao.PostDao;
import com.jspBoard.dto.BoardListDto;
import com.jspBoard.dto.BoardListParamDto;
import com.jspBoard.dto.CategoryDto;
import com.jspBoard.entity.CategoryEntity;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListService implements HttpService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private SqlSessionFactory sqlSessionFactory;

    public ListService(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public String doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String view = "/WEB-INF/views/list.jsp";

        try(SqlSession sqlSession = sqlSessionFactory.openSession()) {
            PostDao postDao = sqlSession.getMapper(PostDao.class);

            List<CategoryDto> categoryDtoList = getCategoryDtoList(postDao);

            int pageSize = 5;
            String pageParam = req.getParameter("page");
            int currentPage = getCurrentPage(pageParam);
            int offset = (currentPage - 1) * pageSize;

            // TODO 검색 조건이되는 파라미터들을 uriResult() 메서드 에서도 사용하는데 Map 으로 묶어서 전달 ?
            BoardListParamDto boardListParamDto =
                    createBoardListParamDto(req, offset, pageSize);

            int postCount = postDao.getPostCount(boardListParamDto);
            int totalPage = getTotalPage(postCount, pageSize);

            List<BoardListDto> boardList = getBoardList(postDao, boardListParamDto);

            String reqUri = req.getRequestURI();
            String uriResult = buildUri(reqUri, boardListParamDto);

            // view 로 전송
            req.setAttribute("uriResult", uriResult);
            req.setAttribute("categoryList", categoryDtoList);
            req.setAttribute("searchParam", boardListParamDto);
            req.setAttribute("postCount", postCount);
            req.setAttribute("boardList", boardList);
            req.setAttribute("totalPage", totalPage);
            req.setAttribute("pageSize", pageSize);
            req.setAttribute("currentPage", currentPage);
            return view;
        }
    }

    // 전체 카테고리 가져오기
    public List<CategoryDto> getCategoryDtoList(PostDao postDao) {
        List<CategoryDto> categoryDtoList = new ArrayList<CategoryDto>();
        List<CategoryEntity> allCategory = postDao.getCategoryDtoList();

        // CategoryEntity -> CategoryDto
        for(CategoryEntity category : allCategory) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setCategoryId(category.getCategoryId());
            categoryDto.setCategoryName(category.getCategoryName());
            categoryDtoList.add(categoryDto);
        }
        return categoryDtoList;
    }

    // Todo page 값에 대한 유효성 검사 ?
    private int getCurrentPage(String pageParam) {
        int currentPage =
                pageParam != null && pageParam != "" ? Integer.parseInt(pageParam) : 1;
        return currentPage;
    }

    private BoardListParamDto createBoardListParamDto(HttpServletRequest req, int offset, int pageSize) {
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String keyword = req.getParameter("keyword");
        String categoryIdStr = req.getParameter("category");
        Integer categoryId = getCategoryId(categoryIdStr);


        BoardListParamDto boardListParamDto = new BoardListParamDto();
        boardListParamDto.setStartDate(startDate);
        boardListParamDto.setEndDate(endDate);
        boardListParamDto.setKeyword(keyword);
        boardListParamDto.setCategoryId(categoryId);
        boardListParamDto.setOffset(offset);
        boardListParamDto.setPageSize(pageSize);
        return boardListParamDto;
    }

    // Todo 일치하는 categoryId가 없을때 처리 하기 (형태, 범위 )
    private Integer getCategoryId(String categoryParam) {
        Integer categoryId;
        try {
            categoryId = categoryParam == null ? null : Integer.parseInt(categoryParam);
        } catch (NumberFormatException e) {
            logger.info(" :: NumberFormatException occurs in categoryParam, converting to null :: ");
            categoryId = null;
        }
        return categoryId;
    }

    public int getTotalPage(int postCount, int pageSize) {
        int totalPage = 0;

        totalPage = postCount / pageSize;
        if (postCount % pageSize != 0) {
            totalPage++;
        }
        return totalPage;
    }

    // 게시물 가져오기  -> 기본 or 검색조건
    public List<BoardListDto> getBoardList(PostDao postDao, BoardListParamDto boardListParamDto) {
        List<BoardListDto> boardList = postDao.getBoardListByParams(boardListParamDto);

        for (BoardListDto post : boardList) {
            String categoryName = postDao.categoryById(post.getCategoryId());
            post.setCategoryName(categoryName);
        }
        return boardList;
    }

    private String buildUri(String reqUri, BoardListParamDto boardListParamDto) {
        StringBuilder uri = new StringBuilder(reqUri);
        List<String> queryParams = new ArrayList<>();

        if (boardListParamDto.getStartDate() != null) {
            queryParams.add("startDate=" + boardListParamDto.getStartDate());
        }
        if (boardListParamDto.getEndDate() != null) {
            queryParams.add("endDate=" + boardListParamDto.getEndDate());
        }
        if(boardListParamDto.getCategoryId() != null) {
            queryParams.add("category=" + boardListParamDto.getCategoryId());
        }
        if (boardListParamDto.getKeyword() != null) {
            queryParams.add("keyword=" + boardListParamDto.getKeyword());
        }

        queryParams.add("page=");

        if (!queryParams.isEmpty()) {
            uri.append("?").append(String.join("&", queryParams));
        }
        logger.info("queryParams = {}" , queryParams);
        return uri.toString();
    }

}
