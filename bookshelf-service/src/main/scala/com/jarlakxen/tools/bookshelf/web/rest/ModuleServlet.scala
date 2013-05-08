package com.jarlakxen.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import com.jarlakxen.tools.bookshelf.domain.dto._
import com.jarlakxen.tools.bookshelf.domain.dto.Module
import com.jarlakxen.tools.bookshelf.api.dto.{ Module => ApiModule }

import com.jarlakxen.tools.bookshelf.domain.dto.Property

class ModuleServlet extends RestService{
	
	// ++++++++++++++++++++++++++++++++++
	// 		Module RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:id"){
		Module.findById( params("id") ).get.asApi;
	}
	
	post("/"){
		var module : Module = extract[ApiModule]
		
		module.saveOrUpdate.asApi
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
		Property.findAllByParentId(params("moduleId")).map(value => value.asApi )
	}
}