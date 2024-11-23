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
			<a href="${pageContext.request.contextPath}/pathways"><section>
				<h3>Gérer les filières</h3>
				<p>Éditez les filières</p>
			</section></a>
			<a href="${pageContext.request.contextPath}/promos"><section>
				<h3>Gérer les promos</h3>
				<p>Éditez les promos</p>
			</section></a>
			<a href="${pageContext.request.contextPath}/classCategories"><section>
				<h3>Gérer les types de cours</h3>
				<p>Éditez les types de cours</p>
			</section></a>
			<a href="${pageContext.request.contextPath}/courses"><section>
				<h3>Gérer les cours</h3>
				<p>Éditez les cours, l'enseignant, les groupes et plus encore</p>
			</section></a>
		</div>
	</c:when>
	<c:otherwise>
		<h2>
		<c:choose>
			<c:when test="${empty sessionScope.role}">
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
