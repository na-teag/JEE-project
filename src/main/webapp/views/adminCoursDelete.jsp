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
    <c:forEach var="occurence" items="${occurences}">
      <tr>

        <td>${occurence.classroom}</td>
        <td>${occurence.professor.firstName} ${occurence.professor.lastName}</td>
        <td>${occurence.beginning}</td>
        <td>${occurence.end}</td>
        <td>${occurence.day}</td>
        <td>${occurence.course.subject.name}</td>
        <td>
          <form action="${pageContext.request.contextPath}/admindeletecours" method="get">
            <input type="hidden" name="deleteId" value="${occurence.id}" />
            <button type="submit">Supprimer</button>
          </form>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>

</div>

<%@ include file="fragments/footer.jsp" %>
