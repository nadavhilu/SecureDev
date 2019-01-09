<%@page import="containers.Event"%>
<%@page import="org.owasp.encoder.Encode"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page language="java" import="java.util.*"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Events</title>
</head>
<body>

	<form>
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}> <input
				type="text" id="myInput" onkeyup="myFunction()"
				placeholder="enter your search." autocomplete="off">
			<table class="table-bordered table-hover table-responsive"
				id="myTable">

				<thead class="thead">
					<tr class="table-header">
						<h2>Upcoming Events</h2>
					</tr>
					<tr class="table-header">
						<td class="cell"><b>Title</b></td>
						<td class="cell"><b>Happening Date</b></td>
						<td class="cell"><b>Number of participants</b></td>
						<td class="cell"><b>User limit</b></td>
					</tr>
				</thead>
				<%
						List<Event> data = (List) request.getAttribute("list");
							for (int index = 0; index < data.size(); index++) {
					%>

				<tr <%=Encode.forHtmlContent(data.get(index).getLink())%>>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(data.get(index).getEventTitle())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(data.get(index).getHappeningDate().toString())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(data.get(index).getParticipating().toString())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(data.get(index).getUserLimit().toString())%></td>
				</tr>
				<%
						}
					%>
			</table>

			<table class="table-bordered table-hover table-responsive"
				id="myTable">
				<thead class="thead">
					<tr class="table-header">
						<h2>Passed Events</h2>
					</tr>
					<tr class="table-header">
						<td class="cell"><b>Title</b></td>
						<td class="cell"><b>Happening Date</b></td>
						<td class="cell"><b>Number of participants</b></td>
						<td class="cell"><b>User limit</b></td>
					</tr>
				</thead>
				<%
						List<Event> oldData = (List) request.getAttribute("oldList");
							for (int index = 0; index < oldData.size(); index++) {
					%>

				<tr <%=Encode.forHtmlContent(oldData.get(index).getLink())%>>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(oldData.get(index).getEventTitle())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(oldData.get(index).getHappeningDate().toString())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(oldData.get(index).getParticipating().toString())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(oldData.get(index).getUserLimit().toString())%></td>
				</tr>
				<%
						}
					%>
			</table>
		</div>
		<br />
		<shiro:user>
			<button type="button" value="Create New Event"
				onclick="location.href='createEvent.jsp';">Create New Event</button>
		</shiro:user>
	</form>
	<script src="js/tables.js"></script>

</body>
</html>