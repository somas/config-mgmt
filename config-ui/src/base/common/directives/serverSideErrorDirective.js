(function() {
    'use strict';
	angular.module('configApp').directive('stFormErrors', ['$log', 'globalHandleErrorService', function ($log, globalHandleErrorService) {
	    return {
	        restrict: "A",
	        replace: false,
	        controller: ['$scope', '$element', function($scope, $element) {
	        	var ctrls = {};
	        	var formLevelErrorsId = 'top_lvl_errors';
	        	
	        	this.addCtrl = function(fieldName, ctrl) {
	        		ctrls[fieldName] = ctrl;
	        	};
	        	
	        	this.addError = function(fieldName, error) {
	        		if (!(fieldName in ctrls)) return;
	                return ctrls[fieldName].addError(fieldName, error);
	        	};
	        	
	        	this.clearErrorsFor = function(fieldName) {
	        	    if (!(fieldName in ctrls)) return;
	        	    return ctrls[fieldName].clearErrors();
	        	};
	
	        	this.clearFormLevelErrors = function() {
	        		this.clearErrorsFor(formLevelErrorsId);
	        	};
	        	
	        	this.clearAllErrors = function() {
	        		angular.forEach(ctrls, function(value, key){
	        			ctrls[key].clearErrors();
	        		});
	        	};
	        }],
	        
	        link: function(scope, element, attr, ctrl) {
	        	globalHandleErrorService.registerForm(attr.name, ctrl);
	        }
	    };
	}]);
	
	
	angular.module('configApp').directive('stFieldErrors', ['$log', '$compile', function ($log, $compile) {
		
		var errors = {
		};
		
		var getTemplate = function() {
			return '<label class="control-label" ng-repeat="(key, value) in errorMap" ng-show="validateOnSubmit">{{value | translate}}</label>';
		};
		
	    return {
	    	scope: true,
	        restrict: "A",
	        replace: false,
	        require: ['?ngModel', 'stFieldErrors', '^stFormErrors'],
	        controller: ['$scope', function($scope) {
	            $scope.errorMap = {};
	            
	            this.addError = function(field, error) {
	              $scope.errorMap[field] = error;
	            };
	
	            this.removeError = function(field) {
	            	delete $scope.errorMap[field];
	            };
	            
	            this.clearErrors = function() {
	              $scope.errorMap = {};
	            };
	            
	          }],
	        link: function(scope, element, attrs, ctrl) {
	        	var ngModelCtrl = ctrl[0];
	        	var stFieldErrorsCtrl = ctrl[1];
	        	var stFormErrorsCtrl = ctrl[2];
	        	
	        	stFormErrorsCtrl.addCtrl(attrs.name, stFieldErrorsCtrl);
	
	        	var el = getTemplate();
	        	var compiled = $compile(el)(scope);
	        	
	        	if(element.parent().find('label').length === 0) {
	        		element.parent().parent().find('label').after(compiled);
	        	} else {
	        		element.parent().find('label').after(compiled);
	        	}
	        	
	        	var clearFormLevelErrors = function() {
	        		stFormErrorsCtrl.clearFormLevelErrors();
	        	};
	        	
	        	if(!ngModelCtrl) {
	        		return;
	        	}
	        	
	        	scope.$watch(function () {
	                return ngModelCtrl.$viewValue;
	             }, function(newValue) {
	            	 $log.info("scope.validateOnSubmit : " + scope.validateOnSubmit);
	            	 clearFormLevelErrors();
	                 if (ngModelCtrl.$invalid) {
	                	 for(var key in ngModelCtrl.$error) {
	                		 $log.info('key: ' + key + ' value: ' + ngModelCtrl.$error[key]);
	                		 var tempFieldKey = attrs.name + '.' + key;
	                		 if(ngModelCtrl.$error[key]) {
	                			 stFieldErrorsCtrl.addError(tempFieldKey, tempFieldKey);
	                		 } else {
	                			 stFieldErrorsCtrl.removeError(tempFieldKey);
	                		 }
	                	 }
	               	  	 $log.info('Data : ' +  ngModelCtrl.$viewValue +' ,ngModelCtrl error: ' + ngModelCtrl.$error);
	                 } else if (ngModelCtrl.$valid) {
	                  stFieldErrorsCtrl.clearErrors();
	               	  $log.info('ngModelCtrl error: no errors');
	                 }
	             });
	        }
	    };
	}]);

})();