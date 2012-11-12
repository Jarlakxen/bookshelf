'use strict';


var BASE_URL = '/bookshelf/rest'

	
// ----------------------------------
// 			Project Services
// ----------------------------------

angular.module('projectService', ['ngResource']).factory('Project', function($resource){

	return $resource(BASE_URL + '/project/:id/:cmd', {id:'@id'},{
		modules:{
			method: 'GET',
			params: {
				cmd: 'modules'
			},
			isArray:true
		}
	});
  
});

// ----------------------------------
// 			Module Services
// ----------------------------------

angular.module('moduleService', ['ngResource']).factory('Module', function($resource){

	return $resource(BASE_URL + '/module/:id:projectId', {id:'@id'}, {
		addTo:{
			method: "PUT"
		}
	});
  
});