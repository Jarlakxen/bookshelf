'use strict';

var app = angular.module('BookshelfApp', [ 'ngResource', 'enviromentService', 'projectService', 'moduleService', 'propertyService', 'propertiesGroupService', 'ui', 'ui.bootstrap' ]);

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

app.directive('hide', [function () {
	return function (scope, elm, attrs) {
		scope.$watch(attrs.hide, function (newVal, oldVal) {

			// Initial case, animation without delay
			if (newVal == oldVal) {
				if (newVal) {
					$( elm ).show();
				} else {
					$( elm ).hide();
				}
			}


			if (newVal) {
				$( elm ).fadeIn();
			} else {
				$( elm ).fadeOut();
			}
		});
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

		selectedProject.$delete();

		$scope.notifyAll('OnProjectRemove', selectedProject);
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
    	$scope.notifyAll('OnModuleUnload');
    	$scope.notifyAll('OnParentPropertyUnload');
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
			$scope.notifyAll('OnParentPropertyUnload');
		} else {
        	$scope.selectedModule =  module;
        	$scope.notifyAll('OnModuleLoad', module);
        	$scope.notifyAll('OnParentPropertyLoad', module);
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

		$scope.notifyAll('OnModuleRemove', selectedModule);
		$scope.notifyAll('OnParentPropertyRemove', selectedModule);
	};

});

// ----------------------------------
//			Property Controllers 
// ----------------------------------

var PropertyListCtrl = app.controller('PropertyListCtrl', function ($scope, Property, Enviroment) {
	var enviroments = Enviroment.query();

	$scope.enviroments = enviroments;

	$scope.$on('OnParentPropertyLoad', function(event, parent) {
        $scope.parent = parent;
        $scope.properties = parent.properties();
    });

    $scope.$on('OnParentPropertyUnload', function(event) {
        $scope.parent =  null;
    	$scope.properties = [];
    });

    $scope.$on('OnParentPropertyRemove', function(event, parent) {
        if( parent == $scope.parent ){
    		$scope.$broadcast('OnParentPropertyUnload');
    	}
    });

    $scope.addProperty = function (newProperty, enviroment){
		var newPropertyValues = {};
		newPropertyValues[enviroment.name] = newProperty.value;

		var property = new Property({id: '', name: newProperty.name, parentId: $scope.parent.id, scope: {name:"PUBLIC"} ,values: newPropertyValues});
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

// ----------------------------------
//			Enviroment Controllers 
// ----------------------------------

var EnviromentListCtrl = app.controller('EnviromentListCtrl', function ($scope, Enviroment) {
	var enviroments = Enviroment.query();
	
	$scope.enviroments = enviroments;


	$scope.addEnviroment = function (newEnviroment){

		var enviroment = new Enviroment({id: '', name: newEnviroment.name, description: newEnviroment.description});
		enviroment.$save();
		
		enviroments.push(enviroment);

		newEnviroment.name = '';
		newEnviroment.description = '';
	};

	$scope.removeEnviroment = function (selectedEnviroment){
		enviroments.pop(selectedEnviroment);

		selectedEnviroment.$delete();

		$scope.notifyAll('OnEnviromentRemove', selectedEnviroment);
	};

});

// ----------------------------------
//     Properties Group Controllers 
// ----------------------------------

var PropertiesGroupListCtrl = app.controller('PropertiesGroupListCtrl', function ($scope, PropertiesGroup) {
	var propertiesGroups = PropertiesGroup.query();
	
	$scope.propertiesGroups = propertiesGroups;


    $scope.$on('OnPropertiesGroupSelect', function(event, propertiesGroup) {
		if( $scope.selectedPropertiesGroup != null && $scope.selectedPropertiesGroup.id == propertiesGroup.id ){
			$scope.selectedPropertiesGroup =  null;
			$scope.notifyAll('OnParentPropertyUnload');
		} else {
        	$scope.selectedPropertiesGroup =  propertiesGroup;
        	$scope.notifyAll('OnParentPropertyLoad', propertiesGroup);
    	}
    });  


	$scope.addPropertiesGroup = function (newPropertiesGroup){

		var propertiesGroup = new PropertiesGroup({id: '', name: newPropertiesGroup.name, description: newPropertiesGroup.description});
		propertiesGroup.$save();
		
		propertiesGroups.push(propertiesGroup);

		newPropertiesGroup.name = '';
		newPropertiesGroup.description = '';
	};

	$scope.removePropertiesGroup = function (selectedPropertiesGroup){
		propertiesGroups.pop(selectedPropertiesGroup);

		selectedPropertiesGroup.$delete();

		$scope.notifyAll('OnPropertiesGroupRemove', selectedPropertiesGroup);
	};
});
