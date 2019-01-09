<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@page import="containers.News"%>
<%@page import="org.owasp.encoder.Encode"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit News</title>
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
</head>
<body>
	<form action="addNews" name="editingNews" id="editingNews"
		method="post">
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}> <input
				type="hidden" name="id"
				value="<%=(((News) request.getAttribute("news")).getId())%>">

			<c:set var="creatingUser" scope="page"
				value="${requestScope.news.getCreator()}" />
			<c:set var="currentUser" scope="page" value="${username}" />
			<c:choose>
				<c:when test="${creatingUser}==${currentUser}">
					<label>Title:</label>
					<input type="text" name="title" id="title"
						value="<%=Encode.forHtmlAttribute(((News) request.getAttribute("news")).getTitle())%>">

				</c:when>
				<c:otherwise>

					<shiro:hasRole name="Moderator">
						<label>Title:</label>
						<input type="text" name="title" id="title"
							value="<%=Encode.forHtmlAttribute(((News) request.getAttribute("news")).getTitle())%>">
					</shiro:hasRole>
					<shiro:lacksRole name="Moderator">
						<table class="table-bordered table-responsive">
							<thead>
								<tr class=table-header>
									<td class="cell">Title:</td>
								</tr>
							</thead>
							<tr>
								<td bgcolor=#0 color=#ffffff class="cell" name="title"
									id="title"><%=Encode.forHtmlAttribute(((News) request.getAttribute("news")).getTitle())%></td>
							</tr>
						</table>
					</shiro:lacksRole>

				</c:otherwise>
			</c:choose>
			<br />
<!-- 				<label>News:</label><br /> -->
				<c:choose>
					<c:when test="${creatingUser}==${currentUser}">
					<label>News:</label>
					
						<textarea name="news" id="news" rows="18" cols="50">
<%=Encode.forHtmlContent(((News) request.getAttribute("news")).getContent())%> 
				</textarea>
					</c:when>
					<c:otherwise>
							<shiro:hasRole name="Moderator">
						<label>News:</label>
						<br />
						<textarea name="news" id="news" rows="18" cols="50">
<%=Encode.forHtmlContent(((News) request.getAttribute("news")).getContent())%>
							</textarea>
					</shiro:hasRole>
						
					</c:otherwise>
				</c:choose>
<shiro:lacksRole name="Moderator">
				<table class="table-bordered table-responsive">
					<thead>
						<tr class=table-header>
							<td class="cell">News:</td>
						</tr>
					</thead>
					<tr>
						<td bgcolor=#0 color=#ffffff class="cell" name="title" id="title"><%=Encode.forHtmlContent(((News) request.getAttribute("news")).getContent())%></td>
					</tr>
				</table>
			</shiro:lacksRole>

		</div>
<%-- 		<c:set var="trueVal" value="${requestScope.news.isVisable()}"/> --%>
<%-- 		<c:out value="${trueVal}"/> --%>
<%-- 		<c:out value="${news.isVisable()}"/> --%>
<%-- 		<c:out value="${requestScope.news.isVisable()=='true'}"/> --%>
			<shiro:hasRole name="Moderator">
			<c:choose>
								<c:when test="${requestScope.news.isVisable()eq true}">
									<input type="checkbox" name="visible" checked>
									<label class="white">make news visible </label>
								</c:when>
								<c:otherwise>
									<input type="checkbox" name="visible">
									<label class="white">make news visible </label>
								</c:otherwise>
			</c:choose>
<!-- 			<input type="checkbox" name="visible"> -->
<!-- 			<label class="white">make news visible </label> -->
			<br />
			<input class="small-button" type="submit" name="submit" id="submit"
				value="submit edit">
			</shiro:hasRole>
	</form>
	<shiro:hasRole name="Admin">
		<form action="DeleationController" method="post">
			<input type="hidden" name="id"
				value="<%=(((News) request.getAttribute("news")).getId())%>">
			<input type="submit" value="Delete News">
		</form>
	</shiro:hasRole>

</body>
</html>