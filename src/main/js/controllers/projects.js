'use strict';

// List Controller

var ProjectsCtrl = function($scope, $location, Project){
	$scope.projects = Project.query(function(projects){
		angular.forEach(projects, function(project){
			project.modules(function(modules){
				project.modulesAmount = modules.length;
			});
		});
	});

	$scope.add = function(){
		$location.path('project');  
	}

	$scope.delete = function(project){
		$scope.showConfirmDeletePopup(project.name, function(){
			$scope.projects.remove(project);	
			project.$remove();
		});
	}

};

ProjectsCtrl.$inject = ['$scope', '$location', 'Project'];

app.controller('ProjectsCtrl', ProjectsCtrl);


// Single Element Controller

var ProjectCtrl = function($scope, $routeParams, Project){

	$scope.createMode = angular.isUndefined($routeParams.id);

	if( !$scope.createMode ){
		$scope.project = Project.get({id:$routeParams.id});
	} else {
		$scope.project = new Project();
	}

	$scope.add = function(){
		$scope.project.$save().then(function() {
			$scope.back();
		});
	};

	$scope.save = function(){
		$scope.project.$update().then(function() {
			$scope.back();
		});
	};

};

ProjectCtrl.$inject = ['$scope', '$routeParams', 'Project'];

app.controller('ProjectCtrl', ProjectCtrl);