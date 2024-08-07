<%@ page import="com.jspBoard.entity.PostEntity" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: lee93
  Date: 2024-08-07
  Time: 오전 9:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시판 목록</title>
    <style>
        table, tr, th ,td{
            border : 1px solid black;
            border-collapse: collapse;
            padding: 20px 10px;
            text-align: center;
        }

    </style>
</head>
<body>
    <table>
        <tr>
            <th>카테고리</th>
            <th>파일첨부</th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회수</th>
            <th>등록일자</th>
            <th>수정일자</th>
        </tr>
        <%
            List<PostEntity> allList = (List<PostEntity>) request.getAttribute("allList");
        %>
        <%
            if(allList == null  ||  allList.size() == 0){
        %>
            <tr>
                <td colspan="8">
                    <p>게시글이 없습니다.</p>
                </td>
            </tr>
        <%
            }else{
                for(int i=0; i<allList.size(); i++){
                    PostEntity post = allList.get(i);
        %>
                <tr>
                    <td><%= post.getCategoryName() %></td>
                    <td>X</td>  <!-- 출력 = null -->
                    <td><%= post.getPostTitle() %></td>
                    <td><%= post.getPostWriter()%></td>
                    <td><%= post.getPostHits() %></td>
                    <td><%= post.getUploadDatetime() %></td>
                    <td><%= post.getUpdateDatetime() %></td>
                </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
