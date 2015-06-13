<%//********************************************************************************************************************
//
//  OCULUS WEB INTERFACE
//  LOGIN PAGE
//
//********************************************************************************************************************%>

<!DOCTYPE html>
<html >
<head>
	<meta charset="UTF-8">
	<title>Oculus Login</title>
    <%-- CSS CONTENT --%>
	<link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="http://css-spinners.com/css/spinner/hexdots.css" type="text/css">
    <%-- IMAGE RESSOURCES --%>
    <link rel="shortcut icon" href="images/o_icon_trans.ico">
</head>
<body>

<div class="wrapper">
    <%//********************************************************************************************************
    //  LOGIN
    //********************************************************************************************************%>
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

    <%//********************************************************************************************************
    //  BUBBLES BACKGROUND
    //********************************************************************************************************%>
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

<%-- JAVASCRIPT ADDONS --%>
<script src="js/jquery.js"></script>
<script src="js/jquery-ui.js"></script>

<script>
    //******************************************************************************************************************
    //  SCRIPT: PAGE INIT
    //******************************************************************************************************************
    $(document).ready(function(){
        $(".hexdots-loader").hide();
    });

    //******************************************************************************************************************
    //  SCRIPT: LOGIN BUTTON
    //******************************************************************************************************************
    $("#login-button").click(function(event){
        $(".hexdots-loader").show();
        $('form').fadeOut(500);
        $('.wrapper').addClass('form-success');
    });
</script>
</body>
</html>