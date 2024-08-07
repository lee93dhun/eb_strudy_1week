package com.jspBoard.controller;

import com.jspBoard.service.HttpService;
import com.jspBoard.service.ListService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/")
public class BoardController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private SqlSessionFactory sqlSessionFactory;

    Map commandMap = new HashMap();

    @Override
    public void init(ServletConfig config) throws ServletException {

        try {
            String resource = "mybatis-config.xml";
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(resource));
            commandMap.put("GET:/list", new ListService(sqlSessionFactory));
        } catch (IOException e) {
            logger.error("SqlSessionFactory Build  실패");
            throw new ServletException("Failed to initialize SqlSessionFactory",e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if ("/".equals(uri)) {
            resp.sendRedirect("list"); // 상대 경로로 수정
            return; // 리디렉션 후 더 이상의 코드 실행을 방지
        }
        executeService(req, resp);
    }

    public void executeService(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpService targetService = findTargetService(req);
        String view = targetService.doService(req, resp);
        req.getRequestDispatcher(view).forward(req, resp);
    }

    private HttpService findTargetService(HttpServletRequest req) {
        String method = req.getMethod();
        String reqUri = req.getRequestURI();
        String key = method + ":" + reqUri;
        logger.info(key);
        return (HttpService) commandMap.get(key);
    }

}
