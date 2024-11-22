<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 11/15/2024
  Time: 10:53 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="fragments/header.jsp" %>

<div class="main-content">
  <h2>Gestion des utilisateurs</h2>
  <p>Ajouter, supprimer ou modifier les utilisateurs du site</p>

  <div class="featured-sections">
    <section>
      <h3>Gestion des étudiants</h3>
      <form action="/UserManagementServlet" method="GET">
        <input type="hidden" name="type" value="student">
        <button type="submit">Gérer les informations des étudiants</button>
      </form>
    </section>

    <section>
      <h3>Gestion des enseignants</h3>
      <form action="/UserManagementServlet" method="GET">
        <input type="hidden" name="type" value="teacher">
        <button type="submit">Gérer les informations des enseignants</button>
      </form>
    </section>
  </div>
</div>


<%@ include file="fragments/footer.jsp" %>

