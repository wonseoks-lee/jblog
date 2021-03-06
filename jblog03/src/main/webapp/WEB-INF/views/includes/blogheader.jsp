<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<ul>
		
		<c:choose>
			<c:when test="${empty authUser }">
				<li><a
					href="${pageContext.servletContext.contextPath}/user/login">로그인</a></li>
			</c:when>
			<c:otherwise>
				<li><a
					href="${pageContext.servletContext.contextPath}/user/logout">로그아웃</a></li>
			</c:otherwise>
		</c:choose>
		<c:if test="${authUser.id eq blogVo.userId }">
			<li><a
				href="${pageContext.servletContext.contextPath}/${authUser.id}/admin/basic">블로그관리</a></li>
		</c:if>
		<li><a href="${pageContext.servletContext.contextPath}/${authUser.id}">메인화면</a></li>
	</ul>
</body>
</html>