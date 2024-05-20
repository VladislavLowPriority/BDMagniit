async function data_save() {
	var data = {
		identificationNumber: document.getElementById('IdentificationNumber').value,
		fullName: document.getElementById('FullName').value,
		workPhone: document.getElementById('WorkPhone').value,
		cellPhone: document.getElementById('CellPhone').value,
		direction: document.getElementById('Direction').value,
		department: document.getElementById('Department').value,
		branch: document.getElementById('Branch').value,
		office: document.getElementById('Office').value,
		jobTitle: document.getElementById('JobTitle').value,
	}

	let token = sessionStorage.getItem('access_token')
	console.log('Access Token:', token)

	fetch('http://localhost:8081/api/user/save', {
		credentials: 'include',
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			Authorization: 'Bearer ' + token,
		},
		body: JSON.stringify(data),
	})
		.then(response => response.json())
		.then(data => {
			console.log('Success:', data)
		})
		.catch(error => {
			console.error('Error:', error)
		})

	// Предотвращаем действие формы по умолчанию (перезагрузка страницы)
	event.preventDefault()
	location.reload()
}
