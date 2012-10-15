'use strict';


var BASE_URL = '/bookshelf/rest'

	
// ----------------------------------
// 			Project Services
// ----------------------------------

angular.module('projectService', ['ngResource']).factory('Project', function($resource){

	return $resource(BASE_URL + '/project/:name', {name:'@name'});
  	/*return $resource(BASE_URL + '/projects', {}, {
		query : {
			method : 'GET',
			params : {},
			isArray : true
		}
	});*/
});