package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import com.despegar.tools.bookshelf.domain.dto.PropertiesGroup
import com.despegar.tools.bookshelf.api.dto.{ PropertiesGroup => ApiPropertiesGroup }

class PropertiesGroupServlet extends RestService{
	
	// ++++++++++++++++++++++++++++++++++
	// 		Properties Group RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/"){
		PropertiesGroup.findAll.map( _.asApi ) 
	}
	
	get("/:id"){
		PropertiesGroup.findById( params("id") ).get.asApi
	}
	
	post("/"){
		
		var newPropertiesGroup : PropertiesGroup = extract[ApiPropertiesGroup]
		
		newPropertiesGroup save
		
		newPropertiesGroup asApi
	}
	
	delete("/:id"){
		val propertiesGroup = PropertiesGroup.findById( params("id") ).get
		
		propertiesGroup.delete
	}
}