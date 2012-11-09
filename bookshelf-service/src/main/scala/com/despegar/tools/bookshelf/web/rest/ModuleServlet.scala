package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

class ModuleServlet extends RestService{
	
	get("/"){
		asJson( Module.findAll.asApi );
	}
	
	get("/:id"){
		asJson( Module.findById( params("id") ).get.asApi );
	}
	
	post("/:projectId"){
		params("projectId") match {
			case projectId : String if projectId.nonEmpty => {
				val project = Project findById(projectId) get
				
				// Add new module
				var newModule = fromJson[com.despegar.tools.bookshelf.api.dto.Module]( request.body ).asDomain.asInstanceOf[Module]
				newModule.parent = project
				newModule.save
				
				// Add module to project
				project.modules.add(newModule)
				project.save
				
				//asJson(newModule.asApi)
				"Create"
			}
			case _ => pass()
		}
	}
	
	post("/"){
		val projectId = params.getOrElse("projectId", null)
		
		projectId match {
			case null =>  {
				var newModule = fromJson[com.despegar.tools.bookshelf.api.dto.Module]( request.body ).asDomain.asInstanceOf[Module]

				newModule.save
				
				asJson(newModule.asApi)
			}
			case _ => pass()
		}
		
	}
	
	post("/:projectId"){
		val projectId = params.getOrElse("projectId", null)
		
		projectId match {
			case null =>  pass()
			case _ =>  {
				val project = Project findById(params("projectId")) get
				
				// Add new module
				var newModule = fromJson[com.despegar.tools.bookshelf.api.dto.Module]( request.body ).asDomain.asInstanceOf[Module]
				newModule.parent = project
				newModule.save
				
				// Add module to project
				project.modules.add(newModule)
				project.save
				
				asJson(newModule.asApi)
			}
		}
	}
	
	delete("/:id"){
		val module = Module.findById( params("id") ).get
		
		for( property <- module.properties ) property.delete
			
		module.delete
	}
}