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
    <title></title>
</head>
<body>
<div class="header_outherHeader">
    Logged in User: <br><br>

    First Name: ${user.firstName}
    <br>
    Last Name: ${user.lastName}
    <br>
    SV Number: ${user.svNumber}
</div>

<br>
<div class="wrapper">
    <div class="mainBox">
        <ul id="crumbs">
            <li><a href="#">Home</a></li>
            <li><a href="#">Main section</a></li>
            <li><a href="#">Sub section</a></li>
            <li><a href="#">Sub sub section</a></li>
            <li>Home section...</li>
        </ul>
    </div>
</body>
</html>
