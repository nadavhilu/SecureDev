<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
</head>
<body>
	<h4 id="Error" class="bad" style=${loginFailed};>${ message }</h4>
	<form name="Login" id="Login" method="post">		
		<div class="form">
			<input type="hidden" name="anti-csrf" value=${csrf}>
			<p>
				<label class="cell">Username: </label>
				<input type="text" id="Username" placeholder="Enter Username" name="username" autofocus autocomplete="off">
				<label id="Badusername"></label>
			</p>
			<p>
				<label class="cell">Password: </label>
				<input type="password" id="Password" placeholder="Enter Password" name="password" autocomplete="off">
				<label id="Badpassword"></label>
			</p>
		</div>
		<input type="submit" class="small-button" name="submit" value="let's go">
		<button class="small-button" type="reset" value="Clear" onclick="backtonormal();">Reset</button>
		<button class="small-button" type="button" value="register" onclick="location.href='Registration.jsp';">register</button> 
	</form>
	<script src="js/login.js"></script>
</body>
</html>