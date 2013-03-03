package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import com.despegar.tools.bookshelf.domain.dto._
import com.despegar.tools.bookshelf.domain.dto.Project
import com.despegar.tools.bookshelf.api.dto.{ Project => ApiProject }

import com.despegar.tools.bookshelf.domain.dto.Module

class ProjectServlet extends RestService{
	
	// ++++++++++++++++++++++++++++++++++
	// 		Project RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/"){
		Project.findAll.map( _.asApi ) 
	}
	
	get("/:id"){
		Project.findById( params("id") ).get.asApi
	}
	
	post("/"){
		
		var newProject : Project = extract[ApiProject]
		
		newProject saveOrUpdate
		
		newProject asApi
	}
	
	delete("/:id"){
		val project = Project.findById( params("id") ).get
		
		for( module <- project.modules ) module.delete
		
		project.delete
	}
	
	// ++++++++++++++++++++++++++++++++++
	// 		Modules RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:projectId/modules"){
		Module.findAllByParentId( params("projectId") ).map(value => value.asApi )
	}
}