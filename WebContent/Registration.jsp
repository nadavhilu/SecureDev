<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/mystyle.css">
<title>Registration</title>
</head>
<body>
	<h4 id="Error" class="bad" style="">${ message }</h4>
	<form name="Registry" id="Registry" enctype="multipart/form-data" action="upload" 
	onsubmit="return validate();" method="post">
		<div class="form">
			<p>
				<label class="cell">Username: </label> 
				<input type="text" id="Username" placeholder="Enter Username" name="Username" 
				onkeydown="checkUsername()" onkeypress="checkUsername()" onkeyup="checkUsername()" autofocus autocomplete="off">
				<label class="cell" id="Badusername"></label>
			</p>
			<p>
				<label class="cell">Password: </label> 
				<input type="password" id="Password" placeholder="Enter Password" name="Password"
				onkeydown="checkPassword()" onkeypress="checkPassword()" onkeyup="checkPassword()" autocomplete="off">
				<label class="cell" id="Badpassword"></label>
			</p>
			<p>
				<label class="cell">Name: </label> 
				<input type="text" id="Name" placeholder="Enter Name" name="Name" onkeydown="checkName()"
				onkeypress="checkName()" onkeyup="checkName()" autocomplete="off"> 
				<label class="cell" id="Badname"></label>
			</p>
			<p>
				<label class="cell">Lastname: </label> 
				<input type="text" id="Lastname" placeholder="Enter Lastname" name="Lastname" 
				onkeydown="checkLastName()" onkeypress="checkLastName()" onkeyup="checkLastName()" autocomplete="off">
				<label class="cell" id="Badlastname"></label>
			</p>
			<p>
				<label class="cell">Email: </label> 
				<input type="text" id="Email" placeholder="Enter Email" name="Email" onkeydown="checkEmail()"
				onkeypress="checkEmail()" onkeyup="checkEmail()" autocomplete="off"> 
				<label class="cell" id="Bademail"></label>
			</p>
			<p>
				<label class="cell">Phone number: </label> 
				<input type="text" id="Phonenumber" placeholder="Enter Phone number" name="Phonenumber"
				onkeydown="checkPhoneNumber()" onkeypress="checkPhoneNumber()" onkeyup="checkPhoneNumber()" autocomplete="off"> 
				<label class="cell" id="Badphonenumber"></label>
			</p>
			<p>
				<label class="cell">Picture: </label> 
				<input class="file-input" type="file" id="Picture" name="Picture" onkeydown="checkPicture()" 
				onkeypress="checkPicture()" onkeyup="checkPicture()" onChange="checkPicture()"> 
				<label class="cell" id="Badpicture"></label>
			</p>
		</div>
		<input class="small-button" type="submit" name="submit" value="let's go">
		<button class="small-button" type="reset" value="Clear" onclick="backtonormal();">Reset</button>
	</form>
	<script src="js/regestryinputvalidation.js"></script>
</body>
</html>