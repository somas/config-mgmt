(function() {
    'use strict';
	angular.module('configApp').directive('jsonTable', ['$log', 'globalHandleErrorService', function ($log, globalHandleErrorService) {
	    return {
	        restrict: "E",
	        replace: false,
			scope: {
				data: '=ngModel'
			},
            template: '<li ng-repeat="(key, val) in propertyJson">{{key}}: {{val}}</li>',
	        link: function(scope, element, attr, ctrl) {
                scope.$watch('data', function(newValue, oldValue) {
                    if (newValue)
                    console.log(scope.data);
                    if(scope.data != "empty" && scope.data !== "") {
                        var temp = JSON.parse(scope.data);
                        if (temp) {
                            scope.propertyJson = temp;
                        }
                        console.log(scope.propertyJson);
                    }
                    console.log("end---------");
                }, true);
	        }
	    };
	}]);
})();