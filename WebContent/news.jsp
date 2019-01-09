<%@page import="containers.News"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page language="java" import="java.util.*" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>News</title>
</head>
<body>
<form name="Forum" action="addNews.jsp" method="post">
<div class="form">
		<input type="hidden" name="anti-csrf" value=${csrf}>
		<input type="text" id="myInput" onkeyup="myFunction()" placeholder="enter your search." autocomplete="off">
		<table class="table-bordered table-hover table-responsive" id="myTable">
		<thead class="thead">
		<tr class="table-header">
			<td class="cell"><b>Title</b></td>
			<td class="cell"><b>Created by</b></td>
		</tr>
		</thead>
		<%
			Iterator itr;
		%>
		<%
			List<News> data = (List) request.getAttribute("news");
			for(int index = 0; index < data.size(); index++)
			{
		%>

		<tr <%=data.get(index).getLink()%>>
			<td bgcolor=#0 color=#ffffff class="cell"><%=data.get(index).getTitle()%></td>
			<td bgcolor=#0 color=#ffffff class="cell"><%=data.get(index).getCreator()%></td>
		
 		</tr>
		<%
			}
		%>
	</table>
	</div>
	<br/>
	<shiro:user>
		<button type="submit" value="Create News">Add News</button> 
	</shiro:user>
</form>
<script src="js/tables.js"></script>
	

</body>
</html>