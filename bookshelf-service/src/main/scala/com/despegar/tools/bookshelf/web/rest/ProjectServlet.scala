package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class ProjectServlet extends RestService{
	
	// ++++++++++++++++++++++++++++++++++
	// 		Project RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/"){
		Project.findAll.asApi
	}
	
	get("/:id"){
		Project.findById( params("id") ).get.asApi
	}
	
	post("/"){
		var newProject = extract[com.despegar.tools.bookshelf.api.dto.Project].asDomain
		
		newProject save
		
		newProject.asApi
	}
	
	delete("/:id"){
		val project = Project.findById( params("id") ).get
		
		for( property <- project.properties ) property.delete
		
		for( module <- project.modules ) module.delete
		
		project.delete
	}
	
	// ++++++++++++++++++++++++++++++++++
	// 		Modules RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:projectId/modules"){
		Project.findById( params("projectId") ).get.modules.asScala.asApi
	}
	
	post("/:projectId/newmodule"){
		val project = Project.findById( params("projectId") ).get
		var newModule = extract[com.despegar.tools.bookshelf.api.dto.Module].asDomain.asInstanceOf[Module]
		newModule.save
				
		// Add module to project
		project.modules.add(newModule)
		project.save
				
		newModule.asApi
	}
}