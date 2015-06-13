<%//********************************************************************************************************************
//
//  OCULUS WEB INTERFACE
//  INDEX PAGE
//
//********************************************************************************************************************%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Oculus</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />

    <%-- SESSION CHECK --%>
    <%
        if (session.getAttribute("loggedin") == null || session.getAttribute("loggedin").equals("")) {
            response.sendRedirect("login.jsp");
        }
    %>

    <%-- CSS CONTENT --%>
    <link href="css/default.css" rel="stylesheet" type="text/css" />
    <link href="css/jquery-ui.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/jquery-ui-timepicker-addon.css">
    <link rel="stylesheet" href="http://css-spinners.com/css/spinner/hexdots.css" type="text/css">
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.11.4/themes/cupertino/jquery-ui.css">

    <%-- IMAGE RESSOURCES --%>
    <link rel="shortcut icon" href="images/o_icon_trans.ico">

    <%-- JAVASCRIPT ADDONS --%>
    <script src="js/jquery.js"></script>
    <script src="js/jquery-ui.js"></script>
    <script src="js/jquery-tabs.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/jquery-ui-timepicker-addon.js"></script>
</head>
<body>

<%//********************************************************************************************************************
//  HEADER & TOPMENU (UNUSED)
//********************************************************************************************************************%>
<div id="header">
    <div id="topmenu">
        <ul>
            <li><a href="index.jsp" id="topmenu1" accesskey="1">Home</a></li>
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

<%//********************************************************************************************************************
//  MAIN CONTENT
//********************************************************************************************************************%>

<div id="content">
    <div id="main">
        <div id="welcome">

            <%//********************************************************************************************************
            //  TABVIEW
            //********************************************************************************************************%>
            <div id='MyTabSelector'>
                <ul>
                    <li><a href="#tabs-1">Current appointments</a></li>
                    <li><a href="#tabs-2">Choose appointment criterias</a></li>
                    <li><a href="#tabs-3">Confirmation</a></li>
                </ul>

                <%//****************************************************************************************************
                //  TAB 01: CURRENT APOINTMENT (DELETE APPOINTMENT)
                //****************************************************************************************************%>
                <div id="tabs-1">
                    <br/>
                    <div id="app_box">
                        <h3 id="title1"><strong>Appointment #1:</strong></h3>
                        <p>
                            <strong>Date:</strong><br/>
                            ${user.dateStart}
                            <br/>
                            <strong>Time:</strong><br/>
                            ${user.dateEnd}
                            <br/>
                            <strong>Description:</strong><br/>
                            ${user.description}
                        </p>
                        <button type="button" id="delete-appointment">Delete appointment</button>
                    </div>

                    <br/><br/>
                    <button type="button" id="activate-tabs">Activate all tabs (debug)</button>
                </div>

                <%//****************************************************************************************************
                //  TAB 02: NEW APPOINTMENT (SELECT SEARCH CRITERIAS)
                //****************************************************************************************************%>
                <div id="tabs-2">
                    <br/>
                    <div>
                        <form>
                            <table class="bordered" id="datetable">
                                <tr id="trid1">
                                    <td>
                                        <input id="option1" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="MON"/>
                                        <label class="checkbox" for="option1"> Monday </label>
                                    </td>
                                    <td>
                                        from <input type="text" style="width:120px;" class="timeywimey" name="time_picker_start" id="time_range_start1" value="" readonly/>
                                        to <input type="text" style="width:120px;" class="timeywimey" name="time_picker_end" id="time_range_end1" value="" readonly/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid2">
                                    <td>
                                        <input id="option2" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="TUE"/>
                                        <label class="checkbox" for="option2"> Tuesday </label>
                                    </td>
                                    <td>
                                        from <input type="text" style="width:120px;" class="timeywimey" name="time_picker_start" id="time_range_start2" value="" readonly/>
                                        to <input type="text" style="width:120px;" class="timeywimey" name="time_picker_end" id="time_range_end2" value="" readonly/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid3">
                                    <td>
                                        <input id="option3" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="WED"/>
                                        <label class="checkbox" for="option3"> Wednesday </label>
                                    </td>
                                    <td>
                                        from <input type="text" style="width:120px;" class="timeywimey" name="time_picker_start" id="time_range_start3" value="" readonly/>
                                        to <input type="text" style="width:120px;" class="timeywimey" name="time_picker_end" id="time_range_end3" value="" readonly/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid4">
                                    <td>
                                        <input id="option4" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="THU"/>
                                        <label class="checkbox" for="option4"> Thursday </label>
                                    </td>
                                    <td>
                                        from <input type="text" style="width:120px;" class="timeywimey" name="time_picker_start" id="time_range_start4" value="" readonly/>
                                        to <input type="text" style="width:120px;" class="timeywimey" name="time_picker_end" id="time_range_end4" value="" readonly/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid5">
                                    <td>
                                        <input id="option5" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="FRI"/>
                                        <label class="checkbox" for="option5"> Friday </label>
                                    </td>
                                    <td>
                                        from <input type="text" style="width:120px;" class="timeywimey" name="time_picker_start" id="time_range_start5" value="" readonly/>
                                        to <input type="text" style="width:120px;" class="timeywimey" name="time_picker_end" id="time_range_end5" value="" readonly/>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid6">
                                    <td>
                                        <input id="option6" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="SAT"/>
                                        <label class="checkbox" for="option6"> Saturday </label>
                                    </td>
                                    <td>
                                        from <input type="text" style="width:120px;" class="timeywimey" name="time_picker_start" id="time_range_start6" value="" readonly/>
                                        to <input type="text" style="width:120px;" class="timeywimey" name="time_picker_end" id="time_range_end6" value="" readonly/>
                                    </td>
                                </tr>
                            </table>
                            <br/>
                            <table class="bordered">
                                <tr>
                                    <td>
                                        <small>Choose unavailability date:</small><br/>
                                        <input id="optionrange" name="boxrange" type="checkbox" onclick='handleRangeClick(this);'/>
                                        <label class="checkbox" for="optionrange"></label>
                                        <input type="text" name="date_range_start" id="date_range_start" value="" readonly/>
                                    </td>
                                    <td>
                                        <small>Period (optional):</small><br/>
                                        <input type="text" name="date_range_end" id="date_range_end" value="" readonly/>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <br/>
                    <button id="check-appointments">Search free appointments</button>
                </div>

                <%//****************************************************************************************************
                //  TAB 03: APPOINTMENT SELECTION & CONFIRMATION
                //****************************************************************************************************%>
                <div id="tabs-3">
                    <br/>
                    <div id="app_box">
                        <h3><strong>Chosen appointment:</strong></h3>
                        <br/>
                        <strong>Selected date & time:</strong> <p id="new_date">Please select an appointment</p>
                        <strong>Reason(s) for your visit:</strong>
                        <textarea id="reasonTextarea" rows="7" cols="32" autofocus></textarea>
                        <br/><br/><br/>
                        <button type="button" id="cancel-appointment">Cancel appointment</button>
                        <br/><br/>
                        <button type="button" id="confirm-appointment">Confirm appointment</button>
                    </div>
                    <div id="app_box" style="position: absolute; top: 73px; left: 350px; width:400px; height:308px">
                        <h3 id="title2"><strong>Searching for free appointments...</strong></h3>
                        <br/>
                        <button type="button" id="retry-appointment">Try a new search</button>
                        <a class="selbutton" id="seldat1" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat2" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat3" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat4" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat5" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat6" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                    </div>
                    <div class="hexdots-loader" style="position: absolute; top: 190px; left: 500px;">
                        <p>Loading dates...</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%//****************************************************************************************************************
    //  SIDEBAR
    //****************************************************************************************************************%>
    <div id="sidebar">
        <div id="updates" class="boxed">
            <h2 class="title">USER INFO</h2>
            <div class="content">
                <ul>
                    <li>
                        <h3><strong>Logged in User:<br/></strong></h3>
                        <p>
                            <br/>
                            <strong>First Name:</strong><br/>
                            <p style="text-indent: 1em;"/>${user.firstName}
                            <br/>
                            <strong>Last Name:</strong><br/>
                            <p style="text-indent: 1em;"/>${user.lastName}
                            <br/>
                            <strong>SV Number:</strong><br/>
                            <p style="text-indent: 1em;"/>${user.svNumber}
                            <br/>
                            <strong>Doctor:</strong><br/>
                            <p style="text-indent: 1em;"/>${user.doctor}
                            <br/><br/>
                        </p>
                        <h3><strong>Next Appointment:<br/></strong></h3>
                        <c:choose>
                            <c:when test="${user.appointAvailable}">
                                <p>
                                    <br/>
                                    <strong>Date:</strong><br/>
                                    <p style="text-indent: 1em;"/>${user.dateStart}
                                    <br/>
                                    <strong>Time:</strong><br/>
                                    <p style="text-indent: 1em;"/>${user.dateEnd}
                                    <br/>
                                    <strong>Description:</strong><br/>
                                    <p style="text-indent: 1em;"/>${user.description}
                                </p>
                            </c:when>
                            <c:otherwise>
                                <p>None</p>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </ul>
                <br/><br/><br/>
                <button type="button" id="user-logout">Logout</button>
            </div>
        </div>

    </div>
</div>

<%//********************************************************************************************************************
//  FOOTER & MESSAGE BOX ELEMENTS
//********************************************************************************************************************%>

<div id="footer">
    <p id="legal">Copyright &copy; 2015 OCULUS Team F. All Rights Reserved. Designed by OCULUS Team F.</p>
</div>

<div id="dialog-message" title="Information">
    <p id="msgp"></p>
    <p></p>
</div>

</body>

<script>
    var times = [];
    var checkdays = [];
    var xhttpreq;
    var results = [];
    var selectedevent;

    //******************************************************************************************************************
    //  SCRIPT: PAGE LOAD INIT
    //******************************************************************************************************************

    $(document).ready(function(){
        refreshPage();
    });

    function refreshPage() {
        $(".hexdots-loader").hide();

        for (i = 1; i < 7; i++) {
            document.getElementById("time_range_start" + i).disabled = true;
            document.getElementById("time_range_end" + i).disabled = true;
            document.getElementById("seldat" + i).style.visibility = 'hidden';
        }

        document.getElementById("date_range_start").disabled = true;
        document.getElementById("date_range_end").disabled = true;

        document.getElementById("activate-tabs").style.visibility = 'hidden';
        document.getElementById("confirm-appointment").style.visibility = 'hidden';
        document.getElementById("retry-appointment").style.visibility = 'hidden';

        if (${user.appointAvailable}) {
            $('#MyTabSelector').enableTab(0);
            $('#MyTabSelector').disableTab(1);
            $('#MyTabSelector').disableTab(2);
        }
        else {
            $('#MyTabSelector').disableTab(0);
            $('#MyTabSelector').enableTab(1);
            $('#MyTabSelector').disableTab(2);
            $('#MyTabSelector').tabs( "option", "active", 1 );
        }

        for (i = 1; i < 7; i++) {
            var startTimeTextBox = $('#time_range_start' + i);
            var endTimeTextBox = $('#time_range_end' + i);

            $.timepicker.timeRange(
                    startTimeTextBox,
                    endTimeTextBox,
                    {
                        hourGrid: 2,
                        minuteGrid: 10,
                        minInterval: (1000 * 60 * 30), // 1hr
                        timeFormat: 'HH:mm',
                        stepMinute: 10,
                        hour: 7,
                        hourMin: 6,
                        hourMax: 18,
                        start: {}, // start picker options
                        end: {} // end picker options
                    }
            );
        }
    }

    //******************************************************************************************************************
    //  SCRIPT: DATE AND TIMEPICKER INIT
    //******************************************************************************************************************

    $('.timey').each(function(){
        $(this).timepicker({
            hourGrid: 3,
            minuteGrid: 10,
            timeFormat: 'HH:mm',
            stepMinute: 10,
            hour: 7,
            hourMin: 6,
            hourMax: 18
        });
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

    $('#MyTabSelector').tabs({
        heightStyle: 'fill'
    });

    //******************************************************************************************************************
    //  SCRIPT: CHECK APPOINTMENTS (INPUT VALIDATION)
    //******************************************************************************************************************

    $("#check-appointments").click(function(event){
        var testchecked = false;
        var inputcountA = 0;
        var inputcountB = 0;
        var rangechecked = false;
        var inputchecked = false;

        // check if ANY checkbox is ticked
        $("input:checkbox[name=boxdays]:checked").each(function() {
            testchecked = true;
            inputcountA++;
        });

        // check if every ticked checkbox has a date assigned
        $("input.timeywimey").each(function (index) {
            if ($(this).val() != "") {
                //alert($(this).val());
                inputcountB++;
            }
        });

        if ((inputcountA - (inputcountB / 2)) == 0){
            inputchecked = true;
        }

        // check dateperiod
        var rangeactive = document.getElementById("optionrange").checked;
        if (rangeactive ==  true) {
            var ds = $('#date_range_start').datepicker('getDate');
            var de = $('#date_range_end').datepicker('getDate');
            if (de != null > 0 && ds == null) {
                rangechecked = false;
            } else {
                rangechecked = true;
            }
        } else {
            rangechecked = true;
        }


        // if all checks are fine then send request
        if (inputchecked == true && rangechecked == true) {
            sendAppointmentRequest();
        } else {
            if (testchecked == false) {
                document.getElementById("msgp").innerHTML = "Please add at LEAST 1 weekday ! thx";
                showmessage();
            }
            if (inputchecked == false) {
                document.getElementById("msgp").innerHTML = "Please add " + (inputcountA - (inputcountB / 2)) + " missing time ranges";
                showmessage();
            }
            if (rangechecked == false) {
                document.getElementById("msgp").innerHTML = "Please check date range for proper values";
                showmessage();
            }

        }

    });

    function showmessage(){
        $( "#dialog-message" ).dialog({
            modal: true,
            buttons: {
                Ok: function() {
                    $( this ).dialog( "close" );
                }
            }
        });
    }

    function sendAppointmentRequest() {
        $("#check-appointments").hide();

        // get checkbox states
        $("input:checkbox[name=boxdays]:checked").each(function()
        {
            //alert($(this).val());
            checkdays.push($(this).val());
        });

        // get timebox states
        $("input.timeywimey").each(function (index) {
            if ($(this).val() != "") {
                //alert($(this).val());
                times.push($(this).val());
            }
        });

        // get daterange unavailable
        var dateRangeStart = $('#date_range_start').datepicker('getDate');
        var dateRangeEnd = $('#date_range_end').datepicker('getDate');

        var url = "RedirectServlet";
        var params = "dispatchto=checkappointments&checkdays=" + checkdays + "&checktimes=" + times + "&daterangestart=" + dateRangeStart + "&daterangeend=" + dateRangeEnd;

        xhttpreq = new XMLHttpRequest();
        if (!xhttpreq) {
            alert("Error: Could not init XMLHttpRequest");
            return;
        }

        xhttpreq.onreadystatechange = sendAppointmentRequest_callback;
        xhttpreq.open("POST", url + "?" + params, true);

        xhttpreq.setRequestHeader("Content-type", "text/xml");

        // switch tab and start waiting process
        $('#MyTabSelector').disableTab(0);
        $('#MyTabSelector').disableTab(1);
        $('#MyTabSelector').enableTab(2);
        $('#MyTabSelector').tabs( "option", "active", 2 );

        $(".hexdots-loader").show();

        xhttpreq.send();
    }

    function sendAppointmentRequest_callback() {
        if ((xhttpreq.readyState == 4) && (xhttpreq.status == 200)) {
            $(".hexdots-loader").hide();
            var resceivedresults = xhttpreq.responseText;

            results = resceivedresults.split(",");

            if (resceivedresults.toString().length == 0){
                document.getElementById("title2").innerHTML = "No free available date found";
                document.getElementById("retry-appointment").style.visibility = 'visible';
            } else {
                document.getElementById("title2").innerHTML = "Choose an appointment:";
            }

            for (i = 1; i < 7; i++) {
                if (results[i-1].length > 3) {
                    document.getElementById("seldat" + i).innerHTML = results[i-1].toString();
                    document.getElementById("seldat" + i).style.visibility = 'visible';
                }
            }
        }
    }

    //******************************************************************************************************************
    //  SCRIPT: CONFIRM APPOINTMENT
    //******************************************************************************************************************

    $("#confirm-appointment").click(function(event){
        $('#MyTabSelector').tabs( "option", "active", 0 );
        $('#MyTabSelector').enableTab(1);
        $('#MyTabSelector').disableTab(1);
        $('#MyTabSelector').disableTab(2);

        $("#confirm-appointment").hide();

        var reasontext = document.getElementById("reasonTextarea").value;
        var url = "RedirectServlet";
        var params = "dispatchto=confirmappointment&selevent=" + selectedevent + "&reason=" + reasontext;

        xhttpreq = new XMLHttpRequest();
        if (!xhttpreq) {
            alert("Error: Could not init XMLHttpRequest");
            return;
        }

        xhttpreq.onreadystatechange = confirmAppointmentRequest_callback;
        xhttpreq.open("POST", url + "?" + params, true);

        xhttpreq.setRequestHeader("Content-type", "text/xml");
        xhttpreq.send();
    });

    $("#cancel-appointment").click(function(event){
        location.reload();
    });

    $("#retry-appointment").click(function(event){
        location.reload();
    });

    function handleDateClick(bu) {
        selectedevent = bu.id.toString().substring(bu.id.toString().length - 1, bu.id.toString().length);
        document.getElementById("new_date").innerHTML = bu.innerHTML;
        document.getElementById("confirm-appointment").style.visibility = 'visible';
    };

    function confirmAppointmentRequest_callback() {
        if ((xhttpreq.readyState == 4) && (xhttpreq.status == 200)) {
            location.reload();
            document.getElementById("title1").innerHTML = "Confirmed Appointment:";
            document.getElementById("msgp").innerHTML = "Your selected appointment is confirmed. You will receive an email shortly.";
            showmessage();
        }
    };

    //******************************************************************************************************************
    //  SCRIPT: DELETE APPOINTMENT
    //******************************************************************************************************************

    $("#delete-appointment").click(function(event){

        var url = "RedirectServlet";
        var params = "dispatchto=deleteappointment";

        xhttpreq = new XMLHttpRequest();
        if (!xhttpreq) {
            alert("Error: Could not init XMLHttpRequest");
            return;
        }

        xhttpreq.onreadystatechange = deleteAppointmentRequest_callback;
        xhttpreq.open("POST", url + "?" + params, true);

        xhttpreq.setRequestHeader("Content-type", "text/xml");
        xhttpreq.send();
    });

    function deleteAppointmentRequest_callback() {
        if ((xhttpreq.readyState == 4) && (xhttpreq.status == 200)) {
            location.reload();
        }
    };


    //******************************************************************************************************************
    //  SCRIPT: WEEKDAY CHECKBOXES
    //******************************************************************************************************************

    function handleCheckClick(cb) {
        if (cb.checked == true) {
            document.getElementById("time_range_start" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).disabled = false;
            document.getElementById("time_range_end" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).disabled = false;
        } else {
            document.getElementById("time_range_start" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).value = "";
            document.getElementById("time_range_start" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).disabled = true;
            document.getElementById("time_range_end" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).value = "";
            document.getElementById("time_range_end" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).disabled = true;
        }
    }
    function handleRangeClick(cb) {
        if (cb.checked == true) {
            document.getElementById("date_range_start").disabled = false;
            document.getElementById("date_range_end").disabled = false;
        } else {
            document.getElementById("date_range_start").value = "";
            document.getElementById("date_range_end").value = "";
            document.getElementById("date_range_start").disabled = true;
            document.getElementById("date_range_end").disabled = true;
        }
    }

    //******************************************************************************************************************
    //  SCRIPT: LOGOUT
    //******************************************************************************************************************

    $("#user-logout").click(function(event){
        var url = "RedirectServlet";
        var params = "dispatchto=logoutuser";

        xhttpreq = new XMLHttpRequest();
        if (!xhttpreq) {
            alert("Error: Could not init XMLHttpRequest");
            return;
        }

        xhttpreq.onreadystatechange = logoutRequest_callback;
        xhttpreq.open("POST", url + "?" + params, true);

        xhttpreq.setRequestHeader("Content-type", "text/xml");
        xhttpreq.send();
    });

    function logoutRequest_callback() {
        if ((xhttpreq.readyState == 4) && (xhttpreq.status == 200)) {
            window.location.replace("login.jsp");
        }
    };

    //******************************************************************************************************************
    //  SCRIPT: DEBUG STUFF
    //******************************************************************************************************************

    $("#activate-tabs").click(function(event){
        $('#MyTabSelector').enableTab(0);
        $('#MyTabSelector').enableTab(1);
        $('#MyTabSelector').enableTab(2);
    });

</script>
</html>
