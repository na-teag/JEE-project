<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
	<h2>Gérer les utilisateurs</h2>
	<c:if test="${not empty errorMessage}">
		<p style="color: red;">${errorMessage}</p>
	</c:if>

	<h3>Élèves</h3>
	<c:forEach var="student" items="${sessionScope.studentUsers}">
		<div class="featured-sections"
			 onclick="openPopupStudent(this, false, '${student.id}', '${student.email}', '${student.lastName}', '${student.firstName}', '${student.address.number}', '${student.address.street}', '${student.address.city}', '${student.address.postalCode}', '${student.address.country}', ${student.classe.id})">
			<h4>nom : ${student.firstName} ${student.lastName}</h4>
			<p>numéro étudiant : ${student.personNumber}</p>
			<p>email : ${student.email}</p>
			<p>adresse
				: ${student.address.number} ${student.address.street}, ${student.address.postalCode} ${student.address.city}, ${student.address.country}</p>
			<p>classe : ${student.classe.name}</p>
		</div>
	</c:forEach>
	<button onclick="openPopupStudent(this, true, null, null, null, null, null, null, null, null)">Créer un élève
	</button>
	<br><br>
	<h3>Enseignants</h3>
	<c:forEach var="professor" items="${sessionScope.profUsers}">
		<div class="featured-sections"
			 onclick="openPopupProf(this, false, '${professor.id}', '${professor.email}', '${professor.lastName}', '${professor.firstName}', '${professor.address.number}', '${professor.address.street}', '${professor.address.city}', '${professor.address.postalCode}', '${professor.address.country}', [
			 	<c:forEach var="subject" varStatus="status" items="${professor.teachingSubjects}">'${subject.id}'
			 		<c:if test="${!status.last}">,</c:if>
			 	</c:forEach>])">
			<h4>nom : ${professor.firstName} ${professor.lastName}</h4>
			<p>numéro enseignant : ${professor.personNumber}</p>
			<p>email : ${professor.email}</p>
			<p>adresse
				: ${professor.address.number} ${professor.address.street}, ${professor.address.postalCode} ${professor.address.city}, ${professor.address.country}</p>
			<p>sujets de cours possibles :
				<c:forEach var="subject" varStatus="status" items="${professor.teachingSubjects}">
					${subject.name}<c:if test="${!status.last}">,</c:if>
				</c:forEach>
			</p>
		</div>
	</c:forEach>
	<button onclick="openPopupProf(this, true, null, null, null, null, null, null, null)">Créer un enseignant</button>
	<br><br>
	<h3>Administrateurs</h3>
	<c:forEach var="admin" items="${sessionScope.adminUsers}">
		<div class="featured-sections"
			 onclick="openPopupAdmin(this, false, '${admin.id}', '${admin.email}', '${admin.lastName}', '${admin.firstName}', '${admin.address.number}', '${admin.address.street}', '${admin.address.city}', '${admin.address.postalCode}', '${admin.address.country}')">
			<h4>nom : ${admin.firstName} ${admin.lastName}</h4>
			<p>numéro administrateur : ${admin.personNumber}</p>
			<p>email : ${admin.email}</p>
			<p>adresse
				: ${admin.address.number} ${admin.address.street}, ${admin.address.postalCode} ${admin.address.city}, ${admin.address.country}</p>
		</div>
	</c:forEach>
	<button onclick="openPopupAdmin(this, true, null, null, null, null, null, null, null, null)">Créer un
		administrateur
	</button>
	<div class="popup" id="popup" onclick="closePopup(event)">
		<div class="popup-content">
			<form action="${pageContext.request.contextPath}/user" method="GET">
				<label for="firstName">Prénom :
					<input type="text" id="firstName" name="firstName" class="firstName" required></label><br>
				<label for="lastName">Nom :
					<input type="text" id="lastName" name="lastName" class="lastName" required></label><br>
				<div class="birthday"><label for="birthday">Date de naissance :
					<input type="date" id="birthday" name="birthday" required></label><br></div>
				<label for="email">Email :
					<input type="email" id="email" name="email" class="email" required></label><br>
				<label for="number">Numéro de rue :
					<input type="text" id="number" name="number" class="number" required></label><br>
				<label for="street">Rue :
					<input type="text" id="street" name="street" class="street" required></label><br>
				<label for="city">Ville :
					<input type="text" id="city" name="city" class="city" required></label><br>
				<label for="postalCode">Code postal :
					<input type="number" id="postalCode" name="postalCode" class="postalCode" required></label><br>
				<label for="country">Pays :
					<input type="text" id="country" name="country" class="country" required></label><br>
				<input type="hidden" name="id" class="id" value="">
				<div id="classe">
					<p>Classe :</p>
					<div id="classe-options">
						<select name="classeId" required>
							<option value="" disabled selected>Choisir une classe</option>
							<c:forEach var="classe" items="${sessionScope.classes}">
								<option value="${classe.id}">${classe.name}</option>
							</c:forEach>
						</select>
					</div>
					<br>
				</div>
				<div id="subject">
					<p>sujets de cours possibles :</p>
					<div id="subject-options">
						<select name="subjectId" multiple>
							<option value="" disabled selected>Choisir des sujets</option>
							<option value="aucun" selected>aucun</option>
							<c:forEach var="subject" items="${sessionScope.subjects}">
								<option value="${subject.id}">${subject.name}</option>
							</c:forEach>
						</select>
					</div>
					<br>
				</div>
				<button type="submit" name="action" id="save" value="save">Valider</button>
				<button type="submit" name="action" value="delete" style="color: red;">Supprimer l'utilisateur</button>
			</form>
		</div>
	</div>
	<script src="${pageContext.request.contextPath}/assets/js/users.js"></script>
</div>

<%@ include file="fragments/footer.jsp" %>
