'use strict';


var BASE_URL = '/bookshelf/rest'

function $service($resource, $http, $name){
    var service = $resource(BASE_URL + '/' + $name + '/:id', {id:'@id'}, {});

     service.prototype.$update = function() {
        $http.post(BASE_URL + '/' + $name + '/', this);
    }

    return service;
}

// ----------------------------------
// 			Enviroments Services
// ----------------------------------

angular.module('enviromentService', ['ngResource']).factory('Enviroment', function($resource, $http){

	return $service($resource, $http, 'enviroment');
  
});


// ----------------------------------
//          Property Services
// ----------------------------------

angular.module('propertyService', ['ngResource']).factory('Property', function($resource, $http){

    return $service($resource, $http, 'property');
  
});

// ----------------------------------
// 			Module Services
// ----------------------------------

angular.module('moduleService', ['ngResource']).factory('Module', function(Property, $resource, $http){

    var Module = $service($resource, $http, 'module')

    Module.prototype.properties = function() {
        var properties = [];
        $http.get(BASE_URL + '/module/' + this.id + '/properties').then(function(response) {
            angular.forEach(response.data, function(value){
                properties.push( new Property({id: value.id, name: value.name, parentId: value.parentId, values: value.values}) );
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

	var Project = $service($resource, $http, 'project');

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

// ----------------------------------
//     Properties Group Services
// ----------------------------------

angular.module('propertiesGroupService', ['ngResource']).factory('PropertiesGroup', function(Property, $resource, $http){

    var PropertiesGroup = $service($resource, $http, 'propertiesGroup');

    PropertiesGroup.prototype.properties = function() {
        var properties = [];
        $http.get(BASE_URL + '/propertiesGroup/' + this.id + '/properties').then(function(response) {
            angular.forEach(response.data, function(value){
                properties.push( new Property({id: value.id, name: value.name, parentId: value.parentId, values: value.values}) );
            });
        });
        return properties;
    }

    return PropertiesGroup;
});