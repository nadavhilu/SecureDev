<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@page import="containers.User"%>
<%@page import="org.owasp.encoder.Encode"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
<title>Your Profile</title>
</head>
<body>
	<shiro:user>
		<form name="Profile" id="Profile" action="profile" method="post"
			enctype="multipart/form-data">
			<div class="form">
				<input type="hidden" name="anti-csrf" value=${csrf}>
				<p>
					<label class="cell">Username:</label> <input type="text"
						name="Username" id="Username"
						value="<%=Encode.forHtml(((User) request.getAttribute("user")).getUsername())%>"
						disabled="disabled">
					<button class="big-button" type="button" onclick="editUsername();">edit
						user name</button>
				</p>
				<p>
					<label class="cell">Password: </label> <input type="password"
						name="Password" id="Password"
						value="<%=Encode.forHtml(((User) request.getAttribute("user")).getPassword())%>"
						disabled="disabled">
					<button class="big-button" type="button" onclick="editPassword();">edit
						password</button>
				</p>
				<p>
					<label class="cell">Name: </label> <input type="text" name="Name"
						id="Name"
						value="<%=Encode.forHtml(((User) request.getAttribute("user")).getName())%>"
						disabled="disabled">
					<button class="big-button" type="button" onclick="editName();">edit
						name</button>
				</p>
				<p>
					<label class="cell">Last Name: </label> <input type="text"
						name="LastName" id="LastName"
						value="<%=Encode.forHtml(((User) request.getAttribute("user")).getLastName())%>"
						disabled="disabled">
					<button class="big-button" type="button" onclick="editLastName();">edit
						last name</button>
				</p>
				<p>
					<label class="cell">Email: </label> <input type="text" name="Email"
						id="Email"
						value="<%=Encode.forHtml(((User) request.getAttribute("user")).getEmail())%>"
						disabled="disabled">
					<button class="big-button" type="button" onclick="editEmail();">edit
						email</button>
				</p>
				<p>
					<label class="cell">Phone Number: </label> <input type="text"
						name="PhoneNumber" id="PhoneNumber"
						value="<%=Encode.forHtml(((User) request.getAttribute("user")).getPhoneNumber())%>"
						disabled="disabled">
					<button class="bigger-button" type="button"
						onclick="editPhoneNumber();">edit phone number</button>
				</p>
				<p>
					<label class="cell">Picture: </label> <img class="pic"
						alt="picture"
						src="<%=Encode.forHtmlAttribute(((User) request.getAttribute("user")).getPicture())%>">
				</p>
				<p>
					<label class="cell"> chanage Picture:</label> <input
						class="file-input left-margin" type="file" name="newPicture"
						id="newPicture" disabled="disabled">
					<button class="big-button" type="button" onclick="editPicture();">edit
						picture</button>
				</p>
				<p>
					<input class="cell big-button" type="submit" name="submit"
						id="submit" value="submit edit" disabled="disabled"> <label
						class="cell"></label>
					<button class="cell big-button" type="button"
						onclick="enableAll();">Enable Edit Profile</button>
				</p>
			</div>
		</form>
		<script src="js/profile.js"></script>
	</shiro:user>
	<shiro:guest>
		<jsp:forward page="homeController" />
	</shiro:guest>
</body>
</html>