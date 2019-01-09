<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add News</title>
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
</head>
<body>
	<shiro:user>
	<h2>Add news</h2>
	<form name="addNews" id="addNews" action="addNews" method="post">
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}> 
			<label>Title:</label> 
			<input type="text" id="title" placeholder="Enter Title" name="title" autofocus>
			<p>
				<label>News: </label><br />
				<textarea name="news" placeholder="enter the news, without links"
					id="news" rows="18" cols="50"></textarea>
			</p>
		</div>
		<input class="small-button" type="submit" name="submit"
			value="send news">
		<button class="small-button" type="reset" value="Clear"
			onclick="backtonormal();">Reset</button>
	</form>
	</shiro:user>
	<shiro:guest>
		<jsp:forward page="homeController"/>
	</shiro:guest>
</body>
</html>