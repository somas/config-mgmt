(function() {
    'use strict';

	var app = angular.module('configApp',
			[ 'admin',
			  'security', 
			  'pascalprecht.translate', 
			  'ui.bootstrap',
			  'templates-dist', 
			  'ui.router' ]);
	
	app.config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
		$urlRouterProvider.otherwise('/');
		
		$stateProvider.state('home', {
			url: '/'
			//templateUrl: 'base/app/admin/admin-properties.tpl.html',
		});
	}]);
	
	app.run(['$rootScope', '$stateParams', 'principal', '$sessionStorage', '$state', '$location', function($rootScope, $stateParams, principal, $sessionStorage, $state, $location) {
		$rootScope.$on('$stateChangeStart', function(event, toState, toStateParams) {
			
			if(toState.authRequired && !principal.isAuthenticated()) {
				if (angular.isUndefined($sessionStorage.accessToken)) {
					event.preventDefault();
					$sessionStorage.destUrl = $location.path();
					$state.go('login');
				} else {
					event.preventDefault();
					console.log('error - permission denied');
					$state.go('error');
				}
			}
		});
	}]);
	
	angular.isUndefinedOrNull = function(val) {
	    return angular.isUndefined(val) || val === null ;
	};

})();