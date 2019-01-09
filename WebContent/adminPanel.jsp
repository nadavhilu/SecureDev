<%@page import="containers.UserRolesInfo"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@page import="containers.News"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin Panel</title>
</head>
<body>
	<shiro:hasRole name="Admin">
		<form name="Users" action="AdminPanel" method="post">
			<div class="form">
				<input type="hidden" name="anti-csrf" value=${csrf}> <input
					type="text" id="myInput" onkeyup="myFunction()"
					placeholder="enter your search." autocomplete="off">

				<table class="table-bordered table-responsive" id="myTable">

					<c:forEach items="${users}" var="data" varStatus="loop">
					<input name="id${loop.index}" id="id${loop.index}" type="hidden" value="${data.getId()}">
						<tr class="cursor-default">

							<td bgcolor=#0 color=#ffffff class="cell cursor-default"><input
								type="hidden" name="username${loop.index}"
								id="username${loop.index}" value=${data.getUsername()}>${data.getUsername()}
							</td>
							<c:choose>
								<c:when test="${data.isUser()}">
									<td bgcolor=#0 color=#ffffff class="cell cursor-default"><input
										id="user${loop.index}" name="user${loop.index}"
										type="checkbox" checked> user</td>
								</c:when>
								<c:otherwise>
									<td bgcolor=#0 color=#ffffff class="cell cursor-default"><input
										id="user${loop.index}" name="user${loop.index}"
										type="checkbox"> user</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${data.isModerator()}">
									<td bgcolor=#0 color=#ffffff class="cell cursor-default"><input
										id="moderator${loop.index}" name="moderator${loop.index}"
										type="checkbox" checked> moderator</td>
								</c:when>
								<c:otherwise>
									<td bgcolor=#0 color=#ffffff class="cell cursor-default"><input
										id="moderator${loop.index}" name="moderator${loop.index}"
										type="checkbox"> moderator</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${data.isAdmin()}">
									<td bgcolor=#0 color=#ffffff class="cell cursor-default"><input
										id="admin${loop.index}" name="admin${loop.index}"
										type="checkbox" checked> admin</td>
								</c:when>
								<c:otherwise>
									<td bgcolor=#0 color=#ffffff class="cell cursor-default"><input
										id="admin${loop.index}" name="admin${loop.index}"
										type="checkbox"> admin</td>
								</c:otherwise>
							</c:choose>
								<td bgcolor=#0 color=#ffffff class="cell cursor-default">
								 <a class="delete-user btn btn-primary" rolr="button" href="/SecureDev/DeleationController?idParameter=${data.getId()}">delete user</a></td>
								 <td bgcolor=#0 color=#ffffff class="cell cursor-default">
								 <a class="small-button btn btn-primary" rolr="button" href="/SecureDev/profile?idParameter=${data.getId()}">edit user</a></td>		
				
						</tr>
						
					</c:forEach>

				</table>
				<input type="hidden" name="size" id="size"
					value="<%=((List) request.getSession().getAttribute("users")).size()%>">
			</div>
			<br />
			<shiro:user>
				<button type="submit" value="Update Roles">Update Roles</button>
			</shiro:user>
		</form>
	</shiro:hasRole>
	<shiro:lacksRole name="Admin">
		<jsp:forward page="homeController"/>
	</shiro:lacksRole>
	<script src="js/AdminPanel.js"></script>
</body>
</html>