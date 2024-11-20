function openPopup(element, isNew, classeName, classeId, pathwayId, promoId, email) {
	if (isNew == false) {
		document.querySelector('.classeName').value = classeName;
		document.querySelector('.classeId').value = classeId;
		document.querySelector('.email').value = email;
		document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';

		// cocher la promo de la classe séléctionnée
		const promoOptions = document.querySelectorAll('#promo-options input');
		promoOptions.forEach(promo => {
			promo.checked = promo.value === promoId;
		});

		// cocher le pathway de la classe séléctionnée
		const pathwayOptions = document.querySelectorAll('#pathway-options input');
		pathwayOptions.forEach(pathway => {
			pathway.checked = pathway.value === pathwayId;
		});
		window.isNew = false;
	} else {
		// put the parameters to empty values if it is the creation case
		document.querySelector('.classeName').value = "";
		document.querySelector('.classeId').value = "";
		document.querySelector('.email').value = "";
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