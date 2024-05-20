document.querySelector('#submitForm').addEventListener('click', function (e) {
	e.preventDefault()
	document.querySelector('#confirm').style.display = 'block'
})

document.querySelector('#yes').addEventListener('click', function () {
	document.querySelector('#confirm').style.display = 'none'
	alert('Пользователь создан!')
})

document.querySelector('#no').addEventListener('click', function () {
	document.querySelector('#confirm').style.display = 'none'
})
