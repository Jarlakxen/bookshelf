package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto.Project

class ProjectServlet extends RestService{
	
	var projects = Seq(new Project("Cars", ""), new Project("Hotels", ""));
	
	before() {
		contentType="application/json"
	}
	
	get("/project"){
		asJson( projects );
	}
	
	get("/project/:name"){
		asJson( projects find (_.name == params("name") ) );
	}
	
	post("/project/:name"){
		var project = new Project(params.getOrElse("name", halt(405)), "") save
		
		projects = projects :+ project
		
		project
	}
	
}