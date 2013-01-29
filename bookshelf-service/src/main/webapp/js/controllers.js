'use strict';

var app = angular.module('BookshelfApp', [ 'ngResource', 'enviromentService', 'projectService', 'moduleService', 'propertyService', 'ui', 'ui.bootstrap' ]);

// ----------------------------------
//			Shared Service
// ----------------------------------

app.run(['$rootScope', function($rootScope) {
	$rootScope.notifyAll = function(eventName, item) {
		$rootScope.$broadcast(eventName, item);
	};
}]);


// ----------------------------------
//			Custom Directives
// ----------------------------------

// X-Editable Configuration
$.fn.editable.defaults.mode = 'inline';

app.directive('editable', function($timeout) {
    return {
        restrict: 'A',
        require: "ngModel",
        link: function(scope, element, attrs, ngModel) {

 			var loadXeditable = function() {
	        	$( element ).editable({
	        			unsavedclass: null
	                });

	        	$( element ).on('save', function(e, params) {
	            	ngModel.$setViewValue(params.newValue);
	            	scope.$apply();

	            	if(attrs.ngSaveAction){
						scope.$eval(attrs.ngSaveAction);
					}
	            });
            }
            $timeout(function() {
                loadXeditable();
            }, 10);
        }
    };
});


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
			$scope.selectedModule =  null;
			$scope.notifyAll('OnModuleUnload');
		} else {
        	$scope.selectedModule =  module;
        	$scope.notifyAll('OnModuleLoad', module);
    	}
    });   

    $scope.addModule = function (newModule){
		var module = new Module({id: '', name: newModule.name, description: '', parentId:  $scope.project.id});
		module.$save();


		$scope.modules.push(module);

		newModule.name = '';
	}; 

	$scope.removeModule = function (selectedModule){
		$scope.modules.pop(selectedModule);

		selectedModule.$delete();
	};

});

var PropertyListCtrl = app.controller('PropertyListCtrl', function ($scope, Property, Enviroment) {
	var enviroments = Enviroment.query();

	$scope.enviroments = enviroments;

	$scope.$on('OnModuleLoad', function(event, module) {
        $scope.module =  module;
        $scope.properties = module.properties();
    });

    $scope.$on('OnModuleUnload', function(event) {
        $scope.module =  null;
    	$scope.properties = [];
    });

    $scope.addProperty = function (newProperty, enviroment){
		var newPropertyValues = {};
		newPropertyValues[enviroment.name] = newProperty.value;

		var property = new Property({id: '', name: newProperty.name, parentId: $scope.module.id, values: newPropertyValues});
		property.$save();

		$scope.properties.push(property);

		newProperty.name = '';
		newProperty.value = '';
	};

	$scope.saveProperty = function (selectedProperty){
		selectedProperty.$save();
	};


	$scope.removeProperty = function (selectedProperty){
		$scope.properties.pop(selectedProperty);

		selectedProperty.$delete();
	};

});
