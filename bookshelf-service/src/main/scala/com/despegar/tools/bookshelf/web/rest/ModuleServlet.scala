package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import com.despegar.tools.bookshelf.domain.dto.Module
import com.despegar.tools.bookshelf.api.dto.{ Module => ApiModule }

import com.despegar.tools.bookshelf.domain.dto.Property

class ModuleServlet extends RestService{
	
	// ++++++++++++++++++++++++++++++++++
	// 		Module RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:id"){
		Module.findById( params("id") ).get.asApi;
	}
	
	post("/"){
		var module : Module = extract[ApiModule]
		
		module save	
		
		module asApi	
	}
	
	delete("/:id"){
		val module = Module.findById( params("id") ).get
				
		for( property <- Property.findAllByParent(module).get ) property.delete
			
		module.delete
	}
	
	// ++++++++++++++++++++++++++++++++++
	// 		Properties RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:moduleId/properties"){
		Property.findAllByParent(params("moduleId")).get.map(value => value.asApi )
	}
}