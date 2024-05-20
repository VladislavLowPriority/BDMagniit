document.getElementById('logoutButton').addEventListener('click', function () {
	// Очистка кэша сервис-воркера, если используется
	if ('caches' in window) {
		caches.keys().then(keyList => {
			return Promise.all(
				keyList.map(key => {
					return caches.delete(key)
				})
			)
		})
	}
	let token = sessionStorage.getItem('access_token')
	console.log('Access Token:', token)
	fetch('/logout', {
		credentials: 'include',
		method: 'GET',
		mode: 'no-cors',
		headers: {
			Authorization: 'Bearer ' + token,
		},
	})
		.then(function (response) {
			// Очистка данных формы и localStorage/sessionStorage должна происходить только после успешного выхода
			if (response.ok) {
				sessionStorage.removeItem('access_token') // Удаляем токен доступа из sessionStorage
				// Очистка полей формы
				document.getElementById('IdentificationNumber').value = ''
				document.getElementById('FullName').value = ''
				document.getElementById('WorkPhone').value = ''
				document.getElementById('CellPhone').value = ''
				document.getElementById('Direction').value = ''
				document.getElementById('Department').value = ''
				document.getElementById('Branch').value = ''
				document.getElementById('Office').value = ''
				document.getElementById('JobTitle').value = ''

				// Перенаправление на главную страницу или страницу входа
				window.location.href = 'http://localhost:8081/homepage.html'
			} else {
				console.error('Logout failed')
				window.location.href = 'http://localhost:8081/homepage.html'
			}
		})
		.catch(function (error) {
			console.error('Error:', error)
		})
})
