'use strict';

var app = angular.module('BookshelfApp', [ 'ngResource', 'projectService', 'moduleService' ]);

// ----------------------------------
//			Shared Service
// ----------------------------------

app.run(['$rootScope', function($rootScope) {
	$rootScope.notifyAll = function(eventName, item) {
		$rootScope.$broadcast(eventName, item);
	};
}]);


// ----------------------------------
//			Project Controllers 
// ----------------------------------

var ProjectListCtrl = app.controller('ProjectListCtrl', function ($scope, Project) {
	var projects = Project.query();
	
	$scope.projects = projects;

	$scope.addProject = function (newProject){
		var project = new Project({id: '', name: newProject.name, description: ''});
		project.$save();
		
		projects.push(project);

		newProject.name = '';
	};

	$scope.removeProject = function (selectedProject){
		projects.pop(selectedProject);

		var project = new Project({id: selectedProject.id, name: selectedProject.name, description: ''});

		project.$delete();
	};

});

// ----------------------------------
//			Module Controllers 
// ----------------------------------

var ModuleListCtrl = app.controller('ModuleListCtrl', function ($scope, Module, Project) {

	$scope.modules = [];
	$scope.project = null;

	$scope.$on('OnProjectSelect', function(event, project) {
        $scope.project = project;
    });   

    $scope.addModule = function (newModule){
		var module = new Module({id: '', name: newModule.name, description: '', projectId:$scope.project.id});
		module.$save();

		$scope.modules.push(module);

		module.name = '';
	}; 

});
