<%@page import="org.owasp.encoder.Encode"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="containers.Event"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Event Edit</title>
</head>
<body>
	<form name="editEvent" id="editEvent" action="eventHandling"
		method="post">
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}> 
				<input type="hidden" name="id" id="id" value="<%=(((Event) request.getAttribute("event")).getId())%>">
			<p>
				<label>Title: </label> <input type="text" id="eventTitle"
					placeholder="Enter Title" name="eventTitle"
					value="<%=Encode.forHtmlAttribute(((Event) request.getAttribute("event")).getEventTitle())%>">
			</p>
			<p>
				<label>Date: </label> <input type="text" name="dateDay" id="dateDay"
					maxlength="2" size="2" placeholder="day"
					value="<%=Encode.forHtmlAttribute(
						((Event) request.getAttribute("event")).getHappeningDate().toString().substring(8, 10))%>">/
				<input type="text" name="dateMonth" id="dateMonth" maxlength="2"
					size="2" placeholder="month"
					value="<%=Encode.forHtmlAttribute(
						((Event) request.getAttribute("event")).getHappeningDate().toString().substring(5, 7))%>">/
				<input type="text" name="dateYear" id="dateYear" maxlength="4"
					size="4" placeholder="year"
					value="<%=Encode.forHtmlAttribute(
						((Event) request.getAttribute("event")).getHappeningDate().toString().substring(0, 4))%>">
			</p>
			<p>
				<label>Time: </label> <input type="text" name="timeHour"
					id="timeHour" maxlength="2" size="2"
					value="<%=Encode.forHtmlAttribute(
						((Event) request.getAttribute("event")).getHappeningDate().toString().substring(11, 13))%>">:
				<input type="text" name="timeMinutes" id="timeMinutes" maxlength="2"
					size="2"
					value="<%=Encode.forHtmlAttribute(
						((Event) request.getAttribute("event")).getHappeningDate().toString().substring(14, 16))%>">
			</p>
			<p>
				<label>Max people:</label> <input type="text"
					name="participants" id="participants" placeholder="maximum number"
					value="<%=Encode.forHtmlAttribute(((Event) request.getAttribute("event")).getUserLimit().toString())%>">
			</p>
			<p>
				<label>Location</label> <input type="text" name="location"
					id="location" placeholder="the event location"
					value="<%=Encode.forHtmlAttribute(((Event) request.getAttribute("event")).getEventLocation())%>">
			</p>
			<p>
				<label>description</label><br />
				<textarea rows="20" cols="30" name="eventDescription"
					id="eventDescription"
					placeholder="enter event description if there is one"><%=Encode.forHtmlContent(((Event) request.getAttribute("event")).getEventDescription())%></textarea>
			</p>
		</div>
		<c:set var="creatingUser" scope="page"
			value="${event.getCreatingUser()}" />
		<c:set var="currentUser" scope="page" value="${username}" />
		<c:if test="${event.getParticipating() < event.getUserLimit()}">
			<c:if test="${requestScope.participating eq true}">
				<shiro:user>
					<br />
					<a class="btn btn-primary" role="button"
						href="/SecureDev/eventHandling">Participate in event</a>
					<br />
					<br />
				</shiro:user>
			</c:if>
		</c:if>
		<c:if test="${requestScope.participating ne true}">
			<shiro:user>
				<br />
				<a class="btn btn-primary" role="button"
					href="/SecureDev/eventHandling?quit">stop participating in
					event</a>
				<br />
				<br />
			</shiro:user>
		</c:if>
		<c:choose>
			<c:when test="${creatingUser}==${currentUser}">


				<input type="submit" name="submit" value="Edit Event">
			</c:when>
			<c:otherwise>
				<shiro:hasRole name="Moderator">
					<input type="submit" name="submit" value="Edit Event">
				</shiro:hasRole>
			</c:otherwise>
		</c:choose>
	</form>

	<shiro:hasRole name="Admin">
		<form action="DeleationController" method="get">
			<input type="hidden" name="id"
				value="<%=(((Event) request.getAttribute("event")).getId())%>">
			<input type="submit" value="Delete Event">
		</form>
	</shiro:hasRole>
	

</body>
</html>