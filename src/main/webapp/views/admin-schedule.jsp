<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">

<div class="scheduleAdmin">
    <form action="${pageContext.request.contextPath}/scheduleAdmin" method="post">
        <table>
            <thead>
            <tr>
                <th>Personne</th>
                <c:forEach var="cours" items="${cours}">
                    <th>${cours}</th>
                </c:forEach>
            </tr>
            </thead>
            <tbody>
            <!-- Section pour les élèves -->
            <c:forEach var="eleve" items="${eleves}">
                <tr>
                    <td>${eleve}</td>
                    <c:forEach var="cours" items="${cours}">
                        <td>
                            <input type="checkbox" name="assignments"
                                   value="eleve:${eleve},cours:${cours}">
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>

            <!-- Section pour les professeurs -->
            <c:forEach var="professeur" items="${professeurs}">
                <tr>
                    <td>${professeur}</td>
                    <c:forEach var="cours" items="${cours}">
                        <td>
                            <input type="checkbox" name="assignments"
                                   value="professeur:${professeur},cours:${cours}">
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>

            <!-- Section pour les groupes -->
            <c:forEach var="groupe" items="${groupes}">
                <tr>
                    <td>${groupe}</td>
                    <c:forEach var="cours" items="${cours}">
                        <td>
                            <input type="checkbox" name="assignments"
                                   value="groupe:${groupe},cours:${cours}">
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <button type="submit">Enregistrer</button>
    </form>
</div>

<%@ include file="fragments/footer.jsp" %>
