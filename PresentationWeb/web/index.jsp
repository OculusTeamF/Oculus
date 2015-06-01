<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Oculus</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link href="css/default.css" rel="stylesheet" type="text/css" />

    <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
</head>
<body>
<div id="header">
    <div id="topmenu">
        <ul>
            <li><a href="#" id="topmenu1" accesskey="1">Home</a></li>
            <li><a href="#" id="topmenu2" accesskey="2">Contact</a></li>
            <li><a href="#" id="topmenu3" accesskey="3">Sitemap</a></li>
        </ul>
    </div>
    <div id="logo">
        <h1><a href="#">Oculus Appointment Creator</a></h1>
        <h2><a href="#"> </a></h2>
    </div>
</div>
<div id="menu">
</div>
<div id="content">
    <div id="main">
        <div id="welcome">
            <div id="tabs">
                <ul>
                    <li><a href="#tabs-1">Actual appointment</a></li>
                    <li><a href="#tabs-2">New appointment</a></li>
                    <li><a href="#tabs-3">Available appointments</a></li>
                    <li><a href="#tabs-4">Confirmation</a></li>
                </ul>
                <div id="tabs-1">
                    <a href="#" onclick="$('#tabs').disableTab(1);">Disable Tab 1</a><br />
                    <a href="#" onclick="$('#tabs').disableTab(1, true);">Hide Tab 1</a>
                    <br />
                    <a href="#" onclick="$('#tabs').enableTab(1);">Show/Enable Tab 1</a>
                </div>
                <div id="tabs-2">
                    lalala
                </div>
                <div id="tabs-3">
                    <div id="widget"></div>
                </div>
                <div id="tabs-4">
                    Termin best&auml;tigen
                </div>
            </div>
        </div>
    </div>
    <div id="sidebar">
        <div id="updates" class="boxed">
            <h2 class="title">USER INFO</h2>
            <div class="content">
                <ul>
                    <li>
                        <h3><strong>Logged in User:<br/></strong></h3>
                        <p>
                            <br/>
                            First Name: ${user.firstName}
                            <br/>
                            Last Name: ${user.lastName}
                            <br/>
                            SV Number: ${user.svNumber}
                            <br/><br/>
                        </p>
                        <h3><strong>Appointment:<br/></strong></h3>
                        <c:choose>
                            <c:when test="${user.appointAvailable}">

                                <p>
                                    <br/>
                                    Date start: ${user.dateStart}
                                    <br/>
                                    Date end: ${user.dateEnd}
                                    <br/>
                                    Description: ${user.description}
                                </p>
                            </c:when>
                        </c:choose>
                    </li>
                </ul>
            </div>
        </div>

    </div>
</div>
<div id="footer">
    <p id="legal">Copyright &copy; 2015 OCULUS Team F. All Rights Reserved. Designed by OCULUS Team F.</p>
</div>
</body>

<script src="js/jquery.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/jquery-ui.min.js"></script>

<script src="js/Query-ui-tab-utils.js"></script>

<script>
    $( "#tabs" ).tabs();

</script>
</html>
