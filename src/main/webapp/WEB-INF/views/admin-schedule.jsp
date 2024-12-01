<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/courseAdd.css">

<div class="test">
    <br><h2 style="text-align: center">Gérer l'ajout d'occurence de cours</h2>
<div class="scheduleAdmin">

    <!-- Formulaire principal -->
    <form action="${pageContext.request.contextPath}/scheduleAdmin" method="get">

        <!-- Sélection du cours -->
        <label for="course">cours :</label>
        <select id="course" name="course" required>
            <option value="">Sélectionnez un cours</option>
            <c:forEach var="course" items="${courses}">
                <option value="${course.id}">
                        ${course.subject.name} avec ${course.professor.firstName} ${course.professor.lastName} pour les <c:forEach var="group" varStatus="status" items="${course.studentGroups}">${group.name}<c:if test="${!status.last}">, </c:if></c:forEach>
                </option>
            </c:forEach>
        </select>
        <br>

        <!-- Date du cours -->
        <label for="day">Jour :</label>
        <input type="date" id="day" name="day" required />
        <br>

        <!-- Heure de début -->
        <label for="beginning">Heure de début :</label>
        <input type="time" id="beginning" name="beginning" required />
        <br>

        <!-- Heure de fin -->
        <label for="end">Heure de fin :</label>
        <input type="time" id="end" name="end" required />
        <br>

        <!-- Catégorie -->
        <label for="category">Catégorie :</label>
        <select name="category" id="category" required>
            <option disabled>Sélectionnez une catégorie</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </select>
        <br><br>

        Optionnel :
        <br>
        <!-- Sélection du professeur -->
        <label for="professor">Attribuer un autre professeur que celui par défaut :</label>
        <select id="professor" name="professor">
            <option value="">Sélectionnez un professeur</option>
            <c:forEach var="professor" items="${professors}">
                <option value="${professor.id}">
                        ${professor.firstName} ${professor.lastName}
                </option>
            </c:forEach>
        </select>
        <br>

        <!-- Salle de classe -->
        <label for="classroom">Attribuer une autre salle de classe que celle par défaut :</label>
        <input type="text" id="classroom" name="classroom" placeholder="Numéro de salle"/>
        <br>

        <!-- Bouton de soumission -->
        <button type="submit">Enregistrer</button>

        <!-- Affichage des messages de succès ou d'erreur -->
        <c:if test="${not empty successMessage}">
            <p class="success-message">${successMessage}</p>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
    </form>
</div>
</div>
<%@ include file="fragments/footer.jsp" %>
