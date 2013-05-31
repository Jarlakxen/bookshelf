'use strict';

var app = angular.module('BookshelfApp', [ 'ngResource', 
											'enviromentService', 
											'projectService', 
											'moduleService', 
											'propertyService', 
											'propertiesGroupService', 
											'ui', 
											'ui.bootstrap' ]);

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
	        			unsavedclass: null/*,
	        			display: function(value, srcData) {
                        	scope.$apply();
                    	}*/
	                });

	        	$( element ).on('save', function(e, params) {

	            	scope.$apply(function () {
						ngModel.$setViewValue(params.newValue);
					});

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
			if (newVal) {
				$( elm ).show();
			} else {
				$( elm ).hide();
			}
		});
	};
}]);

app.directive('fade', [function () {
	return function (scope, elm, attrs) {
		scope.$watch(attrs.fade, function (newVal, oldVal) {

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

var ProjectListCtrl = app.controller('ProjectListCtrl', function ($scope, Project, $dialog) {
	var projects = Project.query();
	
	$scope.projects = projects;
	$scope.selectedProject = null;

	$scope.$on('OnProjectSelect', function(event, project) {
		
		if( $scope.selectedProject != null && $scope.selectedProject.id == project.id ){
			$scope.notifyAll('OnProjectUnload');
		} else {
        	$scope.selectedProject =  project;
        	$scope.notifyAll('OnProjectLoad', project);
    	}
    });   

    $scope.$on('OnProjectUnload', function(event, project) {
		$scope.selectedProject =  null;
    });   

	$scope.addProject = function (newProject){

		var project = new Project({id: '', name: newProject.name, description: ''});
		project.$save();
		
		projects.push(project);

		newProject.name = '';
	};

	$scope.removeProject = function (selectedProject){

		var title = 'Delete Project';
		var msg = 'Do you really want to delete ' + selectedProject.name + '?';
		var btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

		$dialog.messageBox(title, msg, btns)
			.open()
			.then(function(result){
				projects.remove(selectedProject);
				selectedProject.$delete();
				$scope.notifyAll('OnProjectRemove', selectedProject);
			});
	};

});

// ----------------------------------
//			Module Controllers 
// ----------------------------------

var ModuleListCtrl = app.controller('ModuleListCtrl', function ($scope, Module, Project, $dialog) {

	$scope.project =  null;
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
        	$scope.selectedModule.linkedProperties = true;
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

		var title = 'Delete Module';
		var msg = 'Do you really want to delete ' + selectedModule.name + '?';
		var btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

		$dialog.messageBox(title, msg, btns)
			.open()
			.then(function(result){
				$scope.modules.remove(selectedModule);
				
				selectedModule.$delete();
				
				$scope.notifyAll('OnModuleRemove', selectedModule);
				$scope.notifyAll('OnParentPropertyRemove', selectedModule);
			});
	};

});

// ----------------------------------
//			Property Controllers 
// ----------------------------------

var PropertyListCtrl = app.controller('PropertyListCtrl', function ($scope, Property, Enviroment, PropertiesGroup) {

	$scope.parent =  null;
	$scope.enviroments = Enviroment.query();
	$scope.propertiesGroups = PropertiesGroup.query();
	$scope.newProperty = {};

	$scope.$on('OnParentPropertyLoad', function(event, parent) {
        $scope.parent = parent;
        $scope.properties = parent.properties(function(property) {
        	angular.forEach($scope.enviroments, function(enviroment){
        		if( property.values[enviroment.name] == undefined) {
                	property.values[enviroment.name] = {linkEnviromentId: null, linkId: null, value:null};
            	}
            });
        });
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

	$scope.$on('OnPropertiesGroupModalClose', function(event, property) {
        if( property.id ){
    		property.$save();
    	}
    });

    $scope.addProperty = function (newProperty, enviroment){
		var newPropertyValues = {};
		
		var linkEnviromentId = newProperty.linkEnviromentId == undefined ? null : newProperty.linkEnviromentId;
		var linkId = newProperty.linkId == undefined ? null : newProperty.linkId;

		newPropertyValues[enviroment.name] = {linkEnviromentId: linkEnviromentId, linkId: linkId, value: newProperty.value};

		var property = new Property({id: '', name: newProperty.name, parentId: $scope.parent.id, values: newPropertyValues});
		property.$save();

		$scope.properties.push(property);

		newProperty.name = '';
		newProperty.value = '';
		newProperty.linkId = null;
	};

	$scope.linkProperty = function (model, modelToSaveOnSelect){
		var obj = { model : model};

		if( modelToSaveOnSelect != undefined ){
			obj.onSelect = function(){
				modelToSaveOnSelect.$update();
			};
		}

		$scope.notifyAll('OnPropertiesGroupModalShow', obj);
	};

	$scope.unlinkProperty = function (selectedProperty, enviroment){
		selectedProperty.values[enviroment.name].linkId=null;
		selectedProperty.values[enviroment.name].linkEnviromentId=null;
		selectedProperty.$update();
	};

	$scope.saveProperty = function (selectedProperty){
		selectedProperty.$save();
	};


	$scope.removeProperty = function (selectedProperty){
		$scope.properties.remove(selectedProperty);

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
		enviroments.remove(selectedEnviroment);

		selectedEnviroment.$delete();

		$scope.notifyAll('OnEnviromentRemove', selectedEnviroment);
	};

});

// ----------------------------------
//     Properties Group Controllers 
// ----------------------------------

var PropertiesGroupListCtrl = app.controller('PropertiesGroupListCtrl', function ($scope, PropertiesGroup) {

	$scope.propertiesGroups = PropertiesGroup.query();
	$scope.selectedPropertiesGroup =  null;


    $scope.$on('OnPropertiesGroupSelect', function(event, propertiesGroup) {
		if( $scope.selectedPropertiesGroup != null && $scope.selectedPropertiesGroup.id == propertiesGroup.id ){
			$scope.selectedPropertiesGroup =  null;
			$scope.notifyAll('OnParentPropertyUnload');
		} else {
        	$scope.selectedPropertiesGroup =  propertiesGroup;
        	$scope.selectedPropertiesGroup.linkedProperties = false;
        	$scope.notifyAll('OnParentPropertyLoad', propertiesGroup);
    	}
    });  


	$scope.addPropertiesGroup = function (newPropertiesGroup){

		var propertiesGroup = new PropertiesGroup({id: '', name: newPropertiesGroup.name, description: newPropertiesGroup.description});
		propertiesGroup.$save();

		$scope.propertiesGroups.push(propertiesGroup);

		newPropertiesGroup.name = '';
		newPropertiesGroup.description = '';
	};

	$scope.removePropertiesGroup = function (selectedPropertiesGroup){
		$scope.propertiesGroups.remove(selectedPropertiesGroup);

		selectedPropertiesGroup.$delete();

		$scope.notifyAll('OnPropertiesGroupRemove', selectedPropertiesGroup);
		$scope.notifyAll('OnParentPropertyRemove', selectedPropertiesGroup);
	};
});

// ----------------------------------
// Properties Group Modal Controllers 
// ----------------------------------

var PropertiesGroupModalCtrl = app.controller('PropertiesGroupModalCtrl', function ($scope, PropertiesGroup, Enviroment) {

    $scope.$on('OnPropertiesGroupModalShow', function(event, obj) {
	
		$scope.enviroments = [];
    	$scope.enviroments = Enviroment.query(function(){
			$scope.selectedEnviroment = $scope.enviroments[0];
    	});

		$scope.propertiesGroups = {};
		var propertiesGroups = PropertiesGroup.query(function(){
			angular.forEach(propertiesGroups, function(propertiesGroup){
                $scope.propertiesGroups[propertiesGroup.name] = propertiesGroup.properties();
            });
    	});
		$scope.currentModel = obj.model;
		$scope.onSelect = obj.onSelect;
		$scope.shouldBeOpen = true;
    });


	$scope.selectProperty = function (selectedProperty) {
		$scope.currentModel.linkEnviromentId = $scope.selectedEnviroment.id;
		$scope.currentModel.linkId = selectedProperty.id;
		$scope.currentModel.value = selectedProperty.values[$scope.selectedEnviroment.name].value;
		if( $scope.onSelect != undefined ) {
			$scope.onSelect();
		}
		$scope.close(true);
	}

	$scope.selectEnviroment = function (selectedEnviroment){
		$scope.selectedEnviroment = selectedEnviroment;
	}

    $scope.close = function (notify){
    	if(notify == true){
			$scope.notifyAll('OnPropertiesGroupModalClose', $scope.currentModel);
		}

    	$scope.enviroments = [];
    	$scope.propertiesGroups = [];
    	$scope.currentModel = {};
		$scope.shouldBeOpen = false;
		$scope.selectedEnviroment = null;
	};

	$scope.close();
});