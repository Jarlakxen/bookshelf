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
	$scope.selectedProject = null;

	$scope.$on('OnProjectSelect', function(event, project) {
		
		if( $scope.selectedProject != null && $scope.selectedProject.id == project.id ){
			$scope.selectedProject =  null
		} else {
        	$scope.selectedProject =  project
    	}
    	$scope.notifyAll('OnProjectModulesGet', $scope.selectedProject)
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
	};

});

// ----------------------------------
//			Module Controllers 
// ----------------------------------

var ModuleListCtrl = app.controller('ModuleListCtrl', function ($scope, Module, Project) {

	$scope.modules = [];
	$scope.project = null;
	$scope.selectedModule = null;

	$scope.$on('OnProjectModulesGet', function(event, project) {
        $scope.project =  project
        if( project != null ){
        	$scope.modules = project.modules();
    	} else {
    		$scope.modules = [];
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

});
