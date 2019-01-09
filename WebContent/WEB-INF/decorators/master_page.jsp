<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="dec"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><dec:title default="Gaming is Life" /></title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<dec:head />
</head>
<body>
	<!--body code-->
	<nav class="navbar navbar-inverse navbar-fixed-top bg-inverse">
		<a class="navbar-brand" href="homeController">Gaming Is Life</a>
		<div class="collapse navbar-collapse" id="navbarCollapse">
			<ul class="nav navbar-nav mr-auto">
				<li class="nav-item" id="Home"><a class="nav-link" href="homeController">Home</a></li>
				<li class="nav-item" id="Events"><a class="nav-link" href="eventList">Events</a></li>
				<li class="nav-item" id="News"><a class="nav-link" href="News">News</a></li>
				<li class="nav-item" id="Forum"><a class="nav-link" href="ForumController">Forum</a></li>
				<%
					if (session.getAttribute("username") == null) {
				%>
				<li class="nav-item"><a class="nav-link" href="Registration.jsp">Register</a></li>
				<%
					} else {
				%>
				<li class="get"><a class="nav-link" href="profile">Profile</a></li>
				<%
					}
				%>
				<%
					if (session.getAttribute("username") == null) {
				%>
				<li class="nav-item"><a class="nav-link" href="login.jsp">Login</a></li>
				<%
					} else {
				%>
				<li class="nav-item"><a class="nav-link"
					href="${pageContext.request.contextPath}/LogoutController"
					id="Logout">Logout</a></li>
				<%
					}
				%>
				<shiro:hasRole name="Admin">
				<li class="nav-item align-right"><a class="nav-link" href="AdminPanel">Admin Panel</a></li>
				</shiro:hasRole>
			</ul>
		</div>
	</nav>
	<div id="pageBody">
		<div id="content">
			<dec:body />
		</div>
	</div>
	<nav id="footer"
		class="navbar navbar-inverse navbar-fixed-bottom bg-inverse">
		<div class="collapse navbar-collapse" id="navbarCollapse">
			<ul class="nav navbar-nav mr-auto">
				<li class="nav-item"><a class="nav-link" href="#">About Us</a></li>
				<li class="nav-item"><a class="nav-link" href="#">Help</a></li>
				<li class="nav-item"><a class="nav-link" href="#">Jobs</a></li>
				<li class="nav-item"><a class="nav-link" href="#">Terms</a></li>
				<li class="nav-item"><a class="nav-link" href="#">Privacy</a></li>
				<li class="nav-item"><a class="nav-link" href="#">Contact
						Us</a></li>
			</ul>
			<a class="navbar-brand float-right" href="#">Copyright 2017
				Gaming is Life - All Rights Reserved</a>
		</div>
	</nav>
	<form action="#" type="hidden" id="form">
	<input type="hidden" name="anti-csrf" value=${csrf}>
	</form>
	<script src="js/master.js"></script>
</body>
</html>