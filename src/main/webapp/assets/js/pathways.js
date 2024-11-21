function openPopup(element, isNew, name, id) {
	if (!isNew) {
		document.querySelector('.name').value = name;
		document.querySelector('.id').value = id;
		document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';
		window.isNew = false;
	} else {
		document.querySelector('.name').value = "";
		document.querySelector('.id').value = "";
		document.querySelector('button[name="action"][value="delete"]').style.display = 'none';
		window.isNew = true;
	}
	document.getElementById('popup').style.display = 'flex';
}




function closePopup(event) {
	const popupContent = document.querySelector('.popup-content');
	if (!popupContent.contains(event.target)) {
		document.getElementById('popup').style.display = 'none';
	}
}