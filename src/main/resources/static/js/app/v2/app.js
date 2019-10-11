(function() {
    'use strict';

    // angular
    //     .module('bookApp', ['ui.router', 'ngMessages', 'ngStorage', 'ngMockE2E'])
    //     .config(config)
    //     .run(run);

    var app = angular
	    .module('bookApp', [ 'ngStorage', 'ngRoute', 'ui.router' ]);
    //var app = angular.module('bookApp',['ui.router','ngStorage']);

    app.constant('urls', {
	BASE : 'http://localhost:8082/',
	AUTH_SERVICE_API : 'http://localhost:8082/api/auth/',
	BOOK_SERVICE_API : 'http://localhost:8082/api/book/',
	FILE_SERVICE_API : 'http://localhost:8082/api/files/'
    });

    // app.directive('fileModel', ['$parse', 'FileService', function ($parse, FileService) {
    //  return {
    //      restrict: 'A',
    //      link: function(scope, element) {
    //          element.bind('change', function(){
    //              scope.$apply(function(){
    //                  if (element[0].files != undefined) {
    //                      FileService.push(element[0].files[0]);
    //                      console.log('directive applying with file');
    //                  }
    //              });
    //          });
    //      }
    //  };
    //}]);

    app.directive('uploadFiles', function() {
	return {
	    scope : true, //create a new scope  
	    link : function(scope, el, attrs) {
		el.bind('change', function(event) {
		    var files = event.target.files;
		    //iterate files since 'multiple' may be specified on the element  
		    for (var i = 0; i < files.length; i++) {
			//emit event upward  
			scope.$emit("fileSelected", {
			    file : files[i]
			});
			console.log('from #uploadFiles directive: emit event#:'
				+ i);
		    }
		});
	    }
	};
    });

    app.config(config).run(run);

    function config($stateProvider, $routeProvider, $urlRouterProvider) {

	// default route
	$urlRouterProvider.otherwise("/");

	$routeProvider.when('/', {
	    templateUrl : 'partials/login',
	    controller : 'Login.IndexController',
	    controllerAs : 'authCtrl'
	}).when('/login', {
	    templateUrl : 'partials/login',
	    controller : 'Login.IndexController',
	    controllerAs : 'authCtrl'
	}).when('/signin', {
	    templateUrl : 'partials/login',
	    controller : 'Login.IndexController',
	    controllerAs : 'authCtrl'
	}).when('/signup', {
	    templateUrl : 'partials/login',
	    controller : 'Login.IndexController',
	    controllerAs : 'authCtrl'
	}).when(
		'/book',
		{
		    templateUrl : 'partials/book',
		    controller : 'BookController',
		    controllerAs : 'ctrlBook',
		    resolve : {
			books : function($q, BookService) {
			    console.log('Load all books');
			    var deferred = $q.defer();
			    BookService.loadAllBooks().then(deferred.resolve,
				    deferred.resolve);
			    //$location.path('/api/book');
			    return deferred.promise;
			}
		    }
		});

	// app routes
	$stateProvider.state('home', {
	    url : '/',
	    templateUrl : 'partials/login',
	    controller : 'Home.IndexController',
	    controllerAs : 'vm'
	}).state('login', {
	    url : '/login',
	    templateUrl : 'partials/login',
	    controller : 'Login.IndexController',
	    controllerAs : 'vm'
	}).state(
		'book',
		{
		    url : '/book',
		    templateUrl : 'partials/book',
		    controller : 'BookController',
		    controllerAs : 'bookCtrl',
		    resolve : {
			books : function($q, BookService) {
			    console.log('Load all books');
			    var deferred = $q.defer();
			    BookService.loadAllBooks().then(deferred.resolve,
				    deferred.resolve);
			    return deferred.promise;
			}
		    }
		});
    }

    function run($rootScope, $http, $location, $localStorage) {
	// keep user logged in after page refresh
	if ($localStorage.currentUser) {
	    $http.defaults.headers.common.Authorization = 'Bearer '
		    + $localStorage.currentUser.token;
	}

	// redirect to login page if not logged in and trying to access a restricted page
	$rootScope.$on('$locationChangeStart', function(event, next, current) {
	    var publicPages = [ '/login' ];
	    var restrictedPage = publicPages.indexOf($location.path()) === -1;
	    if (restrictedPage && !$localStorage.currentUser) {
		$location.path('/login');
	    }
	});
    }
})();