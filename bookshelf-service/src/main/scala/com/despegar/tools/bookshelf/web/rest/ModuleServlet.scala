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
	
	delete("/:id"){
		val module = Module.findById( params("id") ).get
		
		Project.findAll filter { _.modules contains module } foreach { project => project.modules remove module; project.save } 
		
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