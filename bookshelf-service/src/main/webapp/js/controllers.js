'use strict';

var app = angular.module('BookshelfApp', [ 'ngResource', 'projectService' ]);

// ----------------------------------
//			Project Controllers 
// ----------------------------------

app.controller('ProjectListCtrl', function ($scope, Project) {
	var projects = Project.query();
	
	$scope.projects = projects;

	$scope.addProject = function (newProject){
		var project = new Project({name: newProject.name, description: ''});
		project.$save();
		
		projects.push(project);

		newProject.name = '';
	};

	$scope.removeProject = function (selectedProject){
		projects.pop(selectedProject);

		var project = new Project({id: 's', name: 'aaa'});

		project.$delete();
	};

});