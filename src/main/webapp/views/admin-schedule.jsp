<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/coursAdd.css">

<div class="scheduleAdmin">

    <!-- Formulaire principal -->
    <form action="${pageContext.request.contextPath}/scheduleAdmin" method="get">

        <!-- Sélection du professeur -->
        <label for="professeur">Professeur :</label>
        <select id="professeur" name="professeur" required>
            <option value="">Sélectionnez un professeur</option>
            <c:forEach var="professeur" items="${professeurs}">
                <option value="${professeur.id}">
                        ${professeur.firstName} ${professeur.lastName}
                </option>
            </c:forEach>
        </select>
        <br>

        <!-- Sélection de la matière -->
        <label for="subject">Matière :</label>
        <select id="subject" name="subject" required>
            <option value="">Sélectionnez une matière</option>
            <c:forEach var="professeur" items="${professeurs}">
                <optgroup label="${professeur.firstName} ${professeur.lastName}">
                    <c:forEach var="subject" items="${professeurSubjectsMap[professeur.id]}">
                        <option value="${subject.id}">
                                ${subject.name}
                        </option>
                    </c:forEach>
                </optgroup>
            </c:forEach>
        </select>
        <br>

        <!-- Salle de classe -->
        <label for="classroom">Salle de classe :</label>
        <input type="text" id="classroom" name="classroom" placeholder="Numéro de salle" required/>
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
            <option value="">Sélectionnez une catégorie</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </select>
        <br>

        <!-- Bouton de soumission -->
        <button type="submit">Enregistrer</button>

        <!-- Affichage des messages de succès ou d'erreur -->
        <c:if test="${not empty param.success}">
            <p class="success-message">Cours enregistré avec succès !</p>
        </c:if>
        <c:if test="${not empty param.error}">
            <p class="error-message">${param.error}</p>
        </c:if>
    </form>
</div>

<%@ include file="fragments/footer.jsp" %>
