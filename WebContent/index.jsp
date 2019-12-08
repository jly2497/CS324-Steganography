<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.util.*, java.io.*" %>
<%
String LogIn;
String LogOut;
String imgPath;
if (session.getAttribute("Username") == "" || session.getAttribute("Username") == null) {
	LogIn = "";
	LogOut = "hidden";
} else {
	LogIn = "hidden";
	LogOut = "";
}
if (session.getAttribute("EncodedOutput") == "" ||session.getAttribute("EncodedOutput") == null) {
	imgPath = "#";
} else {
	imgPath = "/web/images/tmp/out.png";
}

%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link
		href="https://fonts.googleapis.com/css?family=Open+Sans&amp;display=swap"
		rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="web/style/style.css">
	
	<script src="web/scripts/jquery-3.4.1.min.js" type="text/javascript"></script>
	<script src="web/scripts/script.js" type="text/javascript"></script>
	<script>
		console.log();
	</script>
<title>Steganography App</title>
</head>
<body>
	<div class="header-wrap">
		<h1>Steganography Application</h1>
	</div>
	<div class="UserLogin" <%=LogIn %>>
		<form class="login" method="post" action="/Steganography/account_hdlr">
			<label for="Username">Username: </label>
			<input type="text" name="Username" id="Username" placeholder="Username" required>
			
			<label for="Password">Password: </label>
			<input type="password" name="Password" id="Password" placeholder="Password" required>
				
			<input type="submit" name="sign-in" placeholder="Create Account" value="Log In">
			<button type="button" onclick="window.location.href = '/Steganography/register'">Register</button>
		</form>
	</div>
	
	<button type="button" onclick="window.location.href = '/Steganography/account_hdlr?logout=true'" <%=LogOut %>>Log Out</button>
	
	<div class="content">
		<div class="sidebar">
			<h2>Sidebar</h2>
			<div class="Error Message">
				<% 
					if (session.getAttribute("ErrorMessage") != null) {
						if (!session.getAttribute("ErrorMessage").equals(""))  {
							out.print("<p>" + session.getAttribute("ErrorMessage") + "</p>"); 
						} else { out.print("<p>Error Message is blank</p>"); }
					}
				%>
			</div>
			<form action="/Steganography/encode_hdlr" method="post" id="Form" enctype="multipart/form-data">
				<label for="UploadFile">Upload Image:</label>
				<input type="file" name="UploadFile" id="UploadFile" accept="image/jpeg,image/jpg,image/png" data-max-size=5242880 required><br> 
				
				<label for="TextOrImage">Select Text or Image:</label>
				<select name="TextOrImage" id="TextOrImage">
					<option value="text">Encode Text</option>
					<option value="image">Encode Image</option>
				</select><br>
				
				<div class="file-container" id="file-container">
					<label for="UploadToEnc">Upload Image to hide:</label>
					<input type="file" name="UploadToEnc" id="UploadToEnc" accept="image/jpeg,image/jpg,image/png" data-max-size=5242880><br>
					<img src="#" alt="Image failed to load" id="EncodeImg" hidden="true">
				</div>
				
				<div class="text-container" id="text-container">
					<textarea name="TextToEnc" id="TextMessage" form="Form" cols="" rows="" pattern=".[1,500]" required>Enter text to encode here...</textarea><br> 
				</div>
				
				<input type="submit" name="Encode" id="Encode" value="Encode"><br>
				<!--  <input type="submit" name="Decode" id="Decode" value="Decode"><br> -->
				<input type="reset" name="reset" id="Reset"><br>
			</form>
			<button>Replay</button>
			<button>Save</button>
			<button>Gallery</button>
			<button>View Logs</button>
		</div>
		
		<div class="workspace">
			<h2>Image</h2>
			<div class="img-container">
				<img src="<%=imgPath %>" alt="Image failed to load" id="OriginalImg" hidden="true"> 
				<!-- <img src="web/images/tmp/out.png" id="EncodedImg" hidden="true"></img> -->
			</div>
		</div>
	</div>
</body>
</html>
