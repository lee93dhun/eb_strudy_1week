package com.jspBoard.boardService;


import com.jspBoard.boardController.BoardController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListService implements HttpService{

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public String doService(HttpServletRequest req, HttpServletResponse resp){
        logger.info("doService 실행 ", req.getMethod(), resp.getStatus());
        return "";
    }
}
