package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto._

class EnviromentServlet extends RestService{

	get("/"){
		asJson(Enviroment.findAll.asApi);
	}	
	
	get("/:id"){
		asJson( Enviroment.findById( params("id") ).get.asApi );
	}
	
	post("/"){
		val newEnviroment = fromJson[com.despegar.tools.bookshelf.api.dto.Enviroment]( request.body ).asDomain
		
		newEnviroment save
		
		asJson(newEnviroment asApi)
	}
	
	delete("/:id"){
		val enviroment = Enviroment.findById( params("id") ).get
		
		Property.deleteEnvironmentFromAll(enviroment.name)
		
		enviroment.delete
	}
}