function openPopup(element, isNew, id, subjectId, professorId, classroom, groupIds) {
	const groupsDropdown = document.querySelector('select[name="groupsId"]');
	if (isNew === false) {
		document.querySelector('.id').value = id;
		document.querySelector('select[name="subjectId"]').value = subjectId;
		document.querySelector('select[name="professorId"]').value = professorId;
		document.querySelector('.classroom').value = classroom;

		// cocher les groupes dans la liste déroulante
		Array.from(groupsDropdown.options).forEach(option => {
			option.selected = groupIds.includes(option.value);
		});

		document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';
	} else {
		// vider tous les champs
		document.querySelector('.id').value = "";
		document.querySelector('select[name="subjectId"]').value = "";
		document.querySelector('select[name="professorId"]').value = "";
		document.querySelector('.classroom').value = "";

		Array.from(groupsDropdown.options).forEach(option => {
			option.selected = false;
		});

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