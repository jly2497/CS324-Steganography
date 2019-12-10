<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, java.io.*"%>
<%
	String LogIn = "";
	String LogOut = "hidden";
	String imgPath = "#";
	if (session != null)
		if (session.getAttribute("Username") != null) {
			LogIn = "hidden";
			LogOut = "";
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
<link rel="stylesheet" type="text/css" href="web/style/bootstrap.min.css">
<script src="web/scripts/jquery-3.4.1.min.js" type="text/javascript"></script>
<script src="web/scripts/script.js" type="text/javascript"></script>
<script type="text/javascript">
		
		window.onload = function() {
			$('#text-out').hide();
			$('#OriginalImg').hide();
			<%
			out.print("console.log('" + session.getAttribute("ImageOutput") + "');");
				if (session.getAttribute("ImageOutput") != "") {
					imgPath = "./out.png";
					if (session.getAttribute("ImageOutput") == "Encoded") {
						out.print("$('#OriginalImg').show();" + '\n');
						out.print("alert('Data has been hidden.');" + '\n');
					} else if (session.getAttribute("ImageOutput") == "DecodeImage") {
						out.print("$('#OriginalImg').show();" + '\n');
						out.print("alert('Data has been extracted.');");
						
					} else if (session.getAttribute("ImageOutput") == "DecodeText") {
						out.print("$('#OriginalImg').hide();" + '\n');
						out.print("$('#text-out').show();" + '\n');
						out.print("alert('Data has been extracted.');");
					} else if (session.getAttribute("ImageOutput") == "None") {
						out.print("$('#OriginalImg').hide();" + '\n');
						out.print("alert('No data detected in the image.');");
					}
				}
			session.setAttribute("ImageOutput",""); 
			%>
		}
</script>
<title>Steganography App</title>
</head>
<body>
	<div class="header-wrap">
		<h1>Steganography Application</h1>
	</div>
	<div class="UserLogin" >
		<div class="login-container" <%=LogIn%>>
			<form class="login" method="post"
				action="/Steganography/account_hdlr">
				<label for="Username">Username: </label> <input type="text"
					name="Username" id="Username" placeholder="Username" required>

				<label for="Password">Password: </label> <input type="password"
					name="Password" id="Password" placeholder="Password" required>

				<input type="submit" name="sign-in" placeholder="Create Account" value="Log In">
				<button type="button"
					onclick="window.location.href = '/Steganography/register'">Register</button>
			</form>
		</div>
		<div class="logout-container">
			<button type="button" onclick="window.location.href = '/Steganography/account_hdlr?logout=true'" <%=LogOut%>>Log Out</button>
		</div>
	</div>
	

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
						<label for="UploadFile">Upload Image:</label> <input type="file"
							name="UploadFile" id="UploadFile"
							accept="image/jpeg,image/jpg,image/png" data-max-size="5242880"
							required><br> <label for="TextOrImage">Select
							Text or Image:</label> <select name="TextOrImage" id="TextOrImage">
							<option value="text">Encode Text</option>
							<option value="image">Encode Image</option>
						</select><br>

						<div class="input-container">
							<div class="file-container" id="file-container">
								<label for="UploadToEnc">Upload Image to hide:</label> <input
									type="file" name="UploadToEnc" id="UploadToEnc"
									accept="image/jpeg,image/jpg,image/png" data-max-size="5242880"><br>
								<div class="small-container">
									<div class="small-img">
										<img src="#" alt="Image failed to load" id="EncodeImg"
											hidden="true">
									</div>
								</div>
							</div>

							<div class="text-container" id="text-container">
								<textarea name="TextToEnc" id="TextMessage" form="Form" cols=""
									rows="" pattern=".{1,500}" required>Enter text to encode here...</textarea>
								<br>
							</div>
						</div>

						<input type="submit" name="Encode" id="Encode" value="Encode">
						<input type="submit" name="Decode" id="Decode" value="Decode" disabled="disabled" onclick="$('#Form').attr('action','/Steganography/decode_hdlr');">
						<input type="reset"name="reset" id="Reset">
						<button type="button">Replay</button>
						<button type="button">Download</button>
						<button type="button">Gallery</button>
					</form>
					<div class='log-container'>
						<textarea disabled="disabled"></textarea><br>
						<button type="button" id="ClearLog">Clear Log</button>
					</div>
				</div>
			</div>


			<div class="workspace">
				<div class="workspace-container">
					<div class="zoom-buttons">
						<button type="button" disabled="disabled">Zoom-in</button>
						<button type="button" disabled="disabled">Zoom-out</button>
					</div>
					<div class="img-container">
						<img src="<%=imgPath%>" alt="" id="OriginalImg">
						<!-- <img src="web/images/tmp/out.png" id="EncodedImg" hidden="true"></img> -->
					</div>
					<div class="text-out" id="text-out"><p><%=session.getAttribute("Message") %></p></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
