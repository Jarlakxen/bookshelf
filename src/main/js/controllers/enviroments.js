'use strict';

// List Controller

var EnviromentsCtrl = function($scope, $location, Enviroment){
	$scope.enviroments = Enviroment.query();

	$scope.add = function(){
		$location.path('enviroment');  
	}

	$scope.delete = function(enviroment){
		$scope.showConfirmDeletePopup(enviroment.name, function(){
			$scope.enviroments.remove(enviroment);	
			enviroment.$remove();
		});
	}

};

EnviromentsCtrl.$inject = ['$scope', '$location', 'Enviroment'];

app.controller('EnviromentsCtrl', EnviromentsCtrl);


// Single Element Controller

var EnviromentCtrl = function($scope, $routeParams, Enviroment){

	$scope.createMode = angular.isUndefined($routeParams.id);

	if( !$scope.createMode ){
		$scope.enviroment = Enviroment.get({id:$routeParams.id});
	} else {
		$scope.enviroment = new Enviroment();
	}

	$scope.add = function(){
		$scope.enviroment.$save().then(function() {
			$scope.back();
		});
	};

	$scope.save = function(){
		$scope.enviroment.$update().then(function() {
			$scope.back();
		});
	};

};

EnviromentCtrl.$inject = ['$scope', '$routeParams', 'Enviroment'];

app.controller('EnviromentCtrl', EnviromentCtrl);