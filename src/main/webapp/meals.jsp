<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style><%@include file="/WEB-INF/css/style.css"%></style>
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
        <c:set var="color" value="green" />
        <c:if test="${meal.isExcess()}">
            <c:set var="color" value="red" />
        </c:if>
        <tr style="color: ${color}">
            <td>${FORMATTER.format(meal.getDateTime())}</td>
            <td>${meal.getDescription()}</td>
            <td>${meal.getCalories()}</td>
            <td><a href="${pageContext.request.contextPath}/meals?action=edit&mealId=<c:out value="${meal.getId()}"/>">Update</a></td>
            <td><a href="${pageContext.request.contextPath}/meals?action=delete&mealId=<c:out value="${meal.getId()}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>