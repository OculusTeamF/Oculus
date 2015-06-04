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
    <link rel="stylesheet" href="http://css-spinners.com/css/spinner/hexdots.css" type="text/css">
    <link rel="shortcut icon" href="images/o_icon_trans.ico">
</head>
<body>
<div class="wrapper">
	<div class="container">
		<h1>Welcome to Oculus</h1>
		<form class="form" method="POST" action="RedirectServlet?dispatchto=login">
            <input type="text" placeholder="Email" name="loginemail" id="loginemail">
            <input type="password" placeholder="Password" name="loginpw" id="loginpw">
            <button type="submit" id="login-button" value="UserLogin">Login</button>
		</form>
        <div class="hexdots-loader">
            <p>Login...</p>
        </div>
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
<script src="js/jquery.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

<script>
    $("#login-button").click(function(event){
        $(".hexdots-loader").show();
        $('form').fadeOut(500);
        $('.wrapper').addClass('form-success');
    });

    $(document).ready(function(){
        $(".hexdots-loader").hide();
    });
</script>
</body>
</html>