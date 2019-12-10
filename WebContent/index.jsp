<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, java.io.*"%>
<%
	String imgPath = "#";
%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link
	href="https://fonts.googleapis.com/css?family=Open+Sans&amp;display=swap"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="web/style/style.css">
<link rel="stylesheet" type="text/css"
	href="web/style/bootstrap.min.css">
<script src="web/scripts/jquery-3.4.1.min.js" type="text/javascript"></script>
<script src="web/scripts/script.js" type="text/javascript"></script>
<script type="text/javascript">
	window.onload = function() {
		
		var rStr = "<%=session.getAttribute("Username") %>";
		/*
		if (rStr !== "") {
			$('#Replay').removeAttr("disabled");
		}
		
		$('#Replay').on('click', function(e) {
			window.location.href = '/Steganography/encode_hdlr?replay=true';
		});*/
		
		$('#text-out').hide();
		$('#OriginalImg').hide();
		console.log('<% out.print(session.getAttribute("Username")); %>');
		<%
			if (session.getAttribute("ImageOutput") != "") {
				imgPath = "./out.png";
				if (session.getAttribute("ImageOutput") == "Encoded") {
					out.print("$('#OriginalImg').show();" + '\n');
					out.print("$('#Download').show();" + '\n');
					out.print("alert('Data has been hidden.');" + '\n');
				} else if (session.getAttribute("ImageOutput") == "DecodedImage") {
					out.print("$('#OriginalImg').show();" + '\n');
					out.print("$('#Download').show();" + '\n');
					out.print("$('.zoom-buttons button').removeAttr('disabled');" + '\n');;
					out.print("alert('Data has been extracted.');");
				} else if (session.getAttribute("ImageOutput") == "DecodedText") {
					out.print("$('#text-out').show();" + '\n');
					out.print("alert('Data has been extracted.');");
				} else if (session.getAttribute("ImageOutput") == "None") {
					out.print("alert('No data detected in the image.');");
				} else if (session.getAttribute("ImageOutput") == "Error") {
					out.print("$('#OriginalImg').hide();" + '\n');
					out.print("alert('Steganography failed- Secret image is too large to encode within the cover image.');");
				}
			}
			if (session.getAttribute("Username") != "") {
				out.print("$('#Login').hide();" + '\n');
				out.print("$('#Logout').show();" + '\n');
			}
			session.setAttribute("ImageOutput", "");
		%>
	}
</script>
<title>Steganography App</title>
</head>
<body>
	<div class="header-wrap">
		<h1>Steganography Application</h1>
	</div>
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
		<div class="UserLogin" >
	
		<div class="login-container" id="Login">
			<form class="login form-inline my-2 my-lg-0" method="post"
				action="/Steganography/account_hdlr">
				<label for="Username">Username: </label> 
				<input type="text" class="form-control"
					name="Username" id="Username" placeholder="Username" required>

				<label for="Password">Password: </label> 
				<input type="password" class="form-control"
					name="Password" id="Password" placeholder="Password" required>
				<div class="LoginButtons">
				<input type="submit" class="form-control mr-sm-2" name="submit" placeholder="Create Account"
					value="Log In">
				<button type="button" class="form-control mr-sm-2"
					onclick="window.location.href = '/Steganography/register'">Register</button>
				</div>
			</form>
		</div>
		<div class="logout-container" id="Logout" style="display: none;">
		<form class="login form-inline my-2 my-lg-0">
			<label for="Logout">Signed in as <%=session.getAttribute("Username") %></label>
			<button type="button"  class="form-control mr-sm-2"
				onclick="window.location.href = '/Steganography/account_hdlr?logout=true'">Log Out</button>
				</form>
		</div>
		
	</div>
	</nav>

	<div class="table">
		<div class="content">

			<div class="sidebar">
				<div class="sidebar-container">
					<div class="Error Message">
						<%
							if (session.getAttribute("ErrorMessage") != null) {
								if (session.getAttribute("ErrorMessage") != "") {
									out.print("<p>" + session.getAttribute("ErrorMessage") + "</p>");
								}
							}
						%>
					</div>
					<form action="/Steganography/encode_hdlr" method="post" id="Form"
						enctype="multipart/form-data">
						<label for="UploadFile">Upload Image:</label> 
						<input type="file" class="form-control"
							name="UploadFile" id="UploadFile"
							accept="image/jpeg,image/jpg,image/png" data-max-size="5242880"
							required><br> 
						<label for="TextOrImage">SelectText or Image:</label> 
						<select name="TextOrImage" id="TextOrImage" class="form-control">
							<option value="text">Encode Text</option>
							<option value="image">Encode Image</option>
						</select><br>

						<div class="input-container">
							<div class="file-container" id="file-container">
								<label for="UploadToEnc">Upload Image to hide:</label> 
								<input class="form-control"
									type="file" name="UploadToEnc" id="UploadToEnc"
									accept="image/jpeg,image/jpg,image/png" data-max-size="5242880"><br>
								<div class="small-container">
									<div class="small-img">
										<img src="#" alt="Image failed to load" id="EncodeImg"
											style="display: none;">
									</div>
								</div>
							</div>

							<div class="text-container" id="text-container">
								<textarea name="TextToEnc" id="TextMessage" form="Form" cols="" class="form-control"
									rows="" pattern=".{1,500}" required>Enter text to encode here...</textarea>
								<br>
							</div>
						</div>

						<input type="submit" name="Encode" id="Encode" value="Encode" class="btn btn-primary">
						<input type="submit" name="Decode" id="Decode" value="Decode" class="btn btn-primary" disabled="disabled" onclick="$('#Form').attr('action','/Steganography/decode_hdlr');">
						<input type="reset" name="reset" id="Reset" class="btn btn-primary">
						<!-- <button type="button" id="Replay" disabled="disabled" class="btn btn-primary">Replay</button> -->
						
						<!-- <button type="button">Gallery</button> -->
						
					</form>
					<div class='log-container'>
						<iframe src="./log.txt"></iframe>
						<br>
						<button type="button" id="ClearLog" class="btn btn-secondary">Clear Log</button>
					</div>
				</div>
			</div>

			<div class="workspace">
				<div class="workspace-container">
					<div class="zoom-buttons">
						<button type="button" disabled="disabled" onclick="zoomIn();" class="btn btn-outline-primary">Zoom-in</button>
						<button type="button" disabled="disabled" onclick="zoomOut();" class="btn btn-outline-primary">Zoom-out</button>
						<a href="./out.png" id="Download" download='Download.png' style="display: none;" class="btn btn-link">Download</a> 
					</div>
					
					<div class="img-container">
						<img src="<%=imgPath%>" alt="" id="OriginalImg"
							style="display: none;">
						<!-- <img src="web/images/tmp/out.png" id="EncodedImg" hidden="true"></img> -->
					</div>
					<div class="text-out" id="text-out" style="display: none;">
						<textarea disabled='disabled'><%=session.getAttribute("Message")%></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
