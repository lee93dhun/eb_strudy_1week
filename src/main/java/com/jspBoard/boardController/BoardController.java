package com.jspBoard.boardController;

import com.jspBoard.boardService.HttpService;
import com.jspBoard.boardService.ListService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BoardController extends HttpServlet {

    private final Logger logger = LogManager.getLogger(this.getClass());

    Map commandMap = new HashMap();

    @Override
    public void init(ServletConfig config) {
        commandMap.put("GET:/list", new ListService());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        executeService(req, resp);
    }

    public void executeService(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpService targetService = findTargetService(req);
        targetService.doService(req, resp);
        logger.info("targetService: " ,targetService.getClass());
    }

    private HttpService findTargetService(HttpServletRequest req) {
        String method = req.getMethod();
        String reqUri = req.getRequestURI();
        String key = method + ":" + reqUri;
        logger.info(key);
        return (HttpService) commandMap.get(key);
    }

}
