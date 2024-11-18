<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/schedule.css">

<div class="main-content">
	<c:choose>
		<c:when test="${not empty errorMessage}">
			<p style="color: red;">${errorMessage}</p>
		</c:when>
		<c:otherwise>
			<label for="date-picker">Séléctionnez une date:</label>
			<input type="date" id="date-picker" /><br>
			<label for="id-picker">Entrez un numéro (facultatif) :</label>
			<input type="text" id="id-picker" /><br>
			<input type="submit" onclick="handleDateChange()">
			<div class="calendar">
				<div class="timeline">
					<div class="spacer"></div>
					<div class="spacer"></div>
					<div class="time-marker">08h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">08h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">09h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">09h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">10h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">10h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">11h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">11h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">12h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">12h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">13h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">13h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">14h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">14h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">15h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">15h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">16h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">16h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">17h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">17h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">18h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">18h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">19h00</div>
					<div class="time-marker"></div>
					<div class="time-marker">19h30</div>
					<div class="time-marker"></div>
					<div class="time-marker">20h00</div>
				</div>
				<div class="days">
					<c:forEach var="days" items="${sessionScope.days}">
						<c:set var="dayName" value="${days.key}" />
						<c:set var="dayNumero" value="${days.value}" />
						<div class="day">
							<div class="date">
								<p class="date-day">
									<c:choose>
										<c:when test="${dayName == 'MONDAY'}">Lun.</c:when>
										<c:when test="${dayName == 'TUESDAY'}">Mar.</c:when>
										<c:when test="${dayName == 'WEDNESDAY'}">Mer.</c:when>
										<c:when test="${dayName == 'THURSDAY'}">Jeu.</c:when>
										<c:when test="${dayName == 'FRIDAY'}">Ven.</c:when>
									</c:choose>
								</p>
								<p class="date-num">${dayNumero}</p>
							</div>
							<div class="events">
								<c:forEach var="dayEntry" items="${sessionScope.schedule}">
									<c:set var="dayName2" value="${dayEntry.key}" />
									<c:set var="events" value="${dayEntry.value}" />
									<c:if test="${dayName == dayName2}">
										<c:forEach var="event" items="${events}">
											<div class="event start-${event.startTime} end-${event.endTime}" style="background-color: ${event.color};" onclick="openPopup(this)">
												<p class="title">${event.title}</p>
												<p class="time">${event.startTime} - ${event.endTime}</p>
												<div class="hidden-info" style="display: none;">
													<span class="data-room">${event.room}</span>
													<span class="data-professor">${event.professor}</span>
													<span class="data-type">${event.type}</span>
													<span class="data-classes">${event.classes}</span>
												</div>
											</div>
										</c:forEach>
									</c:if>
								</c:forEach>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
			<div class="popup" id="popup" onclick="closePopup(event)">
				<div class="popup-content">
					<h3 id="popup-title"></h3>
					<p id="popup-details"></p>
					<p id="popup-room"></p>
					<p id="popup-professor"></p>
					<p id="popup-classes"></p>
					<p id="popup-type"></p>
				</div>
			</div>
			<script src="${pageContext.request.contextPath}/assets/js/schedule.js"></script>
		</c:otherwise>
	</c:choose>

</div>

<%@ include file="fragments/footer.jsp" %>
