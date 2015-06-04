<%--
  ~ Copyright (c) 2015 Team F
  ~
  ~ This file is part of Oculus.
  ~ Oculus is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
  ~ Oculus is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
  ~ You should have received a copy of the GNU General Public License along with Oculus.  If not, see <http://www.gnu.org/licenses/>.
  --%>

<%--
  Created by IntelliJ IDEA.
  User: Fabian
  Date: 02.06.2015
  Time: 12:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css">
    <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
    <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
    <style>
        .ui-body-f {
            color: white;
            background-color: red;
        }
    </style>
</head>
<body>


    <div id="datelist" data-role="content">
        <ul data-role="listview" data-inset="true">
            <li><a href="#selectdate">(Date available)</a></li>
            <li><a href="#selectdate">(Date available)</a></li>
            <li data-theme="f">(Date not available)</li>
            <li>(Not checked yet)</li>
        </ul>
    </div>


</body>

<script>
    $( ".listview" ).listview({
        disabled:false
    });

    $("#datelist").on('click', 'li', function (e) {
        alert($(this).text());
    });
</script>
</html>

