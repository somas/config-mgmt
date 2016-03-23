(function() {
    'use strict';
	angular.module('admin').factory('adminService', ['$resource', function($resource, $scope) {
			return $resource('/properties/:item_key/:field_key/:version', {}, {
				update: {method: 'PUT'}
		    });
	  }]);
	
	angular.module('admin').factory('adminVersionService', ['$resource', function($resource, $scope) {
		return $resource('/properties/:item_key/:field_key/versions', {}, {
	    });
	}]);
})();