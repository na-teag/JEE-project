function openPopup(element, isNew, name, id, email) {
	if (!isNew) {
		document.querySelector('.name').value = name;
		document.querySelector('.id').value = id;
		document.querySelector('.email').value = email;
		document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';
	} else {
		document.querySelector('.name').value = "";
		document.querySelector('.id').value = "";
		document.querySelector('.email').value = "";
		document.querySelector('button[name="action"][value="delete"]').style.display = 'none';
	}
	document.getElementById('popup').style.display = 'flex';
}




function closePopup(event) {
	const popupContent = document.querySelector('.popup-content');
	if (!popupContent.contains(event.target)) {
		document.getElementById('popup').style.display = 'none';
	}
}