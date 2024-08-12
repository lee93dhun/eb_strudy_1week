<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: lee93
  Date: 2024-08-07
  Time: 오전 9:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시판 - 목록</title>
    <style>
        .container {
            width: 1100px;
            margin: 0 auto;
        }
        .search-box {
            border: 1px solid #000;
            padding: 5px 10px;
            display: flex;
        }

        .search-box input[type="text"] {
            width: 300px;
        }

        .board-box {
            margin: 30px 0;
        }

        table, tr, th, td {
            border: 1px solid black;
            border-collapse: collapse;
            padding: 10px 7px;
            text-align: center;
        }

        .pageSection {
            display: flex; /* Flexbox를 사용하여 가로 정렬 */
            margin: 30px 0;
            justify-content: center;
        }

        .pagination {
            list-style: none;
            padding: 0;
            margin: 0;
            display: flex; /* Flexbox를 사용하여 리스트 항목을 가로로 정렬 */
        }

        .pagination li {
            margin: 0 5px;
        }

        .pagination a {
            text-decoration: none;
            color: #000;
            font-size: 20px;
            font-weight: 600;
        }

        .pagination a.active {
            color: red;
        }

        .addPostBtn{
            float: right;
            text-decoration: none;
            border: 1px solid black;
            border-radius: 5px;
            color: black;
            padding: 10px 15px;
        }

    </style>
</head>
<body>
<div class="container">
    <h2>자유 게시판 - 목록</h2>
    <form class="search-box" action="/board/list" method="get" onsubmit="return filterEmptyFields(this);">
        <label>등록일</label>
        <div>
            <input type="date" id="startDate" name="startDate" value="${searchParam.startDate}"/> ~
            <input type="date" id="endDate" name="endDate" value="${searchParam.endDate}">
        </div>
        <div>
            <select id="category" name="category">
                <option value="" ${empty searchParam.categoryId ? 'selected' : ''}>전체 카테고리</option>
                <c:forEach var="category" items="${categoryList}">
                    <option value="${category.categoryId}"
                        ${category.categoryId == searchParam.categoryId ? 'selected' : ''}>
                            ${category.categoryName}
                    </option>
                </c:forEach>
            </select>
            <input type="text" id="keyword" name="keyword" value="${searchParam.keyword}"
                   placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)"/>
            <button type="submit">검색</button>
        </div>
    </form>
    <div class="msgBox">
        <p>${errorMsg}</p>
    </div>
    <div class="board-box">
        <p>총 ${postCount} 건</p>
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
                <c:when test="${empty boardList}">
                    <tr>
                        <td colspan="7">
                            <p>게시글이 없습니다.</p>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="post" items="${boardList}">
<%--                        <input type="hidden" value="${post.postId}"/>--%>
                        <tr>
                            <td>${post.categoryName}</td>
                            <td>X</td> <!-- 파일 첨부가 없으므로 X로 처리 -->
                            <c:choose>
                                <c:when test="${fn:length(post.postTitle) > 80}">
                                    <c:set var="shortTitle" value="${fn:substring(post.postTitle, 0, 80)}" />
                                    <td><a href="board/detail?idx=${post.postId}">${post.postTitle}...</a></td>
                                </c:when>
                                <c:otherwise>
                                    <td><a href="board/detail?idx=${post.postId}">${post.postTitle}</a></td>
                                </c:otherwise>
                            </c:choose>
                            <td>${post.postWriter}</td>
                            <td>${post.postHits}</td>
                            <td>${fn:replace(post.uploadDatetime, 'T', ' ')}</td>

                            <c:set var="formattedUpdateDatetime"
                                   value="${fn:replace(post.updateDatetime, '9999-12-31T23:59:59', '-')}" />
                            <c:set var="finalUpdateDatetime"
                                   value="${fn:replace(formattedUpdateDatetime, 'T', ' ')}" />
                            <td>${finalUpdateDatetime}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </table>
        <div class="pageSection">
            <div class=".prev">
                <button onclick="window.location.href='${uriResult}${ currentPage-1}'"
                ${currentPage == 1 ? "disabled":"" } > ◀
                </button>
            </div>
            <ul class="pagination">
                <c:choose>
                    <c:when test="${totalPage == 0}">
                        <li><a class="page-num active" href="list?page=1">1</a></li>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="i" begin="1" end="${totalPage}">
                            <c:set var="pageIdx" value="${i}"/>
                            <c:set var="activeCls" value="${currentPage == pageIdx ? 'active' : ''}"/>
                            <li><a class="page-num ${activeCls}"
                                   href="${uriResult}${pageIdx}"> ${pageIdx}
                            </a></li>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </ul>
            <div class="next">
                <button onclick="window.location.href='${uriResult}${ currentPage+1}'"
                ${currentPage == totalPage ? "disabled":"" } > ▶
                </button>
            </div>
        </div>
    </div>
    <div>
        <a href="/board/register" class="addPostBtn">등록</a>
    </div>
</div>
</body>
<script>

    const filterEmptyFields =  (form) => {
        const elements = form.elements;

        Array.from(elements).forEach(element => {
            console.log(element.name +" : ",element.value);
            if (element.value === "") {
                element.removeAttribute("name");
            }
        });
        return true;
    }

</script>
</html>
