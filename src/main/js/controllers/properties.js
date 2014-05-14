'use strict';

// List Controller

var PropertiesForModuleCtrl = function($scope, $routeParams, $location, Project, Module, Enviroment, Property){

	function newProperty(){
		var newProperty = new Property();
		newProperty.values = {};
		angular.forEach($scope.enviroments, function(enviroment){
			newProperty.values[enviroment.id] = {linkEnviromentId: null, linkId: null, fixValue: null};
		});
		return newProperty;
	}

	$scope.project = Project.get({id:$routeParams.pid});
	$scope.module = Module.get({id:$routeParams.mid}, function(module){
		$scope.properties = module.properties();
	});
	$scope.enviroments = Enviroment.query(function(){
		$scope.newProperty = newProperty();
	});

	$scope.add = function(property, enviroment){
		property.parentId = $scope.module.id;
		property.$save();
		$scope.properties.push(property);
		$scope.newProperty = newProperty();
	}

	$scope.update = function(property){
		property.$update();
	}

	$scope.link = function(property, enviroment){
		$scope.showSelectCommonPropertyPopup(function(value){
			property.values[enviroment.id].linkEnviromentId = value.enviromentId;
			property.values[enviroment.id].linkId = value.propertyId;
			property.values[enviroment.id].fixValue = value.value;
		});
	}

	$scope.linkAndUpdate = function(property, enviroment){
		$scope.showSelectCommonPropertyPopup(function(value){
			property.values[enviroment.id].linkEnviromentId = value.enviromentId;
			property.values[enviroment.id].linkId = value.propertyId;
			property.values[enviroment.id].fixValue = value.value;
			property.$update();
		});
	}

	$scope.unlink = function(property, enviroment){
		property.values[enviroment.id].linkEnviromentId = null;
		property.values[enviroment.id].linkId = null;
	}

	$scope.unlinkAndUpdate = function(property, enviroment){
		$scope.unlink(property, enviroment);
		property.$update();
	}

	$scope.delete = function(property){
		$scope.showConfirmDeletePopup(property.name, function(){
			$scope.properties.remove(property);	
			property.$remove();
		});
	}

};

PropertiesForModuleCtrl.$inject = ['$scope', '$routeParams', '$location', 'Project', 'Module', 'Enviroment', 'Property'];

app.controller('PropertiesForModuleCtrl', PropertiesForModuleCtrl);


var PropertiesForPropertiesGroupCtrl = function($scope, $routeParams, $location, PropertiesGroup, Enviroment, Property){

	function newProperty(){
		var newProperty = new Property();
		newProperty.values = {};
		angular.forEach($scope.enviroments, function(enviroment){
			newProperty.values[enviroment.id] = {linkEnviromentId: null, linkId: null, fixValue: null};
		});
		return newProperty;
	}

	$scope.propertiesGroup = PropertiesGroup.get({id:$routeParams.id}, function(propertiesGroup){
		$scope.properties = propertiesGroup.properties();
	});
	$scope.enviroments = Enviroment.query(function(){
		$scope.newProperty = newProperty();
	});

	$scope.add = function(property, enviroment){
		property.parentId = $scope.propertiesGroup.id;
		property.$save();
		$scope.properties.push(property);
		$scope.newProperty = newProperty();
	}

	$scope.update = function(property){
		property.$update();
	}

	$scope.delete = function(property){
		$scope.showConfirmDeletePopup(property.name, function(){
			$scope.properties.remove(property);	
			property.$remove();
		});
	}

};

PropertiesForPropertiesGroupCtrl.$inject = ['$scope', '$routeParams', '$location', 'PropertiesGroup', 'Enviroment', 'Property'];

app.controller('PropertiesForPropertiesGroupCtrl', PropertiesForPropertiesGroupCtrl);