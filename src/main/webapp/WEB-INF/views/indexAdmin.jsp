<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/indexAdmin.css">


<div class="main-content">
    <h2>Bienvenue, ${sessionScope.user.firstName} !</h2>

    <div class="featured-sections">
        <a href="${pageContext.request.contextPath}/users">
            <section>
                <h3>Gérer les utilisateurs</h3>
                <p>Éditez les données des utilisateurs</p>
            </section>
        </a>
        <a href="${pageContext.request.contextPath}/classes">
            <section>
                <h3>Gérer les classes</h3>
                <p>Éditez les classes, leurs promo et leurs filières</p>
            </section>
        </a>
        <a href="${pageContext.request.contextPath}/pathways">
            <section>
                <h3>Gérer les filières</h3>
                <p>Éditez les filières</p>
            </section>
        </a>
        <a href="${pageContext.request.contextPath}/promos">
            <section>
                <h3>Gérer les promos</h3>
                <p>Éditez les promos</p>
            </section>
        </a>
        <a href="${pageContext.request.contextPath}/subjects">
            <section>
                <h3>Gérer les sujets</h3>
                <p>Éditez les sujets possibles des cours</p>
            </section>
        </a>
    </div>
    <div class="featured-sections">
        <a href="${pageContext.request.contextPath}/courses">
            <section>
                <h3>Gérer les cours</h3>
                <p>Éditez les cours, l'enseignant, les groupes et plus encore</p>
            </section>
        </a>
        <a href="${pageContext.request.contextPath}/scheduleAdmin">
            <section>
                <h3>Ajouter une occurrence de cours</h3>
                <p>Ajouter un cours</p>
            </section>
        </a>
        <a href="${pageContext.request.contextPath}/adminDeleteCourse">
            <section>
                <h3>Supprimer une occurrence de cours</h3>
                <p>Supprimer un cours</p>
            </section>
        </a>
        <a href="${pageContext.request.contextPath}/classCategories">
            <section>
                <h3>Gérer les types de cours</h3>
                <p>Éditez les types de cours</p>
            </section>
        </a>
    </div>
</div>

<%@ include file="fragments/footer.jsp" %>
