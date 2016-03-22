(function() {
    'use strict';
	angular.module('configApp').directive('jsonTable', ['$log', 'globalHandleErrorService', function ($log, globalHandleErrorService) {
	    return {
	        restrict: "E",
	        replace: true,
			scope: {
				data: '=ngModel'
			},
            templateUrl: 'base/common/directives/json-table.tpl.html',
	        link: function(scope, element, attr, ctrl) {
                scope.jsonTable = {};
                scope.$watch('data', function(newValue, oldValue) {
                    if (newValue)
                    console.log(scope.data);
                    if(scope.data != "empty" && scope.data !== "") {
                        var temp = JSON.parse(scope.data);
                        if (temp) {
                            scope.propertyJson = temp;
                        } else {
                            scope.propertyJson = {};
                        }
                        console.log(scope.propertyJson);
                    }
                    console.log("end---------");
                }, true);

                scope.jsonTable.add = function(key, value) {
                    scope.propertyJson[key] = value;
                };

                scope.jsonTable.delete = function(key) {
                    delete propertyJson[key];
                };

                $("#add").click(function() {
                    $('#jsonTable tbody>tr:nth-last-child(2)').clone(true).insertAfter('#jsonTable tbody>tr:nth-last-child(2)');
                    $('#jsonTable tbody>tr:nth-last-child(2) #key').val('');
                    $('#jsonTable tbody>tr:nth-last-child(2) #value').val('');
                    return false;
                });
	        }
	    };
	}]);
})();