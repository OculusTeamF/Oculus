<%--
  Created by IntelliJ IDEA.
  User: Fabian
  Date: 31.05.2015
  Time: 14:58
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/header.css"/>
    <link href="css/jquery-ui.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="css/jquery.weekSchedulerWidget.css">
    <title></title>
</head>
<body>

<div class="header_outherHeader">

</div>

<br>

<div class="wrapper">
    <div class="mainBox">
        <!-- Tab -->
        <div id="tabs">
            <ul>
                <li><a href="#tabs-1">First</a></li>
                <li><a href="#tabs-2">Second</a></li>
                <li><a href="#tabs-3">Third</a></li>
            </ul>
            <div id="tabs-1">
                <div class="userInfo">
                    <strong>Logged in User:  <br /></strong><br>
                    First Name: ${user.firstName}
                    <br>
                    Last Name: ${user.lastName}
                    <br>
                    SV Number: ${user.svNumber}
                </div>
            </div>
            <div id="tabs-2">
                <div id="widget"><div>
            </div>
            <div id="tabs-3">
                <!-- Slider -->
                <h2 class="demoHeaders">Slider</h2>
                <div id="slider"></div>
            </div>
        </div>


    </div>
</body>

<script src="js/jquery.js"></script>
<script src="js/jquery-ui.js"></script>
<script src="js/jquery-ui.min.js"></script>

<script src="js/jquery.weekSchedulerWidget.js"></script>

<script>
    $( "#tabs" ).tabs();

    $( "#slider" ).slider({
        range: true,
        values: [ 17, 67 ]
    });

    // initialize the widget
    $("#widget").weekSchedulerWidget({
        startDate: new Date(),
        endDate: new Date(2016, 12, 5)
    });

    // do something when the user selects the dates
    $("#widget").on("onConfirm.weekSchedulerWidget", function () {
        var $widget = $(this);

        // hide widget
        $widget.weekSchedulerWidget('hide');

        // get selected dates
        var selectedDates = $widget.weekSchedulerWidget('getSelectedDates');

        // do something...
    });
</script>

</html>
