(function() {
    'use strict';
	angular.module('security.login')
		.factory('loginService', ['$resource', function($resource, $scope) {
			return $resource('/login/auth', {}, {
				login: {method: 'POST', headers : {'Content-Type': 'application/x-www-form-urlencoded'}}
		    });
	  }]);
})();