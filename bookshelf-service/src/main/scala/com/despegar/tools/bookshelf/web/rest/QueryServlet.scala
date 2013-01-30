package com.despegar.tools.bookshelf.web.rest

import com.despegar.tools.bookshelf.domain.dto.Project
import com.despegar.tools.bookshelf.domain.dto.Enviroment

class QueryServlet extends RestService {

	
	// This method can be improve A LOT in performace but i prefere a more declarative code
	get("/:projectName/:moduleName/:enviromentName"){
		
		val enviroment = Enviroment.findByName( params("enviromentName") ).get
		
		Project.findByName(params("projectName")) match {
			case Some(project) => project.modules.find( _.name equals params("moduleName") ) match {
				case Some(module) => module.properties.map { p => ( p.name, p.value(enviroment)) } toMap 
				case None => 
			}
			case None => 
		}
	}
	
}