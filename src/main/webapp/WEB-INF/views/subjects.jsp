<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
    <c:if test="${not empty errorMessage}">
        <p style="color: red;">${errorMessage}</p>
    </c:if>
    <c:forEach var="subject" items="${sessionScope.subjects}">
        <div class="featured-sections" onclick="openPopup(this, false, '${subject.name}', '${subject.id}')">
            <h4>nom : ${subject.name}</h4>
        </div>
    </c:forEach>
    <div class="popup" id="popup" onclick="closePopup(event)">
        <div class="popup-content">
            <form action="${pageContext.request.contextPath}/subject" method="GET">
                <label for="name">Nom :
                    <input type="text" id="name" name="name" class="name" required></label><br>
                <input type="hidden" name="id" class="id" value="">
                <button type="submit" name="action" value="save">Valider</button>
                <button type="submit" name="action" value="delete" style="color: red;">Supprimer la filière</button>
            </form>
        </div>
    </div>
    <button onclick="openPopup(this, true, null, null)">Créer une filière</button>
    <script src="${pageContext.request.contextPath}/assets/js/subjects.js"></script>
</div>

<%@ include file="fragments/footer.jsp" %>
