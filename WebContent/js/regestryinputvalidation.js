function validate()
{
	var Username=checkUsername();
	var Password=checkPassword();
	var Name=checkName();
	var Lastname=checkLastName(); 
	var Email=checkEmail();
	var PhoneNumber=checkPhoneNumber();
	var Picture=checkPicture();
	return Username&&Password&&Name&&Lastname&&Email&&PhoneNumber&&Picture;
}
function checkUsername()
{
	var Username=document.getElementById("Username");
	var ok=true;
	if(Username.value.length>20)
	{
		ok=false;
		document.getElementById("Badusername").innerHTML="<span style='color:red;'>Username is too long</span>";
	}
	else if(Username.value.length<4)
	{
		ok=false;
		document.getElementById("Badusername").innerHTML="<span style='color:red;'>Username is too short</span>";
	}
	else
	{
		document.getElementById("Badusername").innerHTML="";
	}
	return ok;
}
function checkPassword()
{
	var Password=document.getElementById("Password");
	var strongPassRE = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{6,20})/;
	var mediumPassRE = /^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{6,20})/;
//	var mediumPassRE = /^(((?=.*\d)((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[!@#\$%\^&\*]))|((?=.*[A-Z])(?=.*[!@#\$%\^&\*]))|((?=.*[0-9])(?=.*[!@#\$%\^&\*]))|((?=.*[A-Z])(?=.*[0-9]))).{6,20}) /;
	var ok=true;
	if(Password.value.length>20)
	{
		ok=false;
		document.getElementById("Badpassword").innerHTML="<span style='color:red;'>Password is too long</span>";
	}
	else if(strongPassRE.test(Password.value))
	{
		document.getElementById("Badpassword").innerHTML="<span style='color:rgb(127,255,0);'>Password is Strong</span>";
	}
	else if(mediumPassRE.test(Password.value))
	{
		document.getElementById("Badpassword").innerHTML="<span style='color:orange;'>Password is medium</span>";	
	}
	else
	{
		ok=false;
		document.getElementById("Badpassword").innerHTML="<span style='color:red;'>Password is poor</span>";
	}
	return ok;
}
function checkName()
{
	var Name=document.getElementById("Name");	
	var ok=true;
	if(Name.value.length>20)
	{
		ok=false;
		document.getElementById("Badname").innerHTML="<span style='color:red;'>Name is too long</span>";
	}
	else if(Name.value.length<2)
	{
		ok=false;
		document.getElementById("Badname").innerHTML="<span style='color:red;'>Name is too short</span>";
	}
	else
	{
		document.getElementById("Badname").innerHTML="";
	}	
	return ok;
}
function checkLastName()
{
	var Lastname=document.getElementById("Lastname");
	var ok=true;
	if(Lastname.value.length>20)
	{
		ok=false;
		document.getElementById("Badlastname").innerHTML="<span style='color:red;'>Lastname is too long</span>";
	}
	else if(Lastname.value.length<2)
	{
		ok=false;
		document.getElementById("Badlastname").innerHTML="<span style='color:red;'>Lastname is too short</span>";
	}
	else
	{
		document.getElementById("Badlastname").innerHTML="";
	}
	return ok;
}	
function checkEmail()
{
	var Email=document.getElementById("Email");
	var EmailRE=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
	var ok=true;
	if(Email.value.length>320)
	{
		ok=false;
		document.getElementById("Bademail").innerHTML="<span style='color:red;'>email address too long</span>";
	}
	else if(!EmailRE.test(Email.value))
	{
		ok=false;
		document.getElementById("Bademail").innerHTML="<span style='color:red;'>Not a valid email</span>";
	}
	else
	{
		document.getElementById("Bademail").innerHTML="";
	}			
	return ok;
}
function checkPhoneNumber()
{
	var Phonenumber=document.getElementById("Phonenumber");
	var Goodnumber=/^\d{10}$/;
	var ok=true;
	if(Phonenumber.value.length!=10)
	{
		ok=false;
		document.getElementById("Badphonenumber").innerHTML="<span style='color:red;'>Phone number must be 10 digits</span>";
	}
	else if(!Goodnumber.test(Phonenumber.value))
	{
		ok=false;
		document.getElementById("Badphonenumber").innerHTML="<span style='color:red;'>Phone number must be digits only</span>";
	}
	else
	{
		document.getElementById("Badphonenumber").innerHTML="";
	}	
	return ok;
}	
function checkPicture()
{
	var Picture=document.getElementById("Picture");
	var ok=true;
	if(Picture.value!="")
	{
		
		if(Picture.files[0].size>1024*1024*2)
		{
			ok=false;
			document.getElementById("Badpicture").innerHTML="<span style='color:red;'>Picture's File size is too big</span>";
		}
		else
		{
			document.getElementById("Badpicture").innerHTML="";
		}
		var type = Picture.value.substring(Picture.value.lastIndexOf("."));
		var goodPicture = /^.((jpg)|(JPG)|(jpeg)|(JPEG)|(png)|(PNG))$/;
	
		if(!goodPicture.test(type))
		{
			ok = false;
			document.getElementById("Badpicture").innerHTML = "<span style='color:red'>The Picture type is not premited, use only .jpg/.jpeg/.png</span>"
		}
	}
	return ok;
}

function backtonormal()
{
	document.getElementById("Error").style="visibility:hidden";
	document.getElementById("Badusername").innerHTML="";
	document.getElementById("Badpassword").innerHTML="";
	document.getElementById("Badname").innerHTML="";
	document.getElementById("Badlastname").innerHTML="";
	document.getElementById("Bademail").innerHTML="";
	document.getElementById("Badphonenumber").innerHTML="";
	document.getElementById("Badpicture").innerHTML="";
}