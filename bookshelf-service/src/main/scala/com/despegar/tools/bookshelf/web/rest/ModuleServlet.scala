package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class ModuleServlet extends RestService{
	
	
	get("/:id"){
		Module.findById( params("id") ).get.asApi;
	}
	
	post("/"){
		var module = extract[com.despegar.tools.bookshelf.api.dto.Module].asDomain
		module save	
	}
	
	put("/:projectId"){
		val project = Project.findById( params("projectId") ).get
		var newModule = extract[com.despegar.tools.bookshelf.api.dto.Module].asDomain.asInstanceOf[Module]
		newModule.save
				
		// Add module to project
		project.modules += newModule
		project.save
				
		newModule.asApi
	}
	
	delete("/:id"){
		val module = Module.findById( params("id") ).get
		
		for( property <- module.properties ) property.delete
			
		module.delete
	}
	
	// ++++++++++++++++++++++++++++++++++
	// 		Properties RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:moduleId/properties"){
		(Module.findById( params("moduleId") ).get.properties).asScala.asApi;
	}
	
	post("/:moduleId/newproperty"){
	
	}
}