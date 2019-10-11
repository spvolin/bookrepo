(function() {
    'use strict';

    angular.module('bookApp').controller('Login.IndexController', Controller);

    function Controller($location, $scope, $rootScope, $localStorage,
	    AuthenticationService) {

	$scope.form = {};
	var authCtrl = this;

	authCtrl.successMessage = '';
	authCtrl.errorMessage = '';

	authCtrl.login = login;
	authCtrl.register = register;
	authCtrl.loginRequest = {};
	authCtrl.signupRequest = {};

	initController();

	function initController() {
	    // reset login status
	    AuthenticationService.logout();
	    console.log('Logged out!!!');
	}
	;

	function login() {
	    authCtrl.loading = true;
	    AuthenticationService
		    .login(
			    authCtrl.loginRequest,
			    function(result) {
				if (result === true) {
				    $rootScope.username = $localStorage.currentUser.username;
				    console.log('$scope.username: '
					    + $scope.username);
				    $location.path('/book');
				} else {
				    authCtrl.error = 'Username or password is incorrect';
				    authCtrl.loading = false;
				}
			    });
	}
	;
	function register() {

	    var signupRequest = authCtrl.signupRequest;
	    var username = signupRequest.username;

	    console.log('About to registere user ' + username);
	    AuthenticationService
		    .registerUser(signupRequest)
		    .then(
			    function(response) {
				console.log('User ' + username
					+ ' registered successfully');
				authCtrl.login.successMessage = 'User '
					+ username + ' registered successfully';
				authCtrl.login.errorMessage = '';
				authCtrl.done = true;

				//self.user={};
				//$scope.signupRequest.$setPristine();
				$location.path('/book');
			    },
			    function(errResponse) {
				console.error('Error while registering user: '
					+ username);
				authCtrl.errorMessage = 'Error while registering user: '
					+ usernameOrEmail
					+ ' '
					+ errResponse.data.errorMessage;
				authCtrl.successMessage = '';
			    });
	}
    }

})();