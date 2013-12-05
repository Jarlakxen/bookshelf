'use strict';

var app = angular.module('BookshelfApp', ['ngResource', 'ngRoute', 'ui.bootstrap', 'xeditable']);

app.config(['$locationProvider', '$routeProvider', function($locationProvider, $routeProvider) {
	$routeProvider.
	when('/projects', {
		templateUrl: 'partial/projects',   
		controller: 'ProjectsCtrl'
	}).
	when('/project', {
		templateUrl: 'partial/project',   
		controller: 'ProjectCtrl'
	}).
	when('/project/:id', {
		templateUrl: 'partial/project',   
		controller: 'ProjectCtrl'
	}).


	when('/project/:pid/modules', {
		templateUrl: 'partial/modules',   
		controller: 'ModulesCtrl'
	}).
	when('/project/:pid/module', {
		templateUrl: 'partial/module',   
		controller: 'ModuleCtrl'
	}).
	when('/project/:pid/module/:mid', {
		templateUrl: 'partial/module',   
		controller: 'ModuleCtrl'
	}).
	when('/project/:pid/module/:mid/properties', {
		templateUrl: 'partial/properties',   
		controller: 'PropertiesForModuleCtrl'
	}).

	when('/properties-groups', {
		templateUrl: 'partial/properties_groups',   
		controller: 'PropertiesGroupsCtrl'
	}).
	when('/properties-group', {
		templateUrl: 'partial/properties_group',   
		controller: 'PropertiesGroupCtrl'
	}).
	when('/properties-group/:id', {
		templateUrl: 'partial/properties_group',   
		controller: 'PropertiesGroupCtrl'
	}).
	when('/properties-group/:id/properties', {
		templateUrl: 'partial/properties_group_properties',   
		controller: 'PropertiesForPropertiesGroupCtrl'
	}).

	when('/enviroments', {
		templateUrl: 'partial/enviroments',   
		controller: 'EnviromentsCtrl'
	}).
	when('/enviroment', {
		templateUrl: 'partial/enviroment',   
		controller: 'EnviromentCtrl'
	}).
	when('/enviroment/:id', {
		templateUrl: 'partial/enviroment',   
		controller: 'EnviromentCtrl'
	}).

	otherwise({redirectTo: '/projects'});
}]);

app.run(['editableOptions', function(editableOptions) {
  editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
}]);

var MainCtrl = function($scope, $window, $location, $modal){

	$scope.$on('$locationChangeSuccess', function(locationEvent, newUrl, oldUrl) {
		if( newUrl != oldUrl ) {
			$scope.back = function(){
				var path = oldUrl.substring(oldUrl.indexOf("#/")+2);
				$location.path(path);
			}
		}
	}); 

	$scope.showConfirmDeletePopup = function(element, confirmCallback){
		$modal.open({
			templateUrl: 'ModalConfirmDelete.html',
			controller: 'ModalConfirmDeleteCtrl',
			windowClass: 'modal-block',
			resolve: {
				element: function () {
					return element;
				}
			}
		}).result.then(function (element) {
			confirmCallback(element);
		});
	}

	$scope.showSelectCommonPropertyPopup = function(confirmCallback){
		$modal.open({
			templateUrl: 'ModalSelectCommonProperty.html',
			controller: 'ModalSelectCommonPropertyCtrl',
			windowClass: 'modal-block',
			resolve: {
			}
		}).result.then(function (result) {
			confirmCallback(result);
		});
	}

	$scope.redirectTo = function(url){
		$location.path(url);
	}

	$scope.back = function(){
		$location.path('/projects');
	}

};

MainCtrl.$inject = ['$scope', '$window', '$location', '$modal'];

app.controller('MainCtrl', MainCtrl);

var ModalConfirmDeleteCtrl = app.controller('ModalConfirmDeleteCtrl', ['$scope', '$modalInstance', 'element', 
	function ($scope, $modalInstance, element) {
	$scope.element = element;

	$scope.ok = function () {
		$modalInstance.close($scope.element);
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
}]);

var ModalSelectCommonPropertyCtrl = app.controller('ModalSelectCommonPropertyCtrl', ['$scope', '$modalInstance', 'PropertiesGroup', 'Enviroment',
	function ($scope, $modalInstance, PropertiesGroup, Enviroment) {
	
	$scope.propertiesGroups = PropertiesGroup.query(function(propertiesGroups){
		angular.forEach(propertiesGroups, function(propertiesGroup){
			propertiesGroup.properties = propertiesGroup.properties();
		});
	});
	$scope.enviroments = Enviroment.query();

	$scope.select = function (property, enviroment) {
		$modalInstance.close({propertyId: property.id, enviromentId: enviroment.id, value: property.values[enviroment.id].fixValue});
	};

	$scope.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
}]);

