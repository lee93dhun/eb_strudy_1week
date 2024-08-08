<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        .container{
            width: 1100px;
            margin: 0 auto;
        }
        .search-box{
            border: 1px solid #000;
            padding: 5px 10px;
            display: flex;
        }
        .search-box input[type="text"]{
            width: 300px;
        }
        .board-box{
            margin: 30px 0;
        }
        table, tr, th ,td{
            border : 1px solid black;
            border-collapse: collapse;
            padding: 10px 7px;
            text-align: center;
        }
        .pageSection {
            display: flex;          /* Flexbox를 사용하여 가로 정렬 */
            margin: 30px 0;
            justify-content: center;
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
    <div class="container">
        <h2>자유 게시판 - 목록</h2>
        <form class="search-box" action="list" method="get">
            <label>등록일</label>
            <div>
                <input type="date" id="startDate" name="startDate" value="${param.startDate}"/> ~
                <input type="date" id="endDate" name="endDate" value="${param.endDate}">
            </div>
            <div>
                <select id="category" name="category">
                    <option>전체 카테고리</option>
                    <option>test1</option>
                    <option>test2</option>
                </select>
                <input type="text" id="keyword" name="keyword" value="${param.keyword}"
                       placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)"/>
                <button type="submit">검색</button>
            </div>
        </form>
        <div class="board-box">
            <p>총 ${allPostCnt} 건</p>
            <table>
                <colgroup>
                    <col style="width: 170px">
                    <col style="width: 160px">
                    <col style="width: 540px">
                    <col style="width: 130px">
                    <col style="width: 90px">
                    <col style="width: 230px">
                    <col style="width: 230px">
                </colgroup>
                <tr>
                    <th>카테고리</th>
                    <th>파일첨부</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>조회수</th>
                    <th>등록일자</th>
                    <th>수정일자</th>
                </tr>
                <c:choose>
                    <c:when test="${empty postList}">
                        <tr>
                            <td colspan="7">
                                <p>게시글이 없습니다.</p>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="post" items="${postList}">
                            <tr>
                                <td>${post.categoryName}</td>
                                <td>X</td> <!-- 파일 첨부가 없으므로 X로 처리 -->
                                <td>${post.postTitle}</td>
                                <td>${post.postWriter}</td>
                                <td>${post.postHits}</td>
                                <td>${post.uploadDatetime}</td>
                                <td>${post.updateDatetime}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </table>
            <div class="pageSection">
                <div class="next">
                    <button onclick="window.location.href='list?page=${ currentPageIdx-1}'"
                    ${currentPageIdx == 1 ? "disabled":"" } > ◀ </button>
                </div>
                <ul class="pagination">
                    <c:choose>
                        <c:when test="${maxPage == 0}">
                            <li><a class="page-num active" href="list?page=1">1</a></li>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="i" begin="0" end="${maxPage - 1}">
                                <c:set var="pageIdx" value="${i + 1}" />
                                <c:set var="activeCls" value="${currentPageIdx == pageIdx ? 'active' : ''}" />
                                <li><a class="page-num ${activeCls}"
                                       href="list?page=${pageIdx}"> ${pageIdx}
                                </a></li>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </ul>
                <div class="next">
                    <button onclick="window.location.href='list?page=${ currentPageIdx+1}'"
                            ${currentPageIdx == maxPage ? "disabled":"" } > ▶ </button>
                </div>
            </div>
        </div>
    </div>
</body>
<script>

</script>
</html>
