(function () {
    'use strict';

    angular
        .module('bookApp')
        .factory('AuthenticationService', Service);

    function Service($http, $localStorage, $q, urls) {
        var service = {};

        service.login = login;
        service.logout = logout;
        service.registerUser = registerUser;

        return service;

        function logout() {
            // remove user from local storage and clear http auth header
            delete $localStorage.currentUser;
            //delete $localStorage.headersConfig;
            $http.defaults.headers.common.Authorization = '';
            console.log('Logged out!');
        }

        function login(loginRequest, callback) {

            var signinUrl = urls.AUTH_SERVICE_API + 'signin';

            console.log('Authenticating user...');
            console.log('loginRequest: ' + loginRequest);
            console.log('loginRequest.usernameOrEmail: ' + loginRequest.usernameOrEmail);
            console.log('loginRequest.password: ' + loginRequest.password);
            console.log('signinUrl: ' + signinUrl);

            $http.post(signinUrl, loginRequest)
                .success(function (response) {

                    // login successful if there's a token in the response
                    if (response.accessToken) {

                        console.log('accessToken: ' + response.accessToken);
                        // store username and token in local storage to keep user logged in between page refreshes
                        $localStorage.currentUser = { username: loginRequest.usernameOrEmail, token: response.accessToken };
                        console.log('$localStorage.currentUser: ' + $localStorage.currentUser.username);
                        // add jwt token to auth header for all requests made by the $http service
                        $http.defaults.headers.common.Authorization = 'Bearer ' + response.accessToken;

                        // execute callback with true to indicate successful login
                        callback(true);
                    } else {
                        // execute callback with false to indicate failed login
                        callback(false);
                    }
                });
        }

        function registerUser(signupRequest) {

            var deferred = $q.defer();
            var signupUrl = urls.AUTH_SERVICE_API + 'signup';

            console.log('Authenticating user...');
            console.log('signupRequest.username: ' + signupRequest.username);
            console.log('signupRequest.password: ' + signupRequest.password);
            console.log('signupUrl: ' + signupUrl);

            $http.post(signupUrl, signupRequest)
                .then(
                    function (response) {
                        console.log('in registerUser.response...');
                        //loadAllBooks();		
                        //deferred.resolve(response.accessToken);
                        //deferred.resolve(response.data);								
                    },
                    function (errResponse) {
                        console.error('Error while signing up... : ' + errResponse.data.errorMessage);
                        deferred.reject(errResponse);
                    }
                );
            return deferred.promise;
        }
    }
})();