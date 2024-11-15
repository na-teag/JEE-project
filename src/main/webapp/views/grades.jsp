<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grades.css">

<div class="main-content">
    <h2>Affichage de vos notes</h2>
    <table border="5">
        <thead>
        <tr>
            <th>Cours</th>
            <th>Note</th>
            <th>Contexte</th>
            <th>Commentaire</th>
            <th>Jour</th>
            <th>Session</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="grade" items="${grades}">
        <tr>
            <td>${grade.course.subject.name}</td>
            <td>${grade.result}</td>
            <td>${grade.context}</td>
            <td>${grade.comment}</td>
            <td>${grade.day}</td>
            <td>${grade.session}</td>
        </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="fragments/footer.jsp" %>
