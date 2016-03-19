(function() {
    'use strict';
    
	angular.module('security').factory('principal', ['$http', '$timeout', '$rootScope', function($http, $timeout, $rootScope) {
		var principal;
		var times = 0;
		var authenticated = false;
		
		return {
			
			
			isResolved: function() {
				return angular.isDefined(this.principal);
			},
			get: function() {
				return this.principal;
			},
			set: function(principal) {
				this.authenticated = true;
				this.principal = principal;
				this.fetch();
			},
			isAuthenticated: function() {
				return this.authenticated;
			},
			fetch: function asyncFetch() {
			},
			isInAuthority: function(permission) {
				if (!this.isAuthenticated() || !this.principal.authorities) return false;
		        return this.principal.authorities.indexOf(permission) != -1;
			},
			isAuthorized: function(permissionsArr) {
				var status = false;
				if(angular.isUndefined(permissionsArr)) {
					status = true;
				} else {
					if(this.isResolved()) {
						 for (var i = 0; i < permissionsArr.length; i++) {
					          if (this.isInAuthority(permissionsArr[i])) {
					        	  status = true;
					          }
					     }
					}
				}
				return status;
			}
		};
	}]);

})();
