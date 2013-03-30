'use strict';


var BASE_URL = '/bookshelf/rest'

function $service($resource, $http, $name){
    var service = $resource(BASE_URL + '/' + $name + '/:id', {id:'@id'}, {});

    var fnquery = service.query;

    service.query = function(success, error) {

        switch(arguments.length) {
            case 2:
                return $ext_array(fnquery(success, error));
            case 1:
                return $ext_array(fnquery(success));
            case 0: 
                return $ext_array(fnquery());
            default:
                throw "Expected between 0-2 arguments [success, error], got " + arguments.length + " arguments.";
        }
    }

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

    Module.prototype.properties = function(callback) {
        var properties = $ext_array();

        $http.get(BASE_URL + '/module/' + this.id + '/properties').then(function(response) {
            angular.forEach(response.data, function(value){
                var property = new Property({id: value.id, name: value.name, parentId: value.parentId, values: value.values});
                if( callback != undefined){
                    callback(property);
                }
                properties.push( property );
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

 	Project.prototype.modules = function(callback) {
 		var modules = $ext_array();

    	$http.get(BASE_URL + '/project/' + this.id + '/modules').then(function(response) {
    		angular.forEach(response.data, function(value){
                var module = new Module({id: value.id, name: value.name, description: value.description});
                if( callback != undefined){
                    callback(module);
                }
				modules.push( module );
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

    PropertiesGroup.prototype.properties = function(callback) {
        var properties = $ext_array();

        $http.get(BASE_URL + '/propertiesGroup/' + this.id + '/properties').then(function(response) {
            angular.forEach(response.data, function(value){
                var property = new Property({id: value.id, name: value.name, parentId: value.parentId, values: value.values});
                if( callback != undefined){
                    callback(property);
                }
                properties.push( property );
            });
        });
        return properties;
    }

    return PropertiesGroup;
});
