<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/global.css">
    <title>CY Virtuel</title>
</head>
<body>
<header>
    <div class="logo">
        <h1><a href="${pageContext.request.contextPath}/">CY Virtuel</a></h1>
    </div>
</header>
<nav>
    <ul>
        <li><a href="${pageContext.request.contextPath}/">Accueil</a></li>
        <li><a href="${pageContext.request.contextPath}/views/Contact.jsp">Contacts</a></li>

        <c:choose>
            <c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.student}">
                <li><a href="${pageContext.request.contextPath}/grades">Notes</a></li>
                <li><a href="${pageContext.request.contextPath}/schedule">Emploi du temps</a></li>
            </c:when>

            <c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.professor}">
                <li><a href="${pageContext.request.contextPath}/gradesManagement">Saisie des notes</a></li>
                <li><a href="${pageContext.request.contextPath}/schedule">Emploi du temps</a></li>
            </c:when>

            <c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.admin}">
                <li><a href="${pageContext.request.contextPath}/views/indexAdmin.jsp">éditer des objets</a></li>
                <li><a href="${pageContext.request.contextPath}/schedule">Emploi du temps</a></li>
            </c:when>
        </c:choose>

        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <li><a href="${pageContext.request.contextPath}/logout">Déconnexion</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="${pageContext.request.contextPath}/login">Connexion</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>
