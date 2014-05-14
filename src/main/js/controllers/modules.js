'use strict';

// List Controller

var ModulesCtrl = function($scope, $routeParams, $location, Project, Module){

	$scope.project = Project.get({id:$routeParams.pid}, function(project){
		project.modules(function(modules){
			$scope.modules = modules;
			angular.forEach(modules, function(module){
				module.properties(function(properties){
					module.propertiesAmount = properties.length;
				});
			});
		});
	});

	$scope.add = function(){
		$location.path('project/' + $scope.project.id + '/module');  
	}

	$scope.delete = function(module){
		$scope.showConfirmDeletePopup(module.name, function(){
			$scope.modules.remove(module);	
			module.$remove();
		});
	}

};

ModulesCtrl.$inject = ['$scope', '$routeParams', '$location', 'Project', 'Module'];

app.controller('ModulesCtrl', ModulesCtrl);


// Single Element Controller

var ModuleCtrl = function($scope, $routeParams, Module){

	$scope.createMode = angular.isUndefined($routeParams.mid);

	if( !$scope.createMode ){
		$scope.module = Module.get({id:$routeParams.mid});
	} else {
		$scope.module = new Module();
		$scope.module.parentId = $routeParams.pid;
	}

	$scope.add = function(){
		$scope.module.$save().then(function() {
			$scope.back();
		});
	};

	$scope.save = function(){
		$scope.module.$update().then(function() {
			$scope.back();
		});
	};

};

ModuleCtrl.$inject = ['$scope', '$routeParams', 'Module'];

app.controller('ModuleCtrl', ModuleCtrl);