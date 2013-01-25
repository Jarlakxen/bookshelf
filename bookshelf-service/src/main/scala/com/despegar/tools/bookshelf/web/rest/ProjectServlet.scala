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
		
		for( module <- Module.findAllByParent( params("projectId") ).get ) module.delete
		
		project.delete
	}
	
	// ++++++++++++++++++++++++++++++++++
	// 		Modules RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:projectId/modules"){
		Module.findAllByParent( params("projectId") ).get.asApi
	}
}