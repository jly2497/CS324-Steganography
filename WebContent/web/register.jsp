<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Register</title>
<script>
	function validatePassword(input){
		if (input.value != document.getElementById('Password').value) {
            input.setCustomValidity('Password Does Not Match');
        } else {
            input.setCustomValidity('');
        }
	}
</script>
</head>
<body>
	<% 
		if (session.getAttribute("RegisterMessage") != null) {
			if (!session.getAttribute("RegisterMessage").equals(""))
				out.print("<p>" + session.getAttribute("RegisterMessage") + "</p>"); 
		}
	%>
	<form action="/Steganography/account_hdlr" method="post">
		<label for="Email">Email: </label>
		<input type="email" name="Email" id="Email" placeholder="Email" required><br>
	
		<label for="Username">Username: </label>
		<input type="text" name="Username" id="Username" placeholder="Username" pattern=".{1,16}" 
			title="Must be between 1 to 16 characters" required><br>
				 
		<label for="Password">Password: </label>
		<input type="password" name="Password" id="Password" placeholder="Password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,32}" 
			title="Must contain at least one number and one uppercase and lowercase letter, and be between 8 or 32 characters" required><br>
		
		<label for="ConfirmPassword">Confirm Password: </label>
		<input type="password" placeholder="Confirm Password" id="ConfirmPassword" oninput="validatePassword(this)" required><br>
		
		<span>
			<button type="button" onclick="window.location.href = '/Steganography';">Back</button>
			<input type="reset" name="reset" id="Reset">
			<input type="submit" name="submit" placeholder="Create Account" value="Register">
		</span>
	</form>
	
</body>
</html>