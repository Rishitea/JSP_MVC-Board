<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<meta http-equiv="Content-type" content="text/html; charset=UTF-8">
<title>게시판</title>
<link href="${pageContext.request.contextPath}/resources/css/style.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/resources/css/liststyle.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<section>
<b>글목록(전체 글:${count})</b>
<table class="listwritebutton">
	<tr>
		<td>
			<a href="${pageContext.request.contextPath}/boardtwo/writeForm.do">글쓰기</a>
		</td>
	</tr>
</table>
<c:if test="${count==0}">
<table class="listtable">
	<tr>
		<td>
		게시판에 저장된 글이 없습니다.
		</td>
	</tr>
</table>
</c:if>

<c:if test="${count>0}">
<table class="listtable">
	<tr>
		<th id="num">번호</th>
		<th id="title">제 목</th>
		<th id="writer">작성자</th>
		<th id="date">작성일</th>
		<th id="counter">조 회</th>
		<th id="fileName">첨 부</th>
	</tr>
	<c:forEach var="article" items="${articleList}">
	<tr>
		<td align="center" width="50">
			<c:out value="${number }"/>
			<c:set var="number" value="${number-1 }"/>
		</td>
		<td class="titletd">
			<c:if test="${article.depth > 0}">
				<img src="${pageContext.request.contextPath}/resources/images/level.gif" width="${5*article.depth}">
				<img src="${pageContext.request.contextPath}/resources/images/re.gif">
			</c:if>
			<c:if test="${article.depth == 0}">
				<img src="${pageContext.request.contextPath}/resources/images/level.gif" width="${5*article.depth}">
			</c:if>
			<a href="${pageContext.request.contextPath}/boardtwo/content.do?num=${article.num}&pageNum=${currentPage}">${article.subject}</a>
			<c:if test="${article.readcount >= 20}">
				<img src="${pageContext.request.contextPath}/resources/images/hot.gif">
			</c:if>
		</td>
		<td>
			<a href="mailto:${article.email}">${article.writer}</a>
		</td>
		<td>${article.regdate}</td>
		<td>${article.readcount}</td>
		
		<c:forEach var="attach" items="${attachList}">
			
			<c:if test="${attach.bno eq article.num}">
				<td>${attach.fileName}</td>
			</c:if>
			<c:if test="${attach.bno ne article.num}">
				<td>NO FILE</td>
			</c:if>
		</c:forEach>
		
	</tr>
	</c:forEach>		
</table>
</c:if>

<c:if test="${count>0}">
	<c:set var="imsi" value="${count%pageSize == 0?0:1}"/>
	<c:set var="pageCount" value="${count/pageSize + imsi}"/>
	<fmt:parseNumber var="pageCount" value="${pageCount}" integerOnly="true"/>
	
	<c:set var="pageBlock" value="${3}"/>
	
	<fmt:parseNumber var="result" value="${(currentPage-1)/pageBlock}" integerOnly="true"/>
	<c:set var="startPage" value="${result*pageBlock+1}"/>
	
	<c:set var="endPage" value="${startPage+pageBlock-1}"/>
	
	<c:if test="${endPage>pageCount}">
		<c:set var="endPage" value="${pageCount}"/>
	</c:if>
	
	<c:if test="${startPage>pageBlock}">
		<a href="${pageContext.request.contextPath}/boardtwo/list.do?pageNum=${startPage-pageBlock}">이전</a>
	</c:if>
	
	<c:forEach var="i" begin="${startPage}" end="${endPage}">
		<a href="${pageContext.request.contextPath}/boardtwo/list.do?pageNum=${i}">[${i}]</a>
	</c:forEach>
	
	<c:if test="${endPage < pageCount}">
		<a href="${pageContext.request.contextPath}/boardtwo/list.do?pageNum=${startPage+pageBlock}">다음</a>
	</c:if>
</c:if>
</section>

</body>
</html>