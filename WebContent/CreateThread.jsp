<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Thread</title>
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
</head>

<body>
	<shiro:user>
		<form name="NewThread" id="NewThread" action="ForumController"
			method="post">
			<div class="form">
				<input type="hidden" name="anti-csrf" value=${csrf}>
				<p>
					<label class="cell">Subject: </label> <input type="text"
						id="ThreadTitle" name="ThreadTitle"
						placeholder="Enter Thread Subject" autofocus>
				</p>
				<p>
					<label class="cell">Thread Body:</label><br />
					<textarea rows="18" cols="50" name="PostBody" id="PostBody"
						placeholder="Please enter the message here."></textarea>
				</p>
			</div>
			<input class="small-button" type="submit">
		</form>
	</shiro:user>
	<shiro:guest>
		<jsp:forward page="homeController" />
	</shiro:guest>
</body>
</html>