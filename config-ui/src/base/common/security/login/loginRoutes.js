(function() {
    'use strict';
	angular.module('security.login').
		config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
			$stateProvider.state('login', {
				url: '/login',
				templateUrl: 'base/common/security/login/login.tpl.html'
			});
	}]);

})();
