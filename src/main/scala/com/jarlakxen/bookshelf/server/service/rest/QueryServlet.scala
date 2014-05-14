package com.jarlakxen.bookshelf.server.service.rest

import com.jarlakxen.bookshelf.server.domain.model._
import com.jarlakxen.bookshelf.server.domain.dao._


class QueryServlet extends RestService {

	
	// This method can be improve A LOT in performace but i prefere a more declarative code
	get("/:projectName/:moduleName/:enviromentName"){
		
		val enviroment = Enviroment.findByName( params("enviromentName") ).get
		
		Project.findByName(params("projectName")) match {
			case Some(project) => project.modules.find( _.name equals params("moduleName") ) match {
				case Some(module) => module.properties.map { p => ( p.name, p.value(enviroment) match { case Some(v) => v.value; case None => null }) } toMap 
				case None => 
			}
			case None => 
		}
	}
	
}