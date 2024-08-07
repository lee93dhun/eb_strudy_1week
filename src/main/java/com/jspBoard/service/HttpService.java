package com.jspBoard.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HttpService {

    String doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException;
}