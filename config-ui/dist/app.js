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
			
			var isAllowed = principal.isAuthorized(toState.permissions);
			if(!isAllowed) {
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

})();;(function() {
    'use strict';

	angular.module('configApp').controller('NavigationCtrl', ['$scope', '$location', function($scope, $location) {
		 $scope.isActive = function (viewLocation) {
			// if wildcard asterisk is used (to include any subfolders under the provided path)
			if(viewLocation.slice(-2) === "/*") {
				return $location.path().lastIndexOf(viewLocation.slice(0,viewLocation.length-2), 0) === 0;
			}
			else {
				return viewLocation === $location.path();
			}
		 };
	}]);

})();;(function() {
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
})();;(function() {
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

})();;$(document).ready(function() {

});;angular.module('security.login', ['ui.router', 'ngResource']);;(function() {
    'use strict';
	angular.module('security.login')
		.controller('LoginController', ['$scope', 'loginService', 'globalHandleErrorService', '$sessionStorage', '$location', 'principal', function(sc, loginService, globalHandleErrorService, $sessionStorage, $location, principal) {
			console.log("login controller");
			
			sc.login = function(form) {
				  if(form.$invalid) {
					  return;
				  }
				  
				  loginService.login($.param({username: sc.login.username, password: sc.login.password})).$promise.then(
						  function(success) {
							  $sessionStorage.accessToken = success.token;
							  principal.set(success.principal);
							  $location.path($sessionStorage.destUrl);
							  console.log('success in attempted login :' + success.token);
						  }, 
						  function(error) {
							  handleError({error: error.data.errors, formName: 'loginForm'});
						  });
			  };
			  
			  var handleError = function(response) {
				  console.log('error.data.errors: ' + response.error);
				  globalHandleErrorService({
			          formName: response.formName,
			          fieldErrors: response.error
				  });
			  };
		  
	}]);
})();
;(function() {
    'use strict';
	angular.module('security.login').
		config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
			$stateProvider.state('login', {
				url: '/login',
				templateUrl: 'base/common/security/login/login.tpl.html'
			});
	}]);

})();
;(function() {
    'use strict';
	angular.module('security.login')
		.factory('loginService', ['$resource', function($resource, $scope) {
			return $resource('/login/authenticate', {}, {
				login: {method: 'POST', headers : {'Content-Type': 'application/x-www-form-urlencoded'}}
		    });
	  }]);
})();;angular.module('security', ['security.login', 'ngStorage']);;(function() {
    'use strict';

	/**
	 * Http Interceptor for authentication request - 401
	 */
	angular.module('security').config(['$httpProvider', function($httpProvider, $stateProvider) {
		$httpProvider.interceptors.push(['$q', '$rootScope', '$sessionStorage', '$location', function($q, $rootScope, $sessionStorage, $location) {
			return {
				'responseError' : function(rejection) {
					var status = rejection.status;
					var config = rejection.config;
					var method = config.method;
					var url = config.url;
	
					if (angular.isUndefined($sessionStorage.accessToken) && status === 401) {
						$sessionStorage.destUrl = $location.path();
						$location.path("/login");
					} else {
						$rootScope.error = method + " on " + url + " failed with status " + status;
					}
	
					return $q.reject(rejection);
				},
				request : function(config) {
					if(angular.isDefined($sessionStorage.accessToken)) {
						config.headers.Authorization = $sessionStorage.accessToken;
					}
					return config;
				}
			};
		}]);
	}]);

})();;(function() {
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
;var lineDiff = (function() {
	return {
		diffTwoTexts : function(text1, text2) {
			var dmp = new diff_match_patch();
			console.log('text1 : ' + text1 + ' ' + 'text2 : ' + text2);
			var a = dmp.diff_linesToChars_(text2, text1);
			var lineText1 = a.chars1;
			var lineText2 = a.chars2;
			var lineArray = a.lineArray;

			var diffs = dmp.diff_main(lineText1, lineText2, false);

			dmp.diff_charsToLines_(diffs, lineArray);
			var prettyHtml = createPrettyHTML.withDiff(diffs);
			//dmp.diff_cleanupSemantic(diffs);
			console.log('prettyHTML : ' + prettyHtml);
			return prettyHtml;
		}	
	};
	
})();

var createPrettyHTML = (function() {
	var pattern_amp = /&/g;
	var pattern_lt = /</g;
	var pattern_gt = />/g;
	var pattern_para = /\n/g;
	var div_ins_start = '<div style="color:green">';
	var div_delete_start = '<div style="color:red">';
	var div_end = '</div>';
	
	return {
		withDiff: function(diffs) {
			var html = [];
			for (var x = 0; x < diffs.length; x++) {
				var op = diffs[x][0]; // Operation (insert, delete, equal)
				var data = diffs[x][1]; // Text of change.
				var text = data.replace(pattern_amp, '&amp;').replace(pattern_lt,
						'&lt;').replace(pattern_gt, '&gt;').replace(pattern_para,
						'<br>');
				switch (op) {
				case DIFF_INSERT:
					html[x] = div_ins_start + text + div_end;
					break;
				case DIFF_DELETE:
					html[x] = div_delete_start + text + div_end;
					break;
				case DIFF_EQUAL:
					html[x] = '<span>' + text + '</span>';
					break;
				}
			}
			return html.join('');
		}
	};
})();
;(function() {
    'use strict';
angular.module('configApp').factory('flashMessageService', ['$translate', '$rootScope', function($translate, $rootScope) {
	
	  var isError = false;
	  var messageColl = [];
	  
	  $rootScope.$on('$stateChangeStart', 
			  function(event, toState, toParams, fromState, fromParams){
		  messageColl = [];
		  isError = false;
	  });
	  
	  function filterDupAndAdd(value) {
	    	var message = $translate.instant(value);
	    	if(messageColl.indexOf(message) == -1) {
	    		messageColl.push(message);
	    	}
	  }
	  
	  return {
		  setMessage: function(messages) {
			    angular.forEach(messages, function(value) {
			    	filterDupAndAdd(value.message);
				 });
		  },
		  getMessage: function() {
		      return messageColl;
		  },
		  addMessage: function(message) {
			  filterDupAndAdd(message);
		  },
		  setError: function(status) {
			  isError = status;
		  },
		  isError: function() {
			 return isError;
		  },
		  clearAll: function() {
			  messageColl = [];
		  }
	  };
}]);

})();;(function() {
    'use strict';
    
	angular.module('configApp').factory('globalHandleErrorService', ['$translate', '$log', function($translate, $log) {
		  var formColl = [];
	
		  // Used to set server side errors
		  var globalHandleErrorService = function(opts) {
		    var fieldErrors = opts.fieldErrors;
		    var ctrl = formColl[opts.formName];
	
		    if(angular.isUndefinedOrNull(fieldErrors)) {
		    	return;
		    }
		    
		    Object.keys(fieldErrors).forEach(function(index) {
		      $log.info('translated fieldErrorKey : ' + $translate.instant(fieldErrors[index].id) + ' fieldErrors : ' + $translate.instant(fieldErrors[index].message));
		      ctrl.addError($translate.instant(fieldErrors[index].id), $translate.instant(fieldErrors[index].message));
		    });
		  };
	
		  globalHandleErrorService.registerForm = function(formName, ctrl) {
			  formColl[formName] = ctrl;
		  };
		  
		  globalHandleErrorService.clearAllErrors = function(formName) {
			  if(!angular.isUndefinedOrNull(formColl[formName])) {
				  formColl[formName].clearAllErrors(); 
			  }
		  };
	
		  return globalHandleErrorService;
		}]);

})();;angular.module('admin', ['ngResource', 'ui.router']);;(function() {
    'use strict';
	angular.module('admin').controller('AdminCtrl', ['$scope', 'adminService', 'adminVersionService', 'globalHandleErrorService', '$sce', '$q', '$http', function(sc, adminService, adminVersionService, globalHandleErrorService, sce, $q, $http) {
	  sc.adminProp = {
			  description: 'empty'
	  };
	
	  sc.refresh = function(form) {
		  if(form.$invalid) {
			  return;
		  }
		  
		  sc.adminProp.description = '';
		  reset();
		  var action = 'Refresh';
		  console.log('refresh');
		  adminService.get({item_key: sc.adminProp.itemKey, field_key: sc.adminProp.fieldKey}).$promise.then(
				  function(success) {
					  handleSuccess({success: success, action: action, form: form});
				  }, 
				  function(error) {
					  handleError({error: error.data.errors, action: action , formName: 'adminForm'});
				  });
	  };
	  
	  sc.create = function(form) {
		  if(form.$invalid) {
			  return;
		  }
		  
		  reset();
		  var action = 'Create';
		  console.log('create');
		  adminService.save({item_key: sc.adminProp.itemKey, field_key: sc.adminProp.fieldKey, description: sc.adminProp.description}).$promise.then(
				  function(success) {
					  handleSuccess({success: success, action: action, form: form});
				  }, 
				  function(error) {
					  handleError({error: error.data.errors, action: action , formName: 'adminForm'});
				  });
	  };
	  
	  sc.update = function(form) {
		  if(form.$invalid) {
			  return;
		  }
		  
		  reset();
		  var action = 'Update';
		  console.log(action);
		  adminService.update({item_key: sc.adminProp.itemKey, field_key: sc.adminProp.fieldKey, description: sc.adminProp.description}).$promise.then(
				  function(success) {
					  handleSuccess({success: success, action: action, form: form});
				  }, 
				  function(error) {
					  handleError({error: error.data.errors, action: action , formName: 'adminForm'});
				  });
	  };
	  
	  sc.delete = function(form) {
		  if(form.$invalid) {
			  return;
		  }
		  
		  reset();
		  var action = 'Delete';
		  console.log(action);
		  adminService.delete({item_key: sc.adminProp.itemKey, field_key: sc.adminProp.fieldKey}).$promise.then(
				  function(success) {
					  handleSuccess({success: success, action: action, form: form});
				  }, 
				  function(error) {
					  handleError({error: error.data.errors, action: action , formName: 'adminForm'});
				  });
	  };
	  
	  sc.diff = function() {
		  sc.diff.responseText1 = '';
		  sc.diff.responseText2 = '';
		  $q.all([propertyByVersion(sc.fromVersion, 'responseText1'), propertyByVersion(sc.withVersion, 'responseText2')])
		  			.then(function(success) {
		  				var diffResult = lineDiff.diffTwoTexts(sc.diff.responseText1, sc.diff.responseText2);
		  				sc.diff.results = sce.trustAsHtml(diffResult);
		  				console.log('after method invocation : ' + sc.diff.results);
		  				}, function(error) {
		  					console.log("error");
		  				});
	  };
	  
	  sc.searchIK = function(itemKey) {
		  return $http.get('/developer/admin/search/' + itemKey)
		  		 .then(function(res) {
		  			 return res.data;
		  		 });
	  };
	  
	  sc.searchFK = function(fieldKey) {
		  return $http.get('/developer/admin/search/' + sc.adminProp.itemKey + '/' + fieldKey)
		  		 .then(function(res) {
		  			 return res.data;
		  		 });
	  };
	  
	  /** Non-scope methods */
	  var handleSuccess = function(response) {
		  console.log('success:' + response.success);
		  response.form.$setPristine();
		  console.log('formName:' + response.success);
		  sc.adminProp.successMessage = response.action + ' completed successfully.';
		  sc.adminProp.itemKey = response.success.item_key;
		  sc.adminProp.fieldKey = response.success.field_key;
		  sc.adminProp.description = response.success.description;
		  version();
	  };
	  
	  var handleError = function(response) {
		  console.log('error.data.errors: ' + response.error);
		  globalHandleErrorService({
	          formName: response.formName,
	          fieldErrors: response.error
		  });
	  };
	  
	  var reset = function() {
		  globalHandleErrorService.clearAllErrors('adminForm');
		  sc.adminProp.successMessage = '';
	  };
	  
	  var version = function() {
		  adminVersionService.get({item_key: sc.adminProp.itemKey, field_key: sc.adminProp.fieldKey}).$promise.then(
				  function(success) {
					  console.log('version info : ' + success);
					  sc.versions = success.versions;
					  sc.fromVersion = success.versions.length - 1;
				  }, 
				  function(error) {
					  sc.versions = [];
					  console.log('errrrrrrrrrrrrrorrrrrrrrr');
				  });
	  };
	  
	  var propertyByVersion = function(version, variableName) {
		  return adminService.get({item_key: sc.adminProp.itemKey, field_key: sc.adminProp.fieldKey, version: version}).$promise.then(
				  function(success) {
					  console.log('success : ' + success);
					  sc.diff[variableName] = success.description;
					  console.log(variableName + " : done");
				  }, 
				  function(error) {
					  console.log('error propertyByVersion :     ' + error);
					  sc.diff[variableName] = '';
				  });
	  };
	  
	}]);
	
})();
;(function() {
    'use strict';
	angular.module('admin').
		config(['$urlRouterProvider', '$stateProvider', function($urlRouterProvider, $stateProvider) {
			$stateProvider.state('admin', {
				url: '/admin',
				templateUrl: 'base/app/admin/admin-properties.tpl.html'
			});
	}]);
})();
;(function() {
    'use strict';
	angular.module('admin').factory('adminService', ['$resource', function($resource, $scope) {
			return $resource('/properties/:item_key/:field_key', {}, {
				update: {method: 'PUT'}
		    });
	  }]);
	
	angular.module('admin').factory('adminVersionService', ['$resource', function($resource, $scope) {
		return $resource('/properties/:item_key/:field_key/versions', {}, {
	    });
	}]);
})();;angular.module('templates-dist', ['base/app/admin/admin-properties.tpl.html', 'base/common/directives/json-table.tpl.html', 'base/common/security/login/login.tpl.html']);

angular.module("base/app/admin/admin-properties.tpl.html", []).run(["$templateCache", function($templateCache) {
  "use strict";
  $templateCache.put("base/app/admin/admin-properties.tpl.html",
    "<div class=\"container\">\n" +
    "				<form class=\"form-horizontal\" role=\"form\" name=\"adminForm\" ng-controller=\"AdminCtrl\" novalidate st-form-errors autocomplete=\"off\">\n" +
    "						<div class=\"form-group\">\n" +
    "							<div class=\"col-sm-offset-2 col-sm-5\">\n" +
    "								<h3>Global Properties</h3>\n" +
    "							</div>\n" +
    "						</div>\n" +
    "						<div class=\"form-group\">\n" +
    "						    	<div class=\"col-sm-7 col-sm-offset-2\">\n" +
    "						    		<div class=\"has-error help-block\">\n" +
    "					        			<label name=\"top_lvl_errors\" st-field-errors></label>\n" +
    "					        		</div>\n" +
    "									<div class=\"has-success help-block\" ng-show=\"adminProp.successMessage != null && adminForm.$pristine\">\n" +
    "										{{adminProp.successMessage}}\n" +
    "									</div>\n" +
    "					        	</div>\n" +
    "				        </div>\n" +
    "				\n" +
    "						<div class=\"form-group\" ng-class=\"{'has-error' : validateOnSubmit && adminForm.itemKey.$invalid}\">\n" +
    "							<label class=\"required col-sm-2 control-label\" for=\"itemKey_label\">Item Key</label>\n" +
    "							 <div class=\"col-sm-5\">\n" +
    "								<input name=\"itemKey\" type=\"text\" ng-model=\"adminProp.itemKey\" required st-field-errors tabindex=\"1\" typeahead=\"itemKey for itemKey in searchIK($viewValue)\" typeahead-loading=\"loadingLocations\"  class=\"form-control\"/><i ng-show=\"loadingLocations\" class=\"glyphicon glyphicon-refresh\"></i>\n" +
    "							 </div>\n" +
    "						</div>\n" +
    "						<div class=\"form-group\" ng-class=\"{'has-error' : validateOnSubmit && adminForm.fieldKey.$invalid}\">\n" +
    "							<label class=\"required col-sm-2 control-label\" for=\"fieldKey_label\">Field Key</label>\n" +
    "							<div class=\"col-sm-5\">\n" +
    "								<input name=\"fieldKey\" type=\"text\" id=\"fieldKey_input_id\" ng-model=\"adminProp.fieldKey\" required st-field-errors tabindex=\"2\" typeahead=\"itemKey for itemKey in searchFK($viewValue)\" typeahead-loading=\"loadingLocations\"  class=\"form-control\"/><i ng-show=\"loadingLocations\" class=\"glyphicon glyphicon-refresh\"></i>\n" +
    "							</div>	\n" +
    "						</div>\n" +
    "						<div class=\"form-group\" ng-class=\"{'has-error' : validateOnSubmit && adminForm.description.$invalid}\">\n" +
    "							<label class=\"required col-sm-2 control-label\" for=\"description_label\">Description</label>\n" +
    "								<json-table name=\"description\" id=\"description_textArea_id\" ng-model=\"adminProp.description\"></json-table>\n" +
    "								<br>\n" +
    "								<br>\n" +
    "								<br>\n" +
    "								<br>\n" +
    "								<br>\n" +
    "						</div>\n" +
    "						<div class=\"form-group\">\n" +
    "							<div class=\"col-sm-offset-2 col-sm-10\">\n" +
    "								<button data-ng-click=\"validateOnSubmit=true; refresh(adminForm)\" class=\"btn btn-primary\">Refresh</button>\n" +
    "								<button data-ng-click=\"validateOnSubmit=true; create(adminForm)\" class=\"btn btn-primary\">Create</button>\n" +
    "								<button data-ng-click=\"validateOnSubmit=true; update(adminForm)\" class=\"btn btn-primary\">Update</button>\n" +
    "								<button data-ng-click=\"validateOnSubmit=true; delete(adminForm)\" class=\"btn btn-primary\">Delete</button>\n" +
    "							</div>\n" +
    "						</div>\n" +
    "						<br>\n" +
    "						\n" +
    "						<div class=\"form-group\">\n" +
    "							<label class=\"col-sm-2 control-label\">Diff version: </label>\n" +
    "							<select class=\"col-sm-1\" ng-model=\"fromVersion\" ng-options=\"version as version for version in versions\"></select><label class=\"col-sm-1 control-label\">against:</label><select class=\"col-sm-1\" ng-model=\"withVersion\" ng-options=\"version as version for version in versions\" ng-change=\"diff()\"></select>\n" +
    "						</div>\n" +
    "						<div class=\"form-group\">\n" +
    "							<label class=\"col-sm-2 control-label\">Response:</label>\n" +
    "							<pre class=\"col-sm-7\"><div ng-bind-html=\"diff.results\"></div></pre>\n" +
    "						</div>\n" +
    "				</form>\n" +
    "</div>\n" +
    "");
}]);

angular.module("base/common/directives/json-table.tpl.html", []).run(["$templateCache", function($templateCache) {
  "use strict";
  $templateCache.put("base/common/directives/json-table.tpl.html",
    "<div class=\"col-sm-7\">\n" +
    "    <table id=\"jsonTable\" class=\"table table-striped\">\n" +
    "        <thead>\n" +
    "        <tr>\n" +
    "            <th>Key</th>\n" +
    "            <th>Value</th>\n" +
    "            <th></th>\n" +
    "        </tr>\n" +
    "        </thead>\n" +
    "        <tbody>\n" +
    "        <tr ng-repeat=\"(key, val) in propertyJson\">\n" +
    "            <td><input id=\"key_{{index}}\" type=\"text\" name=\"key\" ng-model=\"key\"></td>\n" +
    "            <td><input id=\"value_{{index}}\" type=\"text\" name=\"value\" ng-model=\"val\" ng-change=\"jsonTable.change($index)\"/></td>\n" +
    "            <td>\n" +
    "                <button ng-click=\"jsonTable.delete(key)\">-</button>\n" +
    "            </td>\n" +
    "        </tr>\n" +
    "        <tr>\n" +
    "            <td><button id=\"add\">+</button></td>\n" +
    "        </tr>\n" +
    "        </tbody>\n" +
    "    </table>\n" +
    "</div>");
}]);

angular.module("base/common/security/login/login.tpl.html", []).run(["$templateCache", function($templateCache) {
  "use strict";
  $templateCache.put("base/common/security/login/login.tpl.html",
    "<div class=\"page-header\">\n" +
    "	<h2>Login</h2>\n" +
    "</div>\n" +
    "\n" +
    "\n" +
    "		<form class=\"form-horizontal\" name=\"loginForm\" ng-controller=\"LoginController\" novalidate st-form-errors autocomplete=\"off\">\n" +
    "			<div class=\"form-group\">\n" +
    "				<label for=\"username\" class=\"col-sm-3 control-label\">User Name:</label>\n" +
    "				<div class=\"col-xs-4\">\n" +
    "					<input id=\"username\" ng-model=\"login.username\" type=\"text\" class=\"form-control\" />\n" +
    "				</div>\n" +
    "			</div>\n" +
    "			<div class=\"form-group\">\n" +
    "				<label for=\"password\" class=\"col-sm-3 control-label\">Password:</label>\n" +
    "				<div class=\"col-xs-4\">\n" +
    "					<input id=\"password\" ng-model=\"login.password\" type=\"password\" class=\"form-control input-medium\" />\n" +
    "				</div>\n" +
    "			</div>\n" +
    "			<div class=\"form-group\">\n" +
    "				<div class=\"col-xs-3 col-sm-offset-3\">\n" +
    "					<input type=\"submit\" value=\"Log In\" class=\"btn btn-primary\" ng-click=\"login(loginForm)\"/>\n" +
    "				</div>\n" +
    "			</div>\n" +
    "		</form>\n" +
    "	\n" +
    "");
}]);
