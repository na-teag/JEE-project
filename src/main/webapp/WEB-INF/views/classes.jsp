<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
    <h2>Gérer les classes</h2>
    <c:if test="${not empty errorMessage}">
        <p style="color: red;">${errorMessage}</p>
    </c:if>
    <c:forEach var="classe" items="${sessionScope.classes}">
        <div class="featured-sections"
             onclick="openPopup(this, false, '${classe.name}', '${classe.id}', '${classe.pathway.id}', '${classe.promo.id}', '${classe.email}')">
            <h4>nom : ${classe.name}</h4>
            <p>email : ${classe.email}</p>
            <p>filière : ${classe.pathway.name}</p>
            <p>promo : ${classe.promo.name}</p>
        </div>
    </c:forEach>
    <div class="popup" id="popup" onclick="closePopup(event)">
        <div class="popup-content">
            <form action="${pageContext.request.contextPath}/classe" method="GET">
                <label for="name">Nom :
                    <input type="text" id="name" name="name" class="name" required></label><br>
                <label for="email">Email :
                    <input type="email" id="email" name="email" class="email" required></label>
                <input type="hidden" name="id" class="id" value="">
                <p>Promo :</p>
                <div id="promo-options">
                    <select name="promoId" required>
                        <option value="" disabled selected>Choisir une promo</option>
                        <c:forEach var="promo" items="${sessionScope.promos}">
                            <option value="${promo.id}">${promo.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <p>Filière :</p>
                <div id="pathway-options">
                    <select name="pathwayId" required>
                        <option value="" disabled selected>Choisir une filière</option>
                        <c:forEach var="pathway" items="${sessionScope.pathways}">
                            <option value="${pathway.id}">${pathway.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <button type="submit" name="action" value="save">Valider</button>
                <button type="submit" name="action" value="delete" style="color: red;">Supprimer la classe</button>
            </form>
        </div>
    </div>
    <button onclick="openPopup(this, true, null, null, null, null, null)">Créer une classe</button>
    <script src="${pageContext.request.contextPath}/assets/js/classes.js"></script>
</div>

<%@ include file="fragments/footer.jsp" %>
