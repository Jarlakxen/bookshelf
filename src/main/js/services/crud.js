'use strict';

var BASE_URL = '/bookshelf/rest'

function $service($resource, $name){
    var service = $resource(BASE_URL + '/' + $name + '/:id', {id:'@id'}, {
        update: { method: 'PUT', params:{id:''} },
    });

    return service;
}


// ----------------------------------
//         HTTP Actions
// ----------------------------------

var HTTPActionFactory = function($http, $q){
    var HTTPAction = {};

    HTTPAction.get = function (baseObject, url, callback){
        var result = baseObject;
        var deferred = $q.defer();

        $http.get(BASE_URL + url).then(function(response) {
            var data = response.data;
            if( !angular.isUndefined(callback) ){
                var transformation = callback(data);
                if( !angular.isUndefined(transformation) ){
                    data = transformation;
                }
            }
            deferred.resolve(data);
            deferred.resolve(angular.extend(result, data));
        });

        result.$promise = deferred.promise;

        return result;
    }

    return HTTPAction;
}

HTTPActionFactory.$inject = ['$http', '$q'];

app.factory('HTTPAction', HTTPActionFactory);


// ----------------------------------
//          Project Services
// ----------------------------------

var ProjectFactory = function($resource, $http, $q, HTTPAction, Module){
    var Project = $service($resource, 'project');

    Project.prototype.modules = function(callback) {
        function _transformer(modules){
            var result = [];
            angular.forEach(modules, function(module){
                result.push(angular.extend(new Module(), module));
            });
            if( callback != undefined ){
                return callback(result);
            } else {
                return result;
            }
        }

        return HTTPAction.get([], '/project/' + this.id + '/modules', _transformer);
    }

    return Project;
}

ProjectFactory.$inject = ['$resource', '$http', '$q', 'HTTPAction', 'Module'];

app.factory('Project', ProjectFactory);

// ----------------------------------
//          Module Services
// ----------------------------------

var ModuleFactory = function($resource, $http, $q, HTTPAction, Property){
    var Module = $service($resource, 'module');

    Module.prototype.properties = function(callback) {
        function _transformer(properties){
            var result = [];
            angular.forEach(properties, function(property){
                result.push(angular.extend(new Property(), property));
            });
            if( callback != undefined ){
                return callback(result);
            } else {
                return result;
            }
        }
        return HTTPAction.get([], '/module/' + this.id + '/properties', _transformer);
    }

    return Module;
}

ModuleFactory.$inject = ['$resource', '$http', '$q', 'HTTPAction', 'Property'];

app.factory('Module', ModuleFactory);

// ----------------------------------
//          Properties Services
// ----------------------------------

var PropertyFactory = function($resource, $http, $q, HTTPAction){
    var Property = $service($resource, 'property');

    Property.prototype.isEnviromentLinked = function(enviroment) {
        return this.values[enviroment.id].linkEnviromentId != null && this.values[enviroment.id].linkId != null;
    };

    return Property;
}

PropertyFactory.$inject = ['$resource', '$http', '$q', 'HTTPAction'];

app.factory('Property', PropertyFactory);

// ----------------------------------
//      Properties Group Services
// ----------------------------------

var PropertiesGroupFactory = function($resource, $http, $q, HTTPAction, Property){
    var PropertiesGroup = $service($resource, 'propertiesGroup');

    PropertiesGroup.prototype.properties = function(callback) {
        function _transformer(properties){
            var result = [];
            angular.forEach(properties, function(property){
                result.push(angular.extend(new Property(), property));
            });
            if( callback != undefined ){
                return callback(result);
            } else {
                return result;
            }
        }
        return HTTPAction.get([], '/module/' + this.id + '/properties', _transformer);
    }

    return PropertiesGroup;
}

PropertiesGroupFactory.$inject = ['$resource', '$http', '$q', 'HTTPAction', 'Property'];

app.factory('PropertiesGroup', PropertiesGroupFactory);


// ----------------------------------
//          Enviroment Services
// ----------------------------------

var EnviromentFactory = function($resource, $http, $q){
    var Enviroment = $service($resource, 'enviroment');


    return Enviroment;
}

EnviromentFactory.$inject = ['$resource', '$http', '$q'];

app.factory('Enviroment', EnviromentFactory);