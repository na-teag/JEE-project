<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-schedule.css">

<div class="scheduleAdmin">
    <form action="${pageContext.request.contextPath}/scheduleAdmin" method="post">
        <!-- Tableau principal -->
        <table>
            <thead>
            <tr>
                <th>Personne</th>
                <c:forEach var="professeur" items="${professeurs}">
                    <th>${professeur}</th>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <!-- Section pour les élèves -->
            <c:forEach var="eleve" items="${eleves}">
                <tr>
                    <td>${eleve}</td>
                    <c:forEach var="professeur" items="${professeurs}">
                        <td>
                            <select name="assignments">
                                <option value="">Sélectionnez un cours</option>
                                <c:forEach var="cours" items="${cours}">
                                    <option value="eleve:${eleve},professeur:${professeur},cours:${cours}">
                                            ${cours}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>

            <!-- Section pour les groupes -->
            <c:forEach var="groupe" items="${groupes}">
                <tr>
                    <td>${groupe}</td>
                    <c:forEach var="professeur" items="${professeurs}">
                        <td>
                            <select name="assignments">
                                <option value="">Sélectionnez un cours</option>
                                <c:forEach var="cours" items="${cours}">
                                    <option value="groupe:${groupe},professeur:${professeur},cours:${cours}">
                                            ${cours}
                                    </option>
                                </c:forEach>
                            </select>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>

            <label for="classroom">Salle de classe :</label>
            <input type="text" id="classroom" name="classroom" placeholder="Numéro de salle" />
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
                    <option value="${category}">${category}</option>
                </c:forEach>
            </select>
        <br>
        <button type="submit">Enregistrer</button>
    </form>
</div>

<%@ include file="fragments/footer.jsp" %>
