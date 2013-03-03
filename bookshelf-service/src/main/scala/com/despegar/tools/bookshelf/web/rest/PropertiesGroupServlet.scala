package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import com.despegar.tools.bookshelf.domain.dto._
import com.despegar.tools.bookshelf.domain.dto.PropertiesGroup
import com.despegar.tools.bookshelf.api.dto.{ PropertiesGroup => ApiPropertiesGroup }
import com.despegar.tools.bookshelf.domain.dto.Property

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
		
		newPropertiesGroup saveOrUpdate
		
		newPropertiesGroup asApi
	}
	
	delete("/:id"){
		val propertiesGroup = PropertiesGroup.findById( params("id") ).get
		
		propertiesGroup.delete
	}
	
	// ++++++++++++++++++++++++++++++++++
	// 		Properties RestFul Services
	// ++++++++++++++++++++++++++++++++++
	
	get("/:propertiesGroupId/properties"){
		Property.findAllByParentId(params("propertiesGroupId")).map(value => value.asApi )
	}
}