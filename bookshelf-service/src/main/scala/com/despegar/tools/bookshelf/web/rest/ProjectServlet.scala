package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto.Project

class ProjectServlet extends RestService{

	before() {
		contentType="application/json"
	}
	
	get("/project"){
		asJson( Project findAll );
	}
	
	get("/project/:id"){
		val id = params.getOrElse("id", halt(405))
		asJson( Project findByName( id ) get );
	}
	
	post("/project"){
		val newProject = fromJson[Project]( request.body )
		
		newProject save
		
		asJson(newProject)
	}
	
	delete("/project/:id"){
		val id = params.getOrElse("id", halt(405))
		Project.deleteById(id)
	}
	
}