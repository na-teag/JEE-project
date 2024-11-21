<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
	<c:choose>
		<c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.admin}">
			<c:if test="${not empty errorMessage}">
				<p style="color: red;">${errorMessage}</p>
			</c:if>
			<c:forEach var="pathway" items="${sessionScope.promos}">
				<div class="featured-sections" onclick="openPopup(this, false, '${pathway.name}', '${pathway.id}', '${pathway.email}')">
					<h4>nom : ${pathway.name}</h4>
					<p>email : ${pathway.email}</p>
				</div>
			</c:forEach>
			<div class="popup" id="popup" onclick="closePopup(event)">
				<div class="popup-content">
					<form action="${pageContext.request.contextPath}/promo" method="GET">
						<label for="name">Nom :
							<input type="text" id="name" name="name" class="name" required></label><br>
						<label for="email">Email :
							<input type="email" id="email" name="email" class="email" required></label><br>
						<input type="hidden" name="id" class="id" value="">
						<button type="submit" name="action" value="save">Valider</button>
						<button type="submit" name="action" value="delete" style="color: red;">Supprimer la promo</button>
					</form>
				</div>
			</div>
			<button onclick="openPopup(this, true, null, null, null)">Créer une promo</button>
			<script src="${pageContext.request.contextPath}/assets/js/pathwaysAndPromos.js"></script>
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
