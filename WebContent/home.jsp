<%@page import="containers.News"%>
<%@page import="containers.Event"%>
<%@page import="org.owasp.encoder.Encode"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Gameing Is Life</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
	<form action="homeController" method="post">
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}> 
			<h4 id="Error" class="bad" style=${ErrorShow};>${ message }</h4>
			<input type="text" id="myInput" name="myInput" placeholder="enter your search."
				autocomplete="off"> <input type="checkbox" name="news" id="news"
				value="news"> <label class="white">News </label> 
				<input type="checkbox" name="events" id="events" value="events"> 
				<label class="white">Events </label> <input type="submit" value="Search">
			<table class="table-bordered table-hover table-responsive margin" id="eventsTable">
				<thead class="thead">
					<tr class="table-header">
						<td class="cell" colspan="2"><b>Events</b></td>
					</tr>
				</thead>
				<%
					List<Event> eventsData = (List) request.getAttribute("events");
					for (int index = 0; index < eventsData.size(); index++) {
				%>

				<tr <%=Encode.forHtmlContent(eventsData.get(index).getLink())%>>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(eventsData.get(index).getEventTitle())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(eventsData.get(index).getHappeningDate().toString())%></td>
				</tr>
				<%
					}
				%>
			</table>
			<table class="table-bordered table-hover table-responsive margin" id="newsTable">
				<thead class="thead">
					<tr class="table-header">
						<td class="cell" colspan="2"><b>News</b></td>
					</tr>
				</thead>

				<%
					List<News> newsData = (List) request.getAttribute("news");
					for (int index = 0; index < newsData.size(); index++) {
				%>

				<tr <%=Encode.forHtmlContent(newsData.get(index).getLink())%>>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(newsData.get(index).getTitle())%></td>
					<td bgcolor=#0 color=#ffffff class="cell"><%=Encode.forHtmlContent(newsData.get(index).getCreator())%></td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</form>
	<script src="js/tables.js"></script>
</body>
</html>