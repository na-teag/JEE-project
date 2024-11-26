<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
	<c:choose>
		<c:when test="${not empty sessionScope.roles and sessionScope.role == sessionScope.roles.admin}">
			<c:if test="${not empty errorMessage}">
				<p style="color: red;">${errorMessage}</p>
			</c:if>
			<c:forEach var="course" items="${sessionScope.courses}">
				<div class="featured-sections" onclick="openPopup(this, false, '${course.id}', '${course.subject.id}', '${course.professor.id}', '${course.classroom}', [<c:forEach	var="group" varStatus="status" items="${course.studentGroups}">'${group.id}'<c:if test="${!status.last}">,</c:if></c:forEach>])">
			<h4>sujet : ${course.subject.name}</h4>
			<p>professeur : ${course.professor.firstName} ${course.professor.lastName}</p>
					<p>salle par défaut : ${course.classroom}</p>
					<p>groupes inscrits :
						<c:forEach var="group" varStatus="status" items="${course.studentGroups}">
							${group.name}<c:if test="${!status.last}">,</c:if>
						</c:forEach>
					</p>
				</div>
			</c:forEach>
			<div class="popup" id="popup" onclick="closePopup(event)">
				<div class="popup-content">
					<form action="${pageContext.request.contextPath}/course" method="GET">
						<p>matière :</p>
						<div id="subject-options">
							<select name="subjectId" required>
								<option value="" disabled selected>Choisir un sujet</option>
								<c:forEach var="subject" items="${sessionScope.subjects}">
									<option value="${subject.id}">${subject.name}</option>
								</c:forEach>
							</select>
						</div>
						<p>professeur :</p>
						<div id="professor-options">
							<select name="professorId" required>
								<option value="" disabled selected>Choisir un professeur</option>
								<c:forEach var="professor" items="${sessionScope.professors}">
									<option value="${professor.id}">${professor.firstName} ${professor.lastName}</option>
								</c:forEach>
							</select>
						</div>
						<p>groupes inscrits :</p>
						<div id="groups-options">
							<select name="groupsId" multiple required>
								<option value="" disabled selected>Choisir des groupes</option>
								<c:forEach var="group" items="${sessionScope.groups}">
									<option value="${group.id}">${group.name}</option>
								</c:forEach>
							</select>
						</div>
						<label for="classroom">salle par défaut :
							<input type="text" id="classroom" name="classroom" class="classroom" required></label><br>
						<input type="hidden" name="id" class="id" value="">
						<button type="submit" name="action" value="save">Valider</button>
						<button type="submit" name="action" value="delete" style="color: red;">Supprimer le cours</button>
					</form>
				</div>
			</div>
			<button onclick="openPopup(this, true, null, null, null)">Créer un cours</button>
			<script src="${pageContext.request.contextPath}/assets/js/course.js"></script>
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
