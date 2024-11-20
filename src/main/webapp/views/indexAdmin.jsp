<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/indexAdmin.css">


<div class="main-content">
	<c:choose>
	<c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.admin}">
		<h2>Bienvenue, ${sessionScope.user.firstName} !</h2>

		<div class="featured-sections">
			<a href="${pageContext.request.contextPath}/classes"><section>
				<h3>Gérer les classes</h3>
				<p>Éditez les classes, leurs promo et leurs filières</p>
			</section></a>
		</div>
	</c:when>
	<c:otherwise>
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
	</c:otherwise>
	</c:choose>

</div>

<%@ include file="fragments/footer.jsp" %>
