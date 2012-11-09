'use strict';


var BASE_URL = '/bookshelf/rest'

	
// ----------------------------------
// 			Project Services
// ----------------------------------

angular.module('projectService', ['ngResource']).factory('Project', function($resource){

	return $resource(BASE_URL + '/project/:id:action', {id:'@id', action:'@action'},{
		addModule:{
			method: "POST",
			params: {
				listController: "/newmodule"
			}	
		}
	});
  
});

// ----------------------------------
// 			Module Services
// ----------------------------------

angular.module('moduleService', ['ngResource']).factory('Module', function($resource){

	return $resource(BASE_URL + '/module/:id', {id:'@id'});
  
});