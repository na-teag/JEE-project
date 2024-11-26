function openPopupStudent(element, isNew, id, email, lastName, firstName, number, street, city, postalCode, country, classeId){
	if (isNew === true){
		resetFormFields();
	} else {
		writeFormCommonFields(id, email, lastName, firstName, number, street, city, postalCode, country);
		// séléctionner la classe
		document.querySelector('select[name="classeId"]').value = classeId;
		document.getElementById('birthday').removeAttribute('required');
		document.querySelector('.birthday').hidden = true;
	}
	// changer la valeur du bouton pour pouvoir identifier le requete dans le back
	document.querySelector('button[name="action"][id="save"]').value = "saveStudent";
	// cacher la section des sujets
	document.getElementById('subject').style.display = 'none';
	// ajouter le required et montrer la section des classes
	document.querySelector('select[name="classeId"]').setAttribute('required', 'required');
	document.getElementById('classe').style.display = 'flex';
	// afficher la popup
	document.getElementById('popup').style.display = 'flex';
}


function openPopupProf(element, isNew, id, email, lastName, firstName, number, street, city, postalCode, country, subjectIds){
	if (isNew === true){
		resetFormFields();
	} else {
		writeFormCommonFields(id, email, lastName, firstName, number, street, city, postalCode, country);
		// cocher les sujets
		const subjectDropdown = document.querySelector('select[name="subjectId"]');
		Array.from(subjectDropdown.options).forEach(option => {
			option.selected = subjectIds.includes(option.value);
		});
		document.getElementById('birthday').removeAttribute('required');
		document.querySelector('.birthday').hidden = true;
	}
	// changer la valeur du bouton pour pouvoir identifier le requete dans le back
	document.querySelector('button[name="action"][id="save"]').value = "saveProf";
	// enlever le required et cacher la section des classes
	document.querySelector('select[name="classeId"]').removeAttribute('required');
	document.getElementById('classe').style.display = 'none';
	// montrer la section des sujets
	document.getElementById('subject').style.display = 'flex';
	// afficher la popup
	document.getElementById('popup').style.display = 'flex';
}


function openPopupAdmin(element, isNew, id, email, lastName, firstName, number, street, city, postalCode, country){
	if (isNew === true){
		resetFormFields();
	} else {
		writeFormCommonFields(id, email, lastName, firstName, number, street, city, postalCode, country);
		document.getElementById('birthday').removeAttribute('required');
		document.querySelector('.birthday').hidden = true;
	}
	// changer la valeur du bouton pour pouvoir identifier le requete dans le back
	document.querySelector('button[name="action"][id="save"]').value = "saveAdmin";
	// cacher la section des classes et des sujets
	document.querySelector('select[name="classeId"]').removeAttribute('required');
	// cacher la séléction des classes et des sujets
	document.getElementById('subject').style.display = 'none';
	document.getElementById('classe').style.display = 'none';
	// afficher la popup
	document.getElementById('popup').style.display = 'flex';
}



function resetFormFields() {
	// Réinitialiser les champs
	document.querySelector('.firstName').value = "";
	document.querySelector('.lastName').value = "";
	document.querySelector('.birthday').value = "";
	document.querySelector('.email').value = "";
	document.querySelector('.number').value = "";
	document.querySelector('.street').value = "";
	document.querySelector('.city').value = "";
	document.querySelector('.postalCode').value = "";
	document.querySelector('.country').value = "";
	document.querySelector('.id').value = "";
	document.querySelector('select[name="classeId"]').value = "";

	const subjectDropdown = document.querySelector('select[name="subjectId"]');
	Array.from(subjectDropdown.options).forEach(option => {
		option.selected = false;
	});
	document.getElementById('birthday').setAttribute('required', 'required');
	document.querySelector('.birthday').hidden = false;
	document.querySelector('button[name="action"][value="delete"]').style.display = 'none';
}



function writeFormCommonFields(id, email, lastName, firstName, number, street, city, postalCode, country){
	document.querySelector('.id').value = id;
	document.querySelector('.birthday').value = birthday;
	document.querySelector('.firstName').value = firstName;
	document.querySelector('.lastName').value = lastName;
	document.querySelector('.email').value = email;
	document.querySelector('.number').value = number;
	document.querySelector('.street').value = street;
	document.querySelector('.city').value = city;
	document.querySelector('.postalCode').value = postalCode;
	document.querySelector('.country').value = country;
	document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';
}




function closePopup(event) {
	const popupContent = document.querySelector('.popup-content');
	if (!popupContent.contains(event.target)) {
		document.getElementById('popup').style.display = 'none';
	}
}