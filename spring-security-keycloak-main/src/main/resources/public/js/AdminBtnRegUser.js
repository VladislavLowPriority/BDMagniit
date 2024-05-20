document.addEventListener('DOMContentLoaded', function () {
	const registerForm = document.getElementById('registerUserForm')
	if (registerForm) {
		registerForm.addEventListener('submit', async function (event) {
			event.preventDefault()

			try {
				const tokenResponse = await fetch('/auth/token')
				if (!tokenResponse.ok) {
					throw new Error('Network response was not ok')
				}
				const tokenData = await tokenResponse.json()
				const accessToken = tokenData.access_token

				sessionStorage.setItem('access_token', accessToken)

				if (!accessToken) {
					console.log('Access token is null or undefined')
					return
				}

				const userData = {
					username: document.getElementById('username').value,
					email: document.getElementById('email').value,
					firstName: document.getElementById('firstName').value,
					lastName: document.getElementById('lastName').value,
					credentials: [
						{
							type: 'password',
							value: document.getElementById('password').value,
							temporary: false,
						},
					],
				}

				const createUserResponse = await fetch('/admin/register', {
					method: 'POST',
					credentials: 'include',
					headers: {
						'Content-Type': 'application/json',
						Authorization: 'Bearer ' + accessToken,
					},
					body: JSON.stringify(userData),
				})

				if (!createUserResponse.ok) {
					throw new Error('Network response was not ok')
				}

				const responseData = await createUserResponse.json()
				if (responseData.success) {
					console.log('User registered successfully')
				} else {
					console.error('User registration failed')
				}
			} catch (error) {
				console.error('Error:', error)
			}
		})
	}
})
