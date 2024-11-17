// when loading the page, send the request for the current schedule
window.onload = function () {
	const today = new Date();
	const formattedDate = today.toISOString().split('T')[0]; // YYYY-MM-DD
	fetchAndUpdateByDate(formattedDate);
};


function handleDateChange() {
	const selectedDate = document.getElementById('date-picker').value;
	fetchAndUpdateByDate(selectedDate);
}

// send a request to the server
function fetchAndUpdateByDate(date) {
	if (!date) {
		console.error('Invalid date');
		return;
	}

	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	let requestedId;
	if (urlParams.has('id')) {
		requestedId = urlParams.get('id');
	}else{
		console.error('no id specified');
		return;
	}

	const [year, month, day] = date.split('-');
	const url = `/schedule/get?day=${day}&month=${month}&year=${year}&id=${requestedId}`;

	fetch(url)
		.then(response => response.json())
		.then(data => updateCalendar(data)) // Appelle une fonction pour remplir le calendrier
		.catch(error => console.error('Error fetching data:', error));
}

// update the schedule
function updateCalendar(data) {
	// update day number
	const days = document.querySelectorAll('.day');
	Object.keys(data).forEach((dayIndex, i) => {
		const dayElement = days[i];
		const dayNum = dayElement.querySelector('.date-num');
		const eventsContainer = dayElement.querySelector('.events');

		// update
		dayNum.textContent = dayIndex;

		// delete event of last consulted week
		eventsContainer.innerHTML = '';

		// add new events
		data[dayIndex].forEach(event => {
			const eventDiv = document.createElement('div');
			eventDiv.className = `event start-${event.startTime} end-${event.endTime} ${event.type}`;
			eventDiv.setAttribute('onclick', 'openPopup(this)');
			eventDiv.innerHTML = `
        <p class="title">${event.title}</p>
        <p class="time">${event.startTime} - ${event.endTime}</p>
        <div class="hidden-info" style="display: none;">
          <span class="data-room">Room: ${event.room}</span>
          <span class="data-professor">Professor: ${event.professor}</span>
          <span class="data-classes">Classes: ${event.classes.join(', ')}</span>
        </div>
      `;
			eventsContainer.appendChild(eventDiv);
		});
	});
}







function openPopup(eventElement) {
	const title = eventElement.querySelector('.title').textContent;
	const details = eventElement.querySelector('.time').textContent;
	const room = eventElement.querySelector('.data-room').textContent;
	const professor = eventElement.querySelector('.data-professor').textContent;
	const classes = eventElement.querySelector('.data-classes').textContent;

	document.getElementById('popup-title').textContent = title;
	document.getElementById('popup-details').textContent = details;
	document.getElementById('popup-room').textContent = "Room: " + room;
	document.getElementById('popup-professor').textContent = "Professor: " + professor;
	document.getElementById('popup-classes').textContent = "Classes: " + classes;
	document.getElementById('popup').style.display = 'flex';
}



function closePopup(event) {
	const popupContent = document.querySelector('.popup-content');
	if (!popupContent.contains(event.target)) {
		document.getElementById('popup').style.display = 'none';
	}
}
