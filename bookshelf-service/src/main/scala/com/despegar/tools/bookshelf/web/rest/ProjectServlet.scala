package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class ProjectServlet extends RestService{
	
	get("/"){
		asJson( Project.findAll.asApi );
	}
	
	get("/:id"){
		asJson( Project.findById( params("id") ).get.asApi );
	}
	
	post("/"){
		var newProject = fromJson[com.despegar.tools.bookshelf.api.dto.Project]( request.body ).asDomain
		
		newProject save
		
		asJson(newProject.asApi)
	}
	
	delete("/:id"){
		val project = Project.findById( params("id") ).get
		
		for( property <- project.properties ) property.delete
		
		for( module <- project.modules ) module.delete
		
		project.delete
	}
}