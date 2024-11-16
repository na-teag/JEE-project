<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/grades.css">

<div class="main-content">
    <h2>Affichage de vos notes</h2>
    <div class="average">
        <c:choose>
            <c:when test="${empty grades}">
                <h3>Aucune note disponible</h3>
            </c:when>
            <c:otherwise>
                <h3>Moyenne générale : ${average}</h3>
            </c:otherwise>
        </c:choose>
    </div>
    <table>
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
        <c:forEach var="course" items="${courses}">
            <tr>
                <td>${course.subject.name}</td>

                <!-- On vérifie s'il y a une note associée au cours -->
                <c:choose>
                    <c:when test="${not empty grades}">
                        <!-- Si des notes existent, on affiche les informations de la note -->
                        <c:forEach var="grade" items="${grades}">
                            <c:if test="${grade.course.id == course.id}">
                                <td>${grade.result}</td>
                                <td>${grade.context}</td>
                                <td>${grade.comment}</td>
                                <td>${grade.day}</td>
                                <td>${grade.session}</td>
                            </c:if>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form action="pdf" method="get">
        <button type="submit" class="download-btn">Télécharger le relevé de notes en PDF</button>
    </form>
</div>

<%@ include file="fragments/footer.jsp" %>
