<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/gradesManagement.css">

<div class="main-content">
    <h1>Saisie des notes</h1>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success">
                ${sessionScope.message}
        </div>
        <c:remove var="message" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">
                ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>
    <form action="gradesManagement" method="get">
        <label for="courses">Choisir le cours :</label>
        <select id="courses" name="courses" onchange="this.form.submit()">
            <option value="">Sélectionner un de vos cours</option>
            <c:forEach var="course" items="${coursesList}">
                <option value="${course.id}"
                        <c:if test="${course.id == sessionScope.coursesId}">selected</c:if>>
                        ${course.subject.name}
                </option>
            </c:forEach>
        </select>
    </form>

    <form action="gradesManagement" method="get">
        <c:if test="${not empty classesList}">
            <label for="classes">Choisir la classe :</label>
            <select id="classes" name="classes" onchange="this.form.submit()">
                <option value="">Sélectionner une de vos classes</option>
                <c:forEach var="classe" items="${classesList}">
                    <option value="${classe.id}"
                            <c:if test="${classe.id == sessionScope.classesId}">selected</c:if>>
                            ${classe.name}
                    </option>
                </c:forEach>
            </select>
        </c:if>
    </form>

    <form action="gradesManagement" method="get">
        <c:if test="${not empty studentsList}">
            <label for="students">Choisir l'étudiant :</label>
            <select id="students" name="students" onchange="this.form.submit()">
                <option value="">Sélectionner un étudiant</option>
                <c:forEach var="student" items="${studentsList}">
                    <option value="${student.id}"
                            <c:if test="${student.id == sessionScope.selectedStudentId}">selected</c:if>>
                            ${student.lastName} ${student.firstName}
                    </option>
                </c:forEach>
            </select>
        </c:if>
    </form>

    <c:if test="${not empty sessionScope.selectedStudentId}">
        <form action="gradesManagement" method="post">
            <div class="form-row">
                <div class="form-group">
                    <label for="grade">Note :</label>
                    <input type="number" id="grade" name="grade" min="0" max="20" step="0.1"
                           value="${not empty sessionScope.selectedStudentGrade ? sessionScope.selectedStudentGrade.result : ''}" required/>
                </div>

                <div class="form-group">
                    <label for="context">Contexte(facultatif) :</label>
                    <input type="text" id="context" name="context"
                           value="${not empty sessionScope.selectedStudentGrade ? sessionScope.selectedStudentGrade.context : ''}"/>
                </div>

                <div class="form-group">
                    <label for="comment">Commentaire(facultatif) :</label>
                    <input type="text" id="comment" name="comment"
                           value="${not empty sessionScope.selectedStudentGrade ? sessionScope.selectedStudentGrade.comment : ''}"/>
                </div>

                <div class="form-group">
                    <label for="session">Session :</label>
                    <input type="number" id="session" name="session" min="1" max="2" step="1"
                           value="${not empty sessionScope.selectedStudentGrade ? sessionScope.selectedStudentGrade.session : ''}" required/>
                </div>
            </div>

            <button type="submit">
                    ${not empty sessionScope.selectedStudentGrade ? 'Modifier la note' : 'Enregistrer la note'}
            </button>
        </form>
    </c:if>
</div>

<%@ include file="fragments/footer.jsp" %>