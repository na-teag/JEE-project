<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/coursDelete.css">

<c:if test="${not empty errorMessage}">
  <p style="color: red;text-align: center">${errorMessage}</p>
</c:if>


<div class="coursAdmin">

  <h2>Liste des occurrences des cours</h2>
  <table>
    <thead>
    <tr>
      <th>Salle</th>
      <th>Professeur</th>
      <th>Heure de début</th>
      <th>Heure de fin</th>
      <th>Date</th>
      <th>Matière</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="occurrence" items="${occurrences}">
      <tr>

        <td>${occurrence.classroom}</td>
        <td>${occurrence.professor.firstName} ${occurrence.professor.lastName}</td>
        <td>${occurrence.beginning}</td>
        <td>${occurrence.end}</td>
        <td>${occurrence.day}</td>
        <td>${occurrence.course.subject.name}</td>
        <td>
          <form action="${pageContext.request.contextPath}/adminDeleteCourse" method="get">
            <input type="hidden" name="deleteId" value="${occurrence.id}" />
            <button type="submit">Supprimer</button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

</div>

<%@ include file="fragments/footer.jsp" %>
