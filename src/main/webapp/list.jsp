<%@ page import="com.jspBoard.entity.PostEntity" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %><%--
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
        .pageSection {
            display: flex;          /* Flexbox를 사용하여 가로 정렬 */
            align-items: center;
        }
        .prev, .next {
            margin: 0 10px;         /* 화살표 아이콘 사이에 여백 추가 */
        }
        .pagination {
            list-style: none;
            padding: 0;
            margin: 0;
            display: flex;          /* Flexbox를 사용하여 리스트 항목을 가로로 정렬 */
        }
        .pagination li {
            margin: 0 5px;
        }
        .pagination a{
            text-decoration: none;
            color : #000;
            font-size: 20px;
            font-weight: 600;
        }
        .pagination a.active{
            color: red;
        }

    </style>
</head>
<body>
    <%
        List<PostEntity> postList = (List<PostEntity>) request.getAttribute("postList");
        int maxPage = (Integer) request.getAttribute("maxPage");
        int findPostCnt = (Integer) request.getAttribute("allPostCnt");
        int currentPageIdx = (Integer) request.getAttribute("currentPageIdx");
//        int postsPerPage = (Integer) request.getAttribute("postsPerPage");

    %>
    <p>검색된 게시물 : <%=findPostCnt%> 개</p>
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
            if(postList == null  ||  postList.size() == 0){
        %>
            <tr>
                <td colspan="7">
                    <p>게시글이 없습니다.</p>
                </td>
            </tr>
        <%
            }else{
                for(int i=0; i<postList.size(); i++){
                    PostEntity post = postList.get(i);
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
    <div class="pageSection">
        <div class="prev">
            <button onclick="window.location.href='list?page=<%= currentPageIdx-1%>'"
                    <%= currentPageIdx == 1 ? "disabled":"" %> >◀</button>
        </div>
        <ul class="pagination">
        <%
            if(maxPage == 0){
        %>
                <li><a class="page-num active" href="list?page=1"> 1 </a></li>
        <%
            }else {
                for(int i=0; i<maxPage; i++){
                    int pageIdx = i+1;
                    String activeCls = (currentPageIdx == pageIdx) ? "active" : "";
        %>
                <li><a class="page-num <%=activeCls%>"
                       href="list?page=<%= pageIdx %>"> <%= pageIdx %>
                </a></li>
        <%
               }
            }
        %>
        </ul>
        <div class="next">
            <button onclick="window.location.href='list?page=<%= currentPageIdx+1%>'"
                    <%= currentPageIdx == maxPage ? "disabled":"" %> > ▶ </button>
        </div>
    </div>
</body>
<script>
    let navigatePage = (pageNum) =>{
        let maxPage = <%= maxPage %>;
        if(pageNum < 1 || pageNum > maxPage){
            alert('')
        }
    }
</script>
</html>
