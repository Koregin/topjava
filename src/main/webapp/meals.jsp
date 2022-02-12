<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        <%@include file="/WEB-INF/css/style.css" %>
    </style>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<a href="${pageContext.request.contextPath}/meals?action=create">Add Meal</a>
<table class="mealTable">
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.excess ? "red" : "green"}">
            <td>${FORMATTER.format(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="${pageContext.request.contextPath}/meals?action=edit&mealId=${meal.id}">Update</a></td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&mealId=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>