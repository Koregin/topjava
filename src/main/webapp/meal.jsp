<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <title>Add new meal</title>
</head>
<body>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" readonly="readonly" name="mealId" value="<c:out value="${meal.getId()}" />" /> <br />
    DateTime : <input
        type="text" name="dateTime"
        <fmt:parseDate value="${meal.getDateTime()}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" />
        value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${parsedDateTime}" />" /><br />
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.getDescription()}" />" /> <br />
    Calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.getCalories()}" />" /> <br />
    <input type="submit" value="Submit" />
</form>
</body>
</html>
