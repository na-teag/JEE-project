<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-schedule.css">

<div class="scheduleAdmin">
    <%
        String confirmationMessage = (String) session.getAttribute("confirmationMessage");
        if (confirmationMessage != null) {
    %>
    <div class="alert alert-success">
        <%= confirmationMessage %>
    </div>
    <%
        }
    %>

    <form action="${pageContext.request.contextPath}/scheduleAdmin" method="get">
        <!-- Tableau principal -->
        <table>
            <thead>
            <tr>
                <th>Personne</th>
                <c:forEach var="professeur" items="${professeurs}">
                    <th>${professeur.firstName} ${professeur.lastName}</th>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="eleve" items="${eleves}">
                <tr>
                    <td>${eleve.firstName} ${eleve.lastName}</td>
                    <c:forEach var="professeur" items="${professeurs}">
                        <td>
                            <select name="assignments">
                                <option value="">Sélectionnez un cours</option>
                                <c:forEach var="cours" items="${professeurSubjectsMap[professeur.id]}">
                                    <option value="eleve:${eleve.id},professeur:${professeur.id},cours:${cours.id}">
                                            ${cours.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>


            <c:forEach var="groupe" items="${groupes}">
                <tr>
                    <td>${groupe.name}</td>
                    <c:forEach var="professeur" items="${professeurs}">
                        <td>
                            <select name="assignments">
                                <option value="">Sélectionnez un cours</option>
                                <c:forEach var="cours" items="${professeurSubjectsMap[professeur.id]}">
                                    <option value="groupe:${groupe.id},professeur:${professeur.id},cours:${cours.id}">
                                            ${cours.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <!-- Autres champs -->
        <label for="classroom">Salle de classe :</label>
        <input type="text" id="classroom" name="classroom" placeholder="Numero de salle" required/>
        <br>

        <label for="day">Jour :</label>
        <input type="date" id="day" name="day" required />
        <br>

        <label for="beginning">Heure de début :</label>
        <input type="time" id="beginning" name="beginning" required />
        <br>

        <label for="end">Heure de fin :</label>
        <input type="time" id="end" name="end" required />
        <br>

        <label for="category">Catégorie :</label>
        <select name="category" id="category" required>
            <option value="">Sélectionnez une catégorie</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category.id}">${category.name}</option>
            </c:forEach>
        </select>
        <br>

        <button type="submit">Enregistrer</button>
    </form>
</div>

<%@ include file="fragments/footer.jsp" %>
