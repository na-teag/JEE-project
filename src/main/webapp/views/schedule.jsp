<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>

<div class="main-content">
	<c:choose>
		<c:when test="${not empty sessionScope.user}">
			<h2>Bienvenue, ${sessionScope.role} ${sessionScope.user.firstName} !</h2>
		</c:when>
		<c:otherwise>
			<h2>Bienvenue sur l'Espace Numérique de Travail de CY Virtuel</h2>
		</c:otherwise>
	</c:choose>

</div>

<%@ include file="fragments/footer.jsp" %>
