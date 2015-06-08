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
            <li><a href="index.jsp" id="topmenu1" accesskey="1">Home</a></li>
            <li><a href="contact.jsp" id="topmenu2" accesskey="2">Contact</a></li>
            <li><a href="sitemap.jsp" id="topmenu3" accesskey="3">Sitemap</a></li>
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
                    <li><a href="#tabs-1">Current appointments</a></li>
                    <li><a href="#tabs-2">New appointment</a></li>
                    <li><a href="#tabs-3">Confirmation</a></li>
                </ul>
                <div id="tabs-1">
                    <br/>
                    <div id="app_box">
                        <h3><strong>Appointment #1:</strong></h3>
                    <p>

                        Date start: ${user.dateStart}
                        <br/>
                        Date end: ${user.dateEnd}
                        <br/>
                        Description: ${user.description}
                    </p>
                        <button type="button" id="delete-appointment">Delete appointment</button>
                    </div>

                    <br/><br/>
                    <button type="button" id="activate-tabs">Activate all tabs (debug)</button>
                </div>

                <div id="tabs-2">
                    <div id="picker">
                        <form>
                            <table>
                                <tr>
                                    <td>
                                        <input id="option1" type="checkbox">
                                        <label class="checkbox" for="option1"> Monday </label>
                                    </td>
                                    <td>
                                        <input type="text" name="time_picker1" id="time_picker1" value="" />
                                        <input type="image" name="option1button" id="option1button" style="height:20px; width:20px;" src="images/success.png"/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr>
                                    <td>
                                        <input id="option2" type="checkbox" >
                                        <label class="checkbox" for="option2"> Tuesday </label>
                                    </td>
                                    <td>
                                        <input type="text" name="time_picker2" id="time_picker2" value="" />
                                        <input type="image" name="option2button" id="option2button" style="height:20px; width:20px;" src="images/success.png"/>
                                    </td>
                                </tr>
                                <tr></tr>

                                <tr>
                                    <td>
                                        <input id="option3" type="checkbox" >
                                        <label class="checkbox" for="option3"> Wednesday </label>
                                    </td>
                                    <td>
                                        <input type="text" name="time_picker3" id="time_picker3" value="" />
                                        <input type="image" name="option3button" id="option3button" style="height:20px; width:20px;" src="images/success.png"/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr>
                                    <td>
                                        <input id="option4" type="checkbox" >
                                        <label class="checkbox" for="option4"> Thursday </label>
                                    </td>
                                    <td>
                                        <input type="text" name="time_picker4" id="time_picker4" value="" />
                                        <input type="image" name="option4button" id="option4button" style="height:20px; width:20px;" src="images/success.png"/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr>
                                    <td>
                                        <input id="option5" type="checkbox" >
                                        <label class="checkbox" for="option5"> Friday </label>
                                    </td>
                                    <td>
                                        <input type="text" name="time_picker5" id="time_picker5" value="" />
                                        <input type="image" name="option5button" id="option5button" style="height:20px; width:20px;" src="images/success.png"/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr>
                                    <td>
                                        <input id="option6" type="checkbox" >
                                        <label class="checkbox" for="option6"> Saturday </label>
                                    </td>
                                    <td>
                                        <input type="text" name="time_picker6" id="time_picker6" value="" />
                                        <input type="image" name="option6button" id="option6button" style="height:20px; width:20px;" src="images/success.png"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td><br/></td>
                                    <td><br/></td>
                                </tr>
                                <tr>
                                    <td>
                                        Unavailability
                                    </td>
                                    <td>
                                        Period (optional)
                                    </td>
                                <tr/>
                                <tr>
                                    <td>
                                        <input type="text" name="date_range_start" id="date_range_start" value="" />
                                    </td>
                                    <td>
                                        <input type="text" name="date_range_end" id="date_range_end" value="" />
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div>
                        <ul id="appoint-list">
                        </ul>
                        <br/><br/>
                        <button id="check-appointments">check appointments</button>
                    </div>
                </div>
                <div id="tabs-3">
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
    var times = [];
    var checkdays = [];
    var xhttpreq;
    var maxHour = 18;
    var minHour = 6;
    var startHour = 7;
    var hourGrid = 2;

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
        }
        else {
            $('#MyTabSelector').disableTab(0, true);
            $('#MyTabSelector').enableTab(1);
            $('#MyTabSelector').disableTab(2);
        }
    });

    $("#activate-tabs").click(function(event){
        $('#MyTabSelector').enableTab(0);
        $('#MyTabSelector').enableTab(1);
        $('#MyTabSelector').enableTab(2);
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

        $("input:checkbox[name=boxdays]:checked").each(function()
        {
            alert($(this).val());
            checkdays.push($(this).val());
        });

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
        $('#MyTabSelector').enableTab(2);*/
    });

    $('#time_picker1').timepicker({
        hourGrid: hourGrid,
        minuteGrid: 10,
        timeFormat: 'HH:mm',
        stepMinute: 10,
        hour: startHour,
        hourMin: minHour,
        hourMax: maxHour
    });

    $('#time_picker2').timepicker({
        hourGrid: hourGrid,
        minuteGrid: 10,
        timeFormat: 'HH:mm',
        stepMinute: 10,
        hour: startHour,
        hourMin: minHour,
        hourMax: maxHour
    });

    $('#time_picker3').timepicker({
        hourGrid: hourGrid,
        minuteGrid: 10,
        timeFormat: 'HH:mm',
        stepMinute: 10,
        hour: startHour,
        hourMin: minHour,
        hourMax: maxHour
    });

    $('#time_picker4').timepicker({
        hourGrid: hourGrid,
        minuteGrid: 10,
        timeFormat: 'HH:mm',
        stepMinute: 10,
        hour: startHour,
        hourMin: minHour,
        hourMax: maxHour
    });

    $('#time_picker5').timepicker({
        hourGrid: hourGrid,
        minuteGrid: 10,
        timeFormat: 'HH:mm',
        stepMinute: 10,
        hour: startHour,
        hourMin: minHour,
        hourMax: maxHour
    });

    $('#time_picker6').timepicker({
        hourGrid: hourGrid,
        minuteGrid: 10,
        timeFormat: 'HH:mm',
        stepMinute: 10,
        hour: startHour,
        hourMin: minHour,
        hourMax: maxHour
    });

    var startDateTextBox = $('#date_range_start');
    var endDateTextBox = $('#date_range_end');

    $.timepicker.dateRange(
            startDateTextBox,
            endDateTextBox,
            {
                minInterval: (1000*60*60*24*1), // 1 day
                maxInterval: (1000*60*60*24*60), // 60 days
                start: {}, // start picker options
                end: {} // end picker options
            }
    );

</script>
</html>
