<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="views/fragments/header.jsp" %>

<div class="main-content">
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <h2>Bienvenue, ${sessionScope.role} ${sessionScope.user.name} !</h2>
        </c:when>
        <c:otherwise>
            <h2>Bienvenue sur l'Espace Numérique de Travail du Lycée Virtuel</h2>
        </c:otherwise>
    </c:choose>
    <p>Découvrez les fonctionnalités de notre ENT : gestion des cours, des étudiants, des enseignants et plus encore.</p>
    
    <div class="featured-sections">
        <section>
            <h3>Accès aux Cours</h3>
            <p>Consultez vos cours en ligne, accédez à des ressources pédagogiques, et bien plus.</p>
        </section>
        <section>
            <h3>Suivi des Étudiants</h3>
            <p>Consultez les performances des étudiants, leurs notes et leurs progrès.</p>
        </section>
        <section>
            <h3>Gestion des Enseignants</h3>
            <p>Accédez à la gestion des enseignants et suivez leurs plannings et disponibilités.</p>
        </section>
    </div>
</div>

<%@ include file="views/fragments/footer.jsp" %>
</html>
