package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto._

class EnviromentServlet extends RestService{

	get("/"){
		Enviroment.findAll.asApi;
	}	
	
	get("/:id"){
		Enviroment.findById( params("id") ).get.asApi;
	}
	
	post("/"){
		val newEnviroment = extract[com.despegar.tools.bookshelf.api.dto.Enviroment].asDomain
		
		newEnviroment save
		
		newEnviroment asApi
	}
	
	delete("/:id"){
		val enviroment = Enviroment.findById( params("id") ).get
		
		Property.deleteEnvironmentFromAll(enviroment.name)
		
		enviroment.delete
	}
}