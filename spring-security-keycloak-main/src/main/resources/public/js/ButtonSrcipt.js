document.addEventListener('DOMContentLoaded', function () {
	var employeeButton = document.getElementById('employeeButton') // Получаем кнопку по ID
	employeeButton.addEventListener('click', function () {
		// Добавляем обработчик события клика
		window.location.href = 'User.html' // Перенаправляем на index2.html
	})
})

document.addEventListener('DOMContentLoaded', function () {
	var managerButton = document.getElementById('managerButton') // Получаем кнопку по ID
	managerButton.addEventListener('click', function () {
		// Добавляем обработчик события клика
		window.location.href = 'Admin.html' // Перенаправляем на index2.html
	})
})
