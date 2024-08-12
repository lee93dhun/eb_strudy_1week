package com.jspBoard.service;

import com.jspBoard.dto.CategoryDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class RegisterService implements HttpService {

        private final Logger logger = LogManager.getLogger(this.getClass());
        private SqlSessionFactory sqlSessionFactory;

        public RegisterService(SqlSessionFactory sqlSessionFactory) {
            this.sqlSessionFactory = sqlSessionFactory;
        }
    @Override
    public String doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            logger.info("::RegisterService::doService");
            String view = "/WEB-INF/views/register.jsp";
        return view;
    }
}
