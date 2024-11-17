<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/schedule.css">

<div class="main-content">
	<c:choose>
		<c:when test="${not empty errorMessage}">
			<p style="color: red;">${errorMessage}</p>
		</c:when>
		<c:otherwise>
			<label for="date-picker">Select a date:</label>
			<input type="date" id="date-picker" onchange="handleDateChange()" />
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
					<div class="day lun">
						<div class="date">
							<p class="date-day">Lun.</p>
							<p class="date-num"></p>
						</div>
						<div class="events">
							<div class="event start-08h00 end-08h45 securities" onclick="openPopup(this)">
								<p class="title">Securities Regulation</p>
								<p class="time">08h00 - 08h45</p>
								<div class="hidden-info" style="display: none;">
									<span class="data-room">Room 202</span>
									<span class="data-professor">Dr. John Doe</span>
									<span class="data-classes">Class A, Class B</span>
								</div>
							</div>
						</div>
					</div>
					<div class="day mar">
						<div class="date">
							<p class="date-day">Mar.</p>
							<p class="date-num"></p>
						</div>
						<div class="events">
						</div>
					</div>
					<div class="day mer">
						<div class="date">
							<p class="date-day">Mer.</p>
							<p class="date-num"></p>
						</div>
						<div class="events">
						</div>
					</div>
					<div class="day jeu">
						<div class="date">
							<p class="date-day">Jeu.</p>
							<p class="date-num"></p>
						</div>
						<div class="events">
						</div>
					</div>
					<div class="day ven">
						<div class="date">
							<p class="date-day">Ven.</p>
							<p class="date-num"></p>
						</div>
						<div class="events">
						</div>
					</div>
				</div>
			</div>
			<div class="popup" id="popup" onclick="closePopup(event)">
				<div class="popup-content">
					<h3 id="popup-title">Event Details</h3>
					<p id="popup-details">Details will appear here...</p>
					<p id="popup-room"></p>
					<p id="popup-professor"></p>
					<p id="popup-classes"></p>
				</div>
			</div>
			<script src="${pageContext.request.contextPath}/assets/js/schedule.js"></script>
		</c:otherwise>
	</c:choose>

</div>

<%@ include file="fragments/footer.jsp" %>
