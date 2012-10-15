'use strict';

var app = angular.module('BookshelfApp', [ 'ngResource', 'projectService' ]);

// ----------------------------------
//			Project Controllers 
// ----------------------------------

app.controller('ProjectListCtrl', function ($scope, Project) {
	var projects = Project.query();
	
	$scope.projects = projects;

	$scope.addProject = function (newProject){
		var project = new Project({name: newProject.name});
		projects.push(project);
		project.$save();

		newProject.name = '';
	};

});