<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Oculus</title>
  <meta http-equiv="content-type" content="text/html; charset=utf-8" />
  <link href="css/default.css" rel="stylesheet" type="text/css" />

    <link href="css/jquery-ui.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="css/jquery.weekSchedulerWidget.css">
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
                <li><a href="#tabs-1">First</a></li>
                <li><a href="#tabs-2">Second</a></li>
                <li><a href="#tabs-3">Third</a></li>
            </ul>
            <div id="tabs-1">
                <div id="widget"></div>
            </div>
            <div id="tabs-2">
                lol 2
            </div>
            <div id="tabs-3">
                lol 3
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
            <h3><strong>Logged in User:  <br /></strong></h3>
            <p>
              <br>
              First Name: ${user.firstName}
              <br>
              Last Name: ${user.lastName}
              <br>
              SV Number: ${user.svNumber}
          </p>
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
