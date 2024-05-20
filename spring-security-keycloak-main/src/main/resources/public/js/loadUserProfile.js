async function loadUserProfile() {
	console.log('Before fetch')

	try {
		const tokenResponse = await fetch('/auth/token')
		console.log('Got response', tokenResponse)
		if (!tokenResponse.ok) {
			throw new Error('Network response was not ok')
		}
		const tokenData = await tokenResponse.json()
		console.log('Got data', tokenData)
		const accessToken = tokenData.access_token

		// Сохраняем токен в sessionStorage
		sessionStorage.setItem('access_token', accessToken)

		// Проверяем, что токен не null или undefined
		if (!accessToken) {
			console.log('Access token is null or undefined')
			return
		}

		// Теперь, когда у нас есть токен, делаем запрос на получение данных пользователя
		const profileResponse = await fetch(
			'http://localhost:8081/api/user/profile',
			{
				credentials: 'include',
				headers: {
					'Cache-Control': 'no-cache',
					Pragma: 'no-cache',
					Authorization: 'Bearer ' + accessToken,
				},
			}
		)

		if (!profileResponse.ok) {
			throw new Error('Network response was not ok')
		}

		const userData = await profileResponse.json()

		// Обновляем DOM с данными пользователя
		document.getElementById('IdentificationNumber').value =
			userData.identificationNumber || ''
		document.getElementById('FullName').value = userData.fullName || ''
		document.getElementById('WorkPhone').value = userData.workPhone || ''
		document.getElementById('CellPhone').value = userData.cellPhone || ''
		document.getElementById('Direction').value = userData.direction || ''
		document.getElementById('Department').value = userData.department || ''
		document.getElementById('Branch').value = userData.branch || ''
		document.getElementById('Office').value = userData.office || ''
		document.getElementById('JobTitle').value = userData.jobTitle || ''
		document.querySelector('.UserForm').style.display = 'block'

		const fullName = document.getElementById('FullName').value
		const nameParts = fullName.trim().split(' ') // Разделяем строку по пробелам
		const name = nameParts.length > 1 ? nameParts[1] : '' // Берем второй элемент, если он есть
		document.getElementById('userFullName').textContent = name // Обновляем текст
	} catch (error) {
		console.error('Error during the user profile fetching process:', error)
	}
}

document.addEventListener('DOMContentLoaded', loadUserProfile)
