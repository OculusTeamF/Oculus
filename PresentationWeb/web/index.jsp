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
                    <li><a href="#tabs-2">Choose appointment criterias</a></li>
                    <li><a href="#tabs-3">Confirmation</a></li>
                </ul>
                <div id="tabs-1">
                    <br/>
                    <div id="app_box">
                        <h3><strong>Appointment #1:</strong></h3>
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
                                        <a class="selbutton" id="seldate1" href="#">select date</a>
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
                                        <a class="selbutton" id="seldate2" href="#">select date</a>
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
                                        <a class="selbutton" id="seldate3" href="#">select date</a>
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
                                        <a class="selbutton" id="seldate4" href="#">select date</a>
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
                                        <a class="selbutton" id="seldate5" href="#">select date</a>
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
                                        <a class="selbutton" id="seldate6" href="#">select date</a>
                                    </td>
                                </tr>
                            </table>
                            <br/>
                            <table class="bordered">
                                <tr>
                                    <td>
                                        <small>Choose unavailability date:</small><br/>
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
                        <h3><strong>Confirm appointment #1:</strong></h3>

                        <strong>Date start:</strong> <p id="new_datestart"></p>
                        <strong>Date end:</strong> <p id="new_dateend"></p>
                        <strong>Reason(s) for your visit:</strong> <p id="new_description"></p>
                        <textarea rows="7" cols="32" autofocus></textarea>
                        <br/><br/>
                        <button type="button" id="cancel-appointment">Cancel appointment</button>
                        <br/><br/>
                        <button type="button" id="confirm-appointment">Confirm appointment</button>
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

    //******************************************************************************************************************
    //  PAGE LOAD INIT SCRIPT
    //******************************************************************************************************************

    $(document).ready(function(){

        $(".hexdots-loader").hide();
        for (i = 1; i < 7; i++) {
            document.getElementById("time_picker" + i).disabled = true;
            document.getElementById("seldate" + i).style.visibility = 'hidden';
        }

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

    $('#MyTabSelector').tabs({
        heightStyle: 'fill'
    });

    //******************************************************************************************************************
    //  CHECK APPOINTMENTS SCRIPT
    //******************************************************************************************************************

    $("#check-appointments").click(function(event){
        //TODO: check if at least 1 date is added
        //TODO: check if every selected checkbox has date assigned
        sendAppointmentRequest();
    });

    function sendAppointmentRequest() {
        $("#check-appointments").hide();
        //$(".hexdots-loader").show();

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
        xhttpreq.send();
    }

    function sendAppointmentRequest_callback() {
        if ((xhttpreq.readyState == 4) && (xhttpreq.status == 200)) {
            alert("[DEBUG] Possible Dates:" + xhttpreq.responseText);

            for (i = 1; i < 7; i++){
                document.getElementById("trid" + i).style.backgroundColor='#39AD65';
                document.getElementById("time_picker" + i).disabled = true;
                document.getElementById("option" + i).disabled = true;
                document.getElementById("date_range_start").disabled = true;
                document.getElementById("date_range_end").disabled = true;
                document.getElementById("seldate" + i).style.visibility = 'visible';
                //document.getElementById("seldate" + i).setAttribute("value","tessst");
                document.getElementById("seldate" + i).onclick = function () {
                    var getdatev = "Selected date should be here later";
                    alert('[DEBUG] date selected: ' + getdatev); // link to confirmation

                    document.getElementById("new_datestart").innerHTML = getdatev;
                    document.getElementById("new_dateend").innerHTML = getdatev;
                    document.getElementById("new_description").innerHTML = "nix";

                    $('#MyTabSelector').disableTab(1, false);
                    $('#MyTabSelector').tabs( "option", "active", 2 );
                };
            }
        }
    }

    //******************************************************************************************************************
    //  CONFIRM APPOINTMENT SCRIPT
    //******************************************************************************************************************

    $("#confirm-appointment").click(function(event){
        $('#MyTabSelector').enableTab(0);
        $('#MyTabSelector').disableTab(1, true);
        $('#MyTabSelector').disableTab(2, true);
        $("#confirm-appointment").hide();
        $('#MyTabSelector').tabs( "option", "active", 0 );
    });

    $("#cancel-appointment").click(function(event){
        alert("cancel");
    });

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
            alert("delete done");
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
