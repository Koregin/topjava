<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>${meal != null ? "Edit meal" : "Add meal"}</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal != null ? "Edit meal" : "Add meal"}</h2>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" readonly="readonly" name="mealId" value="${meal.id}"/> <br/>
    <label for="dateTime">DateTime:</label>
    <input type="datetime-local" id="dateTime" name="dateTime"
           value="${meal.dateTime != null ? meal.dateTime : dateTimeNow}"/><br/>
    Description : <input
        type="text" name="description"
        value="${meal.description}"/><br/>
    Calories : <input
        type="number" min="1" max="1000000" name="calories"
        value="${meal.calories}"/> <br/>
    <input type="submit" value="Save"/>
    <input class="button" type="button" onclick="window.location.replace('meals')" value="Cancel"/>
</form>
</body>
</html>
