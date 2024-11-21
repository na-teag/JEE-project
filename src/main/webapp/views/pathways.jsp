<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
	<c:choose>
		<c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.admin}">
			<c:if test="${not empty errorMessage}">
				<p style="color: red;">${errorMessage}</p>
			</c:if>
			<c:forEach var="pathway" items="${sessionScope.pathways}">
				<div class="featured-sections" onclick="openPopup(this, false, '${pathway.name}', '${pathway.id}')">
					<h4>nom : ${pathway.name}</h4>
				</div>
			</c:forEach>
			<div class="popup" id="popup" onclick="closePopup(event)">
				<div class="popup-content">
					<form action="${pageContext.request.contextPath}/pathway" method="GET">
						<label for="name">Nom :
							<input type="text" id="name" name="name" class="name" required></label><br>
						<input type="hidden" name="id" class="id" value="">
						<button type="submit" name="action" value="save">Valider</button>
						<button type="submit" name="action" value="delete" style="color: red;">Supprimer la filière</button>
					</form>
				</div>
			</div>
			<button onclick="openPopup(this, true, null, null)">Créer une filière</button>
			<script src="${pageContext.request.contextPath}/assets/js/pathways.js"></script>
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
