<!DOCTYPE html>
<%@ page import = "java.util.*, java.io.*" %>
<%
	out.print(session.getCreationTime());
%>
<html>
<head>
<meta charset="UTF-8">
<title>Encoder</title>
</head>
<body>
	<% 
		if (session.getAttribute("ErrorMessage") != null) {
			if (!session.getAttribute("ErrorMessage").equals(""))  {
				out.print("<p>" + session.getAttribute("ErrorMessage") + "</p>"); 
			} else { out.print("<p>Error Message is blank</p>"); }
		} else { out.print("<p>Error Message is null</p>"); }
	%>
	<form action="./Encode" id="Form" method="post" enctype="multipart/form-data">
		<input type="file" name="UploadFile" accept="image/jpeg,image/jpg,image/png"><br>
		
		<select name="TextOrImage">
			<option value="text">Encode Text</option>
			<option value="image">Encode Image</option>
		</select><br>
		
		<textarea name="TextToEnc">Enter text to hide...</textarea><br>
		<select name="TextTechnique">
			<option value="A">Technique 1</option>
			<option value="B">Technique 2</option>
			<option value="C">Technique 3</option>
		</select><br>
		
		<input type="file" name="UploadToEnc" accept="image/jpeg,image/jpg,image/png"><br>
		<select name="ImageTechnique">
			<option value="A">Technique 1</option>
			<option value="B">Technique 2</option>
			<option value="C">Technique 3</option>
		</select><br>
		
		<input type="submit">
	</form>
</body>
</html>