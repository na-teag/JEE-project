<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>

<div class="main-content">
    <h2>
        <c:choose>
            <c:when test="${not empty sessionScope.role}">
                Vous n'êtes pas autorisé à accéder à cette page.
            </c:when>
            <c:otherwise>
                Veuillez vous connecter.
            </c:otherwise>
        </c:choose>
    </h2>
</div>

<%@ include file="fragments/footer.jsp" %>
