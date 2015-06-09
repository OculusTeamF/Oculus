<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Oculus</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link href="css/default.css" rel="stylesheet" type="text/css" />

    <link rel="shortcut icon" href="images/o_icon_trans.ico">

    <link href="css/jquery-ui.css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-ui-timepicker-addon/1.4.5/jquery-ui-timepicker-addon.css">
    <link rel="stylesheet" href="http://css-spinners.com/css/spinner/hexdots.css" type="text/css">

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
                    <li><a href="#tabs-2">Choose appointment criterias</a></li>
                    <li><a href="#tabs-3">Confirmation</a></li>
                </ul>
                <div id="tabs-1">
                    <br/>
                    <div id="app_box">
                        <h3 id="title1"><strong>Appointment #1:</strong></h3>
                        <p>

                            <strong>Date start:</strong> ${user.dateStart}
                            <br/>
                            <strong>Date end:</strong> ${user.dateEnd}
                            <br/>
                            <strong>Description:</strong> ${user.description}
                        </p>
                        <button type="button" id="delete-appointment">Delete appointment</button>
                    </div>

                    <br/><br/>
                    <button type="button" id="activate-tabs">Activate all tabs (debug)</button>
                </div>

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
                                        <input type="text" class="timey" name="time_picker" id="time_picker1" value="" readonly/>
                                        <%--<a class="selbutton" id="seldate1" href="#">select date</a>--%>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid2">
                                    <td>
                                        <input id="option2" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="TUE"/>
                                        <label class="checkbox" for="option2"> Tuesday </label>
                                    </td>
                                    <td>
                                        <input type="text" class="timey" name="time_picker" id="time_picker2" value="" readonly/>
                                        <%--<a class="selbutton" id="seldate2" href="#">select date</a>--%>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid3">
                                    <td>
                                        <input id="option3" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="WED"/>
                                        <label class="checkbox" for="option3"> Wednesday </label>
                                    </td>
                                    <td>
                                        <input type="text" class="timey" name="time_picker" id="time_picker3" value="" readonly/>
                                        <%--<a class="selbutton" id="seldate3" href="#">select date</a>--%>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid4">
                                    <td>
                                        <input id="option4" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="THU"/>
                                        <label class="checkbox" for="option4"> Thursday </label>
                                    </td>
                                    <td>
                                        <input type="text" class="timey" name="time_picker" id="time_picker4" value="" readonly/>
                                        <%--<a class="selbutton" id="seldate4" href="#">select date</a>--%>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid5">
                                    <td>
                                        <input id="option5" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="FRI"/>
                                        <label class="checkbox" for="option5"> Friday </label>
                                    </td>
                                    <td>
                                        <input type="text" class="timey" name="time_picker" id="time_picker5" value="" readonly/>
                                        <%--<a class="selbutton" id="seldate5" href="#">select date</a>--%>
                                    </td>
                                </tr>
                                <tr></tr>
                                <tr id="trid6">
                                    <td>
                                        <input id="option6" name="boxdays" type="checkbox" onclick='handleCheckClick(this);' value="SAT"/>
                                        <label class="checkbox" for="option6"> Saturday </label>
                                    </td>
                                    <td>
                                        <input type="text" class="timey"  name="time_picker" id="time_picker6" value="" readonly/>
                                        <%--<a class="selbutton" id="seldate6" href="#">select date</a>--%>
                                    </td>
                                </tr>
                            </table>
                            <br/>
                            <table class="bordered">
                                <tr>
                                    <td>
                                        <small>Choose unavailability date:</small><br/>
                                        <input id="optionrange" name="boxdays" type="checkbox" onclick='handleRangeClick(this);'/>
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
                    <button id="check-appointments">Check criterias</button>
                </div>
                <div id="tabs-3">
                    <br/>
                    <div id="app_box">
                        <h3><strong>Chosen appointment:</strong></h3>

                        <strong>Date start:</strong> <p id="new_datestart"></p>
                        <strong>Date end:</strong> <p id="new_dateend"></p>
                        <strong>Reason(s) for your visit:</strong>
                        <textarea id="reasonTextarea" rows="7" cols="32" autofocus></textarea>
                        <br/><br/>
                        <button type="button" id="cancel-appointment">Cancel appointment</button>
                        <br/><br/>
                        <button type="button" id="confirm-appointment">Confirm appointment</button>
                    </div>
                    <div id="app_box" style="position: absolute; top: 73px; left: 350px; width:400px">
                        <h3><strong>Choose appointment:</strong></h3>
                        <br/>
                        <a class="selbutton" id="seldat1" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat2" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat3" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat4" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat5" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                        <a class="selbutton" id="seldat6" href="#" onclick='handleDateClick(this);'>select date</a><br/>
                    </div>
                    <div class="hexdots-loader" style="position: absolute; top: 190px; left: 500px;">
                        <p>Login...</p>
                    </div>
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
    var times = [];
    var checkdays = [];
    var xhttpreq;
    var maxHour = 18;
    var minHour = 6;
    var startHour = 7;
    var hourGrid = 2;
    var results = [];
    var selectedevent;

    //******************************************************************************************************************
    //  PAGE LOAD INIT SCRIPT
    //******************************************************************************************************************

    $(document).ready(function(){
        refreshPage();
    });

    function refreshPage() {
        $(".hexdots-loader").hide();

        for (i = 1; i < 7; i++) {
            document.getElementById("time_picker" + i).disabled = true;
            document.getElementById("seldat" + i).style.visibility = 'hidden';
        }

        document.getElementById("date_range_start").disabled = true;
        document.getElementById("date_range_end").disabled = true;

        document.getElementById("activate-tabs").style.visibility = 'hidden';
        document.getElementById("confirm-appointment").style.visibility = 'hidden';

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
    }

    $('#MyTabSelector').tabs({
        heightStyle: 'fill'
    });

    //******************************************************************************************************************
    //  CHECK APPOINTMENTS SCRIPT
    //******************************************************************************************************************

    $("#check-appointments").click(function(event){
        var testchecked = false;
        var inputcount = 0;
        var rangechecked = false;

        // check if ANY checkbox is ticked
        $("input:checkbox[name=boxdays]:checked").each(function() {
            testchecked = true;
            inputcount++;
        });

        // check if every ticked checkbox has a date assigned
        $("input.timey").each(function (index) {
            if ($(this).val() != "") {
                inputcount--;
            }
        });

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
        if (testchecked == true && inputcount == 0 && rangechecked == true) {
            sendAppointmentRequest();
        } else {
            if (testchecked == false) {
                alert("Please add at LEAST 1 date ! thx")
            }
            if (inputcount > 0) {
                alert("Please add " + inputcount + " missing dates");
            }
            if (rangechecked == false) {
                alert("Please check date range for proper values");
            }

        }

    });

    function sendAppointmentRequest() {
        $("#check-appointments").hide();

        // get checkbox states
        $("input:checkbox[name=boxdays]:checked").each(function()
        {
            //alert($(this).val());
            checkdays.push($(this).val());
        });

        // get timebox states
        $("input.timey").each(function (index) {
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

            for (i = 1; i < 7; i++) {
                document.getElementById("time_picker" + i).disabled = true;
                document.getElementById("option" + i).disabled = true;
                document.getElementById("date_range_start").disabled = true;
                document.getElementById("date_range_end").disabled = true;

                if (results[i-1].length > 3) {
                    document.getElementById("seldat" + i).innerHTML = results[i-1].toString();
                    document.getElementById("seldat" + i).style.visibility = 'visible';
                }
            }
        }
    }

    //******************************************************************************************************************
    //  CONFIRM APPOINTMENT SCRIPT
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

    function handleDateClick(bu) {
        selectedevent = bu.id.toString().substring(bu.id.toString().length - 1, bu.id.toString().length);
        document.getElementById("new_datestart").innerHTML = bu.innerHTML;
        document.getElementById("new_dateend").innerHTML = bu.innerHTML;
        document.getElementById("confirm-appointment").style.visibility = 'visible';
    };

    function confirmAppointmentRequest_callback() {
        if ((xhttpreq.readyState == 4) && (xhttpreq.status == 200)) {
            location.reload();
            document.getElementById("new_datestart").innerHTML = "Confirmed Appointment";
        }
    };

    //******************************************************************************************************************
    //  DELETE APPOINTMENT SCRIPT
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
    //  DATE AND TIMEPICKER SCRIPTS
    //******************************************************************************************************************


    $('.timey').each(function(){
        $(this).timepicker({
            hourGrid: hourGrid,
            minuteGrid: 10,
            timeFormat: 'HH:mm',
            stepMinute: 10,
            hour: startHour,
            hourMin: minHour,
            hourMax: maxHour
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

    //******************************************************************************************************************
    //  CHECKBOXES SCRIPTS
    //******************************************************************************************************************

    function handleCheckClick(cb) {
        if (cb.checked == true) {
            document.getElementById("time_picker" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).disabled = false;
        } else {
            document.getElementById("time_picker" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).value = "";
            document.getElementById("time_picker" + cb.id.toString().substring(cb.id.toString().length - 1, cb.id.toString().length)).disabled = true;
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
    //  DEBUG SCRIPTS
    //******************************************************************************************************************

    $("#activate-tabs").click(function(event){
        $('#MyTabSelector').enableTab(0);
        $('#MyTabSelector').enableTab(1);
        $('#MyTabSelector').enableTab(2);
    });

</script>
</html>
