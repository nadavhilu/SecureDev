<%@page import="containers.ForumData"%>
<%@page import="org.owasp.encoder.Encode"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@page language="java" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forum Thread</title>
</head>
<body>
	<form name="ForumThread" action="ThreadController" method="post">
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}> <input
				type="text" id="myInput" onkeyup="myFunction()"
				placeholder="enter your search." autocomplete="off">
			<c:set var="creatingUser" scope="page"
				value="${data.get(index).getCreatingUser()}" />
			<c:set var="currentUser" scope="page" value="${username}" />

			<table class="table-bordered table-responsive" id="myTable">
				<%
					List<ForumData> data = (List) request.getAttribute("posts");
					
				%>
				<tr>
					<h2>
						<%= Encode.forHtmlContent(data.get(0).getThreadName()) %>
					</h2>
				</tr>
				<%
				for(int index = 0; index < data.size(); index++)
				{
					
				%>

				<tr class="table-header">
					<td class="cell"><b>Created by: <%=Encode.forHtmlContent(data.get(index).getCreatingUser())%></b></td>
					<td class="cell"><b>Creation Date: <%=Encode.forHtmlContent(data.get(index).getCreationTime())%></b>
						<c:choose>
							<c:when test="${creatingUser}==${currentUser}">
								<input type="button" class="edit btn-primary float-right" value="Edit" id="<%= data.get(index).getId() %>">
							</c:when>
							<c:otherwise>
								<shiro:hasRole name="Moderator">
									<input type="button" class="edit btn-primary float-right" value="Edit" id="<%= data.get(index).getId() %>">
								</shiro:hasRole>
								<shiro:lacksRole name="Moderator">
								</shiro:lacksRole>
							</c:otherwise>
						</c:choose></td>
				</tr>
				<tr>
					<td class="message" bgcolor=#0 color=#ffffff colspan="2" id="message<%= data.get(index).getId() %>" value="<%= Encode.forHtmlContent(data.get(index).getContent()) %>"><%= Encode.forHtmlContent(data.get(index).getContent()) %></td>
				</tr>
				<%
					}
				%>
			</table>
			<p>
				<br />
				<shiro:user>
					<textarea rows="10" cols="50" name="PostBody" id="PostBody"
						placeholder="Please enter the message here."></textarea>
				</shiro:user>
			</p>
		</div>
		<shiro:user>
			<input class="small-button btn btn-primary" id="editReply"type="submit" value="Reply">
		</shiro:user>
			<input type="hidden" name="id" id="id" value="0">
		<shiro:hasRole name="Admin">
			<a class="little-left-margin big-button btn btn-primary" id="delete" href="/SecureDev/DeleationController?post=">delete post</a>
		</shiro:hasRole>
	</form>
	<script src="js/forumThread.js"></script>
</body>
</html>