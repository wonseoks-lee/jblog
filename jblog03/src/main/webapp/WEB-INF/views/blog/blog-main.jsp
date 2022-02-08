<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBlog</title>
<Link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/jblog.css">
</head>
<body>
	<div id="container">
		<div id="header">
			<h1>${blogVo.title}</h1>
			<c:import url="/WEB-INF/views/includes/blogheader.jsp" />
		</div>
		<div id="wrapper">
			<div id="content">
				<div class="blog-content">
					<c:choose>
						<c:when test="${pList eq null }">
							<c:forEach items="${defaultPost}" begin="0" end="0"
								var="defaultPostVo" varStatus="status">
								<h4>${defaultPostVo.title}</h4>
								<p>${fn:replace(defaultPostVo.contents, newline, "<br/>")}
								<p>
							</c:forEach>

						</c:when>
						<c:otherwise>
							<h4>${postVo.title }</h4>
							<p>${fn:replace(postVo.contents, newline, "<br/>")}
							<p>
						</c:otherwise>
					</c:choose>

				</div>
				<c:choose>
					<c:when test="${pList eq null }">
						<c:forEach items="${defaultPost}" var="defaultPostVo" varStatus="status">
							<ul class="blog-list">
								<li><a
									href="${pageContext.request.contextPath}/${blogId}/${defaultPostVo.categoryNo}/${defaultPostVo.no}">${defaultPostVo.title }</a>
									<span>${defaultPostVo.regDate}</span></li>
									
							</ul>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:forEach items="${pList}" var="postVo" varStatus="status">
							<ul class="blog-list">
								<li><a
									href="${pageContext.request.contextPath}/${blogId}/${postVo.categoryNo}/${postVo.no}">${postVo.title }</a>
									<span>${postVo.regDate}</span></li>
							</ul>
						</c:forEach>
					</c:otherwise>
				</c:choose>

			</div>
		</div>

		<div id="extra">
			<div class="blog-logo">
				<img src="${pageContext.request.contextPath}/${blogVo.logo}">
			</div>
		</div>

		<div id="navigation">
			<h2>카테고리</h2>
			<c:forEach items="${cList}" var="categoryVo" varStatus="status">
				<ul>
					<li><a
						href="${pageContext.request.contextPath}/${blogId}/${categoryVo.no}">${categoryVo.name}</a></li>
				</ul>
			</c:forEach>
		</div>

		<div id="footer">
			<p>
				<strong>${blogVo.title}</strong> is powered by JBlog (c)2016
			</p>
		</div>
	</div>
</body>
</html>