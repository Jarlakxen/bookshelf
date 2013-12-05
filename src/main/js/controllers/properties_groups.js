'use strict';

// List Controller

var PropertiesGroupsCtrl = function($scope, $location, PropertiesGroup){

	$scope.propertiesGroups = PropertiesGroup.query(function(propertiesGroups){
		angular.forEach(propertiesGroups, function(propertiesGroup){
			propertiesGroup.properties(function(properties){
				propertiesGroup.propertiesAmount = properties.length;
			});
		});
	});

	$scope.add = function(){
		$location.path('properties-group');  
	}

	$scope.delete = function(propertiesGroup){
		$scope.showConfirmDeletePopup(propertiesGroup.name, function(){
			$scope.propertiesGroups.remove(propertiesGroup);	
			propertiesGroup.$remove();
		});
	}

};

PropertiesGroupsCtrl.$inject = ['$scope', '$location', 'PropertiesGroup'];

app.controller('PropertiesGroupsCtrl', PropertiesGroupsCtrl);

// Single Element Controller

var PropertiesGroupCtrl = function($scope, $routeParams, PropertiesGroup){

	$scope.createMode = angular.isUndefined($routeParams.id);

	if( !$scope.createMode ){
		$scope.propertiesGroup = PropertiesGroup.get({id:$routeParams.id});
	} else {
		$scope.propertiesGroup = new PropertiesGroup();
	}

	$scope.add = function(){
		$scope.propertiesGroup.$save().then(function() {
			$scope.back();
		});
	};

	$scope.save = function(){
		$scope.propertiesGroup.$update().then(function() {
			$scope.back();
		});
	};

};

PropertiesGroupCtrl.$inject = ['$scope', '$routeParams', 'PropertiesGroup'];

app.controller('PropertiesGroupCtrl', PropertiesGroupCtrl);