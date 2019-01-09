<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Event</title>
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
</head>
<body>
	<shiro:user>
	<h2>Add news</h2>
	<form name="addEvent" id="addEvent" action="eventHandling" method="post">
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}>
			<p>
				<label class="cell">Title: </label> 
				<input type="text" id="eventTitle" placeholder="Enter Title" name="eventTitle">
			</p>
			<p>
				<label class="cell">Date: </label> 
				<input type="text" name="dateDay" id="dateDay"maxlength="2" size="2" placeholder="day">/ 
				<input type="text" name="dateMonth" id="dateMonth" maxlength="2" size="2" placeholder="month">/ 
				<input type="text" name="dateYear" id="dateYear" maxlength="4" size="4" placeholder="year">
			</p>
			<p>
				<label class="cell">Time: </label> <input type="text" name="timeHour" id="timeHour" maxlength="2" size="2">: 
				<input type="text" name="timeMinutes" id="timeMinutes" maxlength="2" size="2">
			</p>
			<p>
				<label class="cell">Number of participants:</label>
				<input type="text" name="participants" id="participants" placeholder="maximum number">
			</p>
			<p>
				<label class="cell">Location:</label>
				<input type="text" name="location" id="location" placeholder="the event location">
			</p>
			<p>
				<label class="cell">description:</label><br />
				<textarea rows="14" cols="50" name="eventDescription" id="eventDescription"
				placeholder="enter event description if there is one"></textarea>
			</p>
		</div>
		<input class="small-button" type="submit" name="submit" value="send Event">
		<button class="small-button" type="reset" value="Clear" onclick="backtonormal();">Reset</button>
		<button class="small-button" type="button" value="profile" onclick="location.href='profile';">profile</button>
	</form>
	</shiro:user>
	<shiro:guest>
		<jsp:forward page="homeController"/>
	</shiro:guest>
</body>
</html>