<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Oculus</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link href="css/default.css" rel="stylesheet" type="text/css" />

    <link rel="shortcut icon" href="images/o_icon_trans.ico">

    <link href="css/jquery-ui.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/jquery-ui-timepicker-addon.css">

    <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/cupertino/jquery-ui.css">
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
        <h1><a href="index.jsp">Oculus Appointment Creator</a></h1>
    </div>
</div>
<div id="menu">
</div>
<div id="content">
    <div id="main">
        <div id="welcome">
            <div id='MyTabSelector'>
                <ul>
                    <li><a href="#tabs-1">Current appointment</a></li>
                    <li><a href="#tabs-2">New appointment</a></li>
                    <li><a href="#tabs-3">Available appointments</a></li>
                    <li><a href="#tabs-4">Confirmation</a></li>
                </ul>
                <div id="tabs-1">
                    <p>
                        <br/>
                        Date start: ${user.dateStart}
                        <br/>
                        Date end: ${user.dateEnd}
                        <br/>
                        Description: ${user.description}
                    </p>
                    <button type="button" id="delete-appointment">Delete appointment</button>
                    <br/><br/>
                    <button type="button" id="activate-tabs">Activate all tabs (debug)</button>
                </div>
                <div id="tabs-2">

                        <div id="picker">
                            <br/>
                            <div id="choose_date"></div>
                            <!--
                            <p>Choose Endtime: (optional)</p>
                            <input type="text" name="choose_time" id="choose_time" value="" />
                            -->
                            <br/><br/>
                            <button type="button" id="add-time">add time</button>
                        </div>

                        <div id="alist">
                            <ul id="appoint-list">
                            </ul>
                            <br/><br/>
                            <button id="check-appointments">check appointments</button>
                        </div>

                </div>
                <div id="tabs-3">
                    [not used -> remove later]
                </div>
                <div id="tabs-4">
                    [confirm date tab]
                    <br/><br/>
                    <button type="button" id="confirm-appointment">Confirm</button>
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
                            <strong>First Name:</strong> ${user.firstName}
                            <br/>
                            <strong>Last Name:</strong> ${user.lastName}
                            <br/>
                            <strong>SV Number:</strong> ${user.svNumber}
                            <br/>
                            <strong>Doctor:</strong> ${user.doctor}
                            <br/><br/>
                        </p>
                        <h3><strong>Current Appointment:<br/></strong></h3>
                        <c:choose>
                            <c:when test="${user.appointAvailable}">

                                <p>
                                    <br/>
                                    <strong>Date start:</strong> ${user.dateStart}
                                    <br/>
                                    <strong>Date end:</strong> ${user.dateEnd}
                                    <br/>
                                    <strong>Description:</strong> ${user.description}
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

<script src="js/jquery-tabs.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/jquery-ui-timepicker-addon.js"></script>

<script>

    var dates = [];
    var xhttpreq;

    $('#MyTabSelector').tabs({
        heightStyle: 'fill'
    });

    $('#choose_date').datetimepicker({
        minDate: 0,
        showButtonPanel:false,
        hourGrid: 4,
        minuteGrid: 10,
        timeFormat: 'HH:mm',
        stepMinute: 10
    });


    $(document).ready(function(){

        $(".hexdots-loader").hide();
        document.getElementById('check-appointments').style.visibility = 'hidden';

        if (${user.appointAvailable}) {
            $('#MyTabSelector').enableTab(0);
            $('#MyTabSelector').disableTab(1);
            $('#MyTabSelector').disableTab(2);
            $('#MyTabSelector').disableTab(3);
        }
        else {
            $('#MyTabSelector').disableTab(0, true);
            $('#MyTabSelector').enableTab(1);
            $('#MyTabSelector').disableTab(2);
            $('#MyTabSelector').disableTab(3);
        }
    });

    $("#activate-tabs").click(function(event){
        $('#MyTabSelector').enableTab(0);
        $('#MyTabSelector').enableTab(1);
        $('#MyTabSelector').enableTab(2);
        $('#MyTabSelector').enableTab(3);
    });

    $("#add-time").click(function(event) {
        if (dates.length < 7) {
            document.getElementById('check-appointments').style.visibility = 'visible';

            var newDateAsObject = $('#choose_date').datepicker('getDate');
            var newDateAsString = $('#choose_date').datepicker({dateFormat: 'dd,MM,yyyy'}).val();
            $('#appoint-list').append('<a href="#" class="myButton" id="list' + dates.length + '">' + newDateAsString + '</a>');
            dates[dates.length] = newDateAsObject;
        } else {
            alert('Please check selected dates before adding more... ')
        }
    });

    function sendAppointmentRequest() {
        $("#check-appointments").hide();
        //$(".hexdots-loader").show();

        var url = "RedirectServlet";
        var params = "dispatchto=checkappointments&datearray=" + dates;

        xhttpreq = new XMLHttpRequest();
        if (!xhttpreq) {
            alert("Error: Could not init XMLHttpRequest");
            return;
        }

        xhttpreq.onreadystatechange = sendAppointmentRequest_callback;
        xhttpreq.open("POST", url + "?" + params, true);

        xhttpreq.setRequestHeader("Content-type", "text/xml");
        xhttpreq.send();
    }

    function sendAppointmentRequest_callback() {
        if ((xhttpreq.readyState == 4) && (xhttpreq.status == 200)) {
            alert(xhttpreq.responseText);
            /* TODO change to XML
            var countedChars = xhttpreq.responseXML.getElementsByTagName("number")[0].firstChild.nodeValue;
            alert("PASST");
            document.getElementById("countedchars").value = countedChars;*/

            /* Modify result list */
            for (var i = 0; dates.length; i++){
                document.getElementById("list" + i).style.color = "green";
                document.getElementById("list" + i).onclick = function () {
                    alert('foo'); // link to confirmation
                };
            }
        }
    }

    $("#confirm-appointment").click(function(event){
        $('#MyTabSelector').enableTab(0);
        $('#MyTabSelector').disableTab(1, true);
        $('#MyTabSelector').disableTab(2, true);
        $('#MyTabSelector').disableTab(3, true);
        $("#confirm-appointment").hide();
    });

    $("#delete-appointment").click(function(event){
       /*
        $('#MyTabSelector').disableTab(0, true);
        $('#MyTabSelector').enableTab(1);*/
    });

    $("#check-appointments").click(function(event){
        sendAppointmentRequest();
    /*
        $('#MyTabSelector').disableTab(0, true);
        $('#MyTabSelector').disableTab(1, true);
        $('#MyTabSelector').enableTab(2);
        $('#MyTabSelector').disableTab(3, true);*/
    });
</script>
</html>
