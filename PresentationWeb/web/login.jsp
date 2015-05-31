<%--
    Document   : user_login
    Created on : 24.05.2015, 21:36:14
    Author     : Fabian
--%>

<!DOCTYPE html>
<html >
<head>
	<meta charset="UTF-8">
	<title>Oculus Login</title>
	<link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="wrapper">
	<div class="container">
		<h1>Welcome to Oculus</h1>
		<form class="form" method="POST" action="RedirectServlet?dispatchto=login">
            <input type="text" placeholder="Email" name="loginemail" id="loginemail">
            <input type="password" placeholder="Password" name="loginpw" id="loginpw">
            <button type="submit" value="UserLogin">Login</button>
		</form>
	</div>
	<div class="bg-bubbles">
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
        <li></li>
	</div>
</div>
<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src="js/login.js"></script>
</body>
</html>