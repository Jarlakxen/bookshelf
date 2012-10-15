package com.despegar.tools.bookshelf.web.rest

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport
import com.despegar.tools.bookshelf.domain.dto.Project

class ModuleServlet extends RestService{
	
	get("/projects"){
		contentType="application/json"
		asJson( Seq(new Project("Cars", ""), new Project("Hotels", "")) );
	}
	
}