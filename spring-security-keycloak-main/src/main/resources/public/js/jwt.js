const keycloak = new Keycloak({
	url: 'http://localhost:8080/',
	realm: 'eselpo',
	clientId: 'springsecurity',
	clientSecret: 'yJC7CbTdxjIx24RzmFsYONYqCVH9Cl4Q',
})

window.onload = function () {
	keycloak
		.init({ onLoad: 'login-required' })
		.then(function (authenticated) {
			console.log(authenticated ? 'Authenticated' : 'Not Authenticated')
			localStorage.setItem('access_token', keycloak.token)
			localStorage.setItem('refresh_token', keycloak.refreshToken)
		})
		.catch(function () {
			console.error('Failed to initialize')
		})
}
