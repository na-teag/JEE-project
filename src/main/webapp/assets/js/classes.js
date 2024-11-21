function openPopup(element, isNew, name, id, pathwayId, promoId, email) {
	if (isNew == false) {
		document.querySelector('.name').value = name;
		document.querySelector('.id').value = id;
		document.querySelector('.email').value = email;
		document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';

		// mettre la promo de la classe sélectionnée
		const promoDropdown = document.querySelector('select[name="promoId"]');
		promoDropdown.value = promoId;

		// mettre le pathway de la classe sélectionnée
		const pathwayDropdown = document.querySelector('select[name="pathwayId"]');
		pathwayDropdown.value = pathwayId;
	} else {
		document.querySelector('.name').value = "";
		document.querySelector('.id').value = "";
		document.querySelector('.email').value = "";
		document.querySelector('select[name="promoId"]').value = "";
		document.querySelector('select[name="pathwayId"]').value = "";
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