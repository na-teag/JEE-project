<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
	<c:choose>
		<c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.admin}">
			<c:if test="${not empty errorMessage}">
				<p style="color: red;">${errorMessage}</p>
			</c:if>
			<c:forEach var="classe" items="${sessionScope.classes}">
				<div class="featured-sections" onclick="openPopup(this, false, '${classe.name}', '${classe.id}', '${classe.pathway.id}', '${classe.promo.id}', '${classe.email}')">
					<h4>nom : ${classe.name}</h4>
					<p>email : ${classe.email}</p>
					<p>filière : ${classe.pathway.name}</p>
					<p>promo : ${classe.promo.name}</p>
				</div>
			</c:forEach>
			<div class="popup" id="popup" onclick="closePopup(event)">
				<div class="popup-content">
					<form action="${pageContext.request.contextPath}/classe" method="GET">
						<label for="classeName">Nom :
						<input type="text" id="classeName" name="classeName" class="classeName" required></label><br>
						<label for="email">Email :
						<input type="email" id="email" name="email" class="email" required></label>
						<input type="hidden" name="classeId" class="classeId" value="">
						<p>Promo :</p>
						<div id="promo-options">
							<c:forEach var="promo" items="${sessionScope.promos}">
								<label>
									<input type="radio" name="promoId" value="${promo.id}" required> ${promo.name}
								</label><br>
							</c:forEach>
						</div>
						<p>Filière :</p>
						<div id="pathway-options">
							<c:forEach var="pathway" items="${sessionScope.pathways}">
								<label>
									<input type="radio" name="pathwayId" value="${pathway.id}" required> ${pathway.name}
								</label><br>
							</c:forEach>
						</div>
						<button type="submit" name="action" value="save">Valider</button>
						<button type="submit" name="action" value="delete" style="color: red;">Supprimer la classe</button>
					</form>
				</div>
			</div>
			<button onclick="openPopup(this, true, null, null, null, null, null)">Créer une classe</button>
			<script src="${pageContext.request.contextPath}/assets/js/classes.js"></script>
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
