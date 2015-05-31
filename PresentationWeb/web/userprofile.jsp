<%--
  Created by IntelliJ IDEA.
  User: Fabian
  Date: 31.05.2015
  Time: 14:58
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:useBean id="user" class="beans.UserBean" scope="session"/>
<html>
<head>
    <title></title>
</head>
<body>

<p>Patient First Name: <%= user.getFirstName() %>
</p>

</body>
</html>
