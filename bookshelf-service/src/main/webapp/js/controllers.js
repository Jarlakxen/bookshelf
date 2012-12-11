'use strict';

var app = angular.module('BookshelfApp', [ 'ngResource', 'enviromentService', 'projectService', 'moduleService' ]);

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
	$scope.selectedProject = null;

	$scope.$on('OnProjectSelect', function(event, project) {
		
		if( $scope.selectedProject != null && $scope.selectedProject.id == project.id ){
			$scope.selectedProject =  null;
			$scope.notifyAll('OnProjectUnload');
		} else {
        	$scope.selectedProject =  project;
        	$scope.notifyAll('OnProjectLoad', project);
    	}
    });   

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

		$scope.notifyAll('OnProjectRemove', selectedProject)
	};

});

// ----------------------------------
//			Module Controllers 
// ----------------------------------

var ModuleListCtrl = app.controller('ModuleListCtrl', function ($scope, Module, Project) {

	$scope.modules = [];
	$scope.project = null;
	$scope.selectedModule = null;

	$scope.$on('OnProjectLoad', function(event, project) {
        $scope.project =  project;
        $scope.modules = project.modules();
    });

    $scope.$on('OnProjectUnload', function(event) {
        $scope.project =  null;
        $scope.selectedModule = null;
    	$scope.modules = [];
    });

    $scope.$on('OnProjectRemove', function(event, project) {
        if( project == $scope.project ){
    		$scope.$broadcast('OnProjectUnload');
    	}
    });

    $scope.$on('OnModuleSelect', function(event, module) {
		if( $scope.selectedModule != null && $scope.selectedModule.id == module.id ){
			$scope.selectedModule =  null
		} else {
        	$scope.selectedModule =  module
    	}
    });   

    $scope.addModule = function (newModule){
		var module = $scope.project.newmodule({id: '', name: newModule.name, description: ''});

		$scope.modules.push(module);

		newModule.name = '';
	}; 

	$scope.removeModule = function (selectedModule){
		$scope.modules.pop(selectedModule);

		selectedModule.$delete();
	};

});

var PropertyListCtrl = app.controller('PropertyListCtrl', function ($scope, Module, Enviroment) {
	var enviroments = Enviroment.query();

	$scope.enviroments = enviroments;

});
