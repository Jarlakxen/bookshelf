package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto._

import com.despegar.tools.bookshelf.domain.dto.Enviroment
import com.despegar.tools.bookshelf.api.dto.{ Enviroment => ApiEnviroment }

class EnviromentServlet extends RestService {

	get( "/" ) {
		Enviroment.findAll.map( _.asApi )
	}

	get( "/:id" ) {
		Enviroment.findById( params( "id" ) ).get.asApi;
	}

	post( "/" ) {
		var newEnviroment : Enviroment = extract[ApiEnviroment]

		newEnviroment save

		newEnviroment asApi
	}

	delete( "/:id" ) {
		val enviroment = Enviroment.findById( params( "id" ) ).get

		Property.deleteEnvironmentFromAll( enviroment.name )

		enviroment.delete
	}
}