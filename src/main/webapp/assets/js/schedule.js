
function handleDateChange() {
	const selectedDate = document.getElementById('date-picker').value;
	const selectedId = document.getElementById('id-picker').value;
	if (selectedDate) {
		const [year, month, day] = selectedDate.split('-');
		let params = `?day=${day}&month=${month}&year=${year}`;
		if (selectedId != ""){
			params += `&id=${selectedId}`
		}
		const contextPath = `${window.location.pathname.split('/')[1]}`;
		window.location.href = `/${contextPath}/schedule${params}`;
	}
}



function openPopup(eventElement) {
	const title = eventElement.querySelector('.title').textContent;
	const details = eventElement.querySelector('.time').textContent;
	const room = eventElement.querySelector('.data-room').textContent;
	const professor = eventElement.querySelector('.data-professor').textContent;
	const classes = eventElement.querySelector('.data-classes').textContent;
	const type = eventElement.querySelector('.data-type').textContent;

	document.getElementById('popup-title').textContent = title;
	document.getElementById('popup-details').textContent = details;
	document.getElementById('popup-room').textContent = "Room: " + room;
	document.getElementById('popup-professor').textContent = "Professor: " + professor;
	document.getElementById('popup-classes').textContent = "Classes: " + classes;
	document.getElementById('popup-type').textContent = "Type: " + type;
	document.getElementById('popup').style.display = 'flex';
}



function closePopup(event) {
	const popupContent = document.querySelector('.popup-content');
	if (!popupContent.contains(event.target)) {
		document.getElementById('popup').style.display = 'none';
	}
}