'use strict';


var BASE_URL = '/bookshelf/rest'


// ----------------------------------
// 			Module Services
// ----------------------------------

angular.module('moduleService', ['ngResource']).factory('Module', function($resource){

	return $resource(BASE_URL + '/module/:id', {id:'@id'}, {
		addTo:{
			method: "PUT"
		}
	});
  
});


// ----------------------------------
// 			Project Services
// ----------------------------------

angular.module('projectService', ['ngResource']).factory('Project', function(Module, $resource, $http){

	var Project = $resource(BASE_URL + '/project/:id', {id:'@id'},{});

 	Project.prototype.modules = function() {
 		var modules = [];
    	$http.get(BASE_URL + '/project/' + this.id + '/modules').then(function(response) {
    		angular.forEach(response.data, function(value){
				modules.push( new Module({id: value.id, name: value.name, description: value.description}) );
			});
    	});
    	return modules;
	}

 	Project.prototype.newmodule = function(newmodule) {
 		var module =  new Module();
    	$http.post(BASE_URL + '/project/' + this.id + '/newmodule', newmodule).then(function(response) {
    		module.id = response.data.id;
    		module.name = response.data.name;
    		module.description = response.data.description;
    	});
    	return module;
	}

	return Project;
});