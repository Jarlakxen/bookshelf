'use strict';


var BASE_URL = '/bookshelf/rest'


// ----------------------------------
// 			Enviroments Services
// ----------------------------------

angular.module('enviromentService', ['ngResource']).factory('Enviroment', function($resource){

	return $resource(BASE_URL + '/enviroment/:id', {id:'@id'}, {});
  
});


// ----------------------------------
//          Property Services
// ----------------------------------

angular.module('propertyService', ['ngResource']).factory('Property', function($resource){

    return $resource(BASE_URL + '/property/:id', {id:'@id'}, {});
  
});

// ----------------------------------
// 			Module Services
// ----------------------------------

angular.module('moduleService', ['ngResource']).factory('Module', function(Property, $resource, $http){

    var Module = $resource(BASE_URL + '/module/:id', {id:'@id'}, {});

    Module.prototype.properties = function() {
        var properties = [];
        $http.get(BASE_URL + '/module/' + this.id + '/properties').then(function(response) {
            angular.forEach(response.data, function(value){
                properties.push( new Property({id: value.id, name: value.name, values: value.values}) );
            });
        });
        return properties;
    }

    return Module;
  
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

	return Project;
});