function GetAccessToken() {
	console.log('Before fetch')
	fetch('/auth/token')
		.then(response => {
			console.log('Got response', response)
			if (!response.ok) {
				throw new Error('Network response was not ok')
			}
			return response.json()
		})
		.then(data => {
			console.log('Got data', data)
			const accessToken = data.access_token
			sessionStorage.setItem('access_token', accessToken)
		})
		.catch(error => {
			console.error('Error fetching access token', error)
		})
}
