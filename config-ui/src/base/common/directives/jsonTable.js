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
                    delete scope.propertyJson[key];
                    scope.data = angular.toJson(scope.propertyJson);
                };

                scope.jsonTable.change = function(id) {
                    var key = $("#key_" + id).val();
                    var value = $("#value_" + id).val();

                    if(key !== '') {
                        scope.propertyJson[key] = value;
                        scope.$apply(function () {
                            scope.data = angular.toJson(scope.propertyJson);
                        });
                    }

                    $("#row_" + id).remove();
                };

                $("#add").click(function() {

                    var newRow = ('<tr id="row_PRONTO"><td><input id="key_PRONTO" type="text" name="key" ng-model="key"></td><td><input id="value_PRONTO" type="text" name="value" ng-model="val" ng-change="jsonTable.change(PRONTO)"/></td><td><button ng-click="jsonTable.delete(PRONTO)">-</button></td></tr>');
                    var id = $('#jsonTable tbody>tr').size();
                    var res = newRow.replace(/PRONTO/g, id);
                    if($('#jsonTable tbody>tr:nth-last-child(2)').size() > 0) {
                        $(res).insertAfter('#jsonTable tbody>tr:nth-last-child(2)');
                    } else {
                        $(res).insertBefore('#jsonTable tbody>tr:nth-last-child(1)');
                    }

                    $( "#value_" + id).change(function() {
                        scope.jsonTable.change(id);
                    });
                    //$('#jsonTable tbody>tr:nth-last-child(2)').clone(true).insertAfter('#jsonTable tbody>tr:nth-last-child(2)');
                    //$('#jsonTable tbody>tr:nth-last-child(2) #key').val('');
                    //$('#jsonTable tbody>tr:nth-last-child(2) #value').val('');
                    return false;
                });
	        }
	    };
	}]);
})();