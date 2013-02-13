package com.despegar.tools.bookshelf.web.view

import org.scalatra.ScalatraServlet
import org.scalatra.scalate.ScalateSupport

class ViewsServlet extends ScalatraServlet with ScalateSupport {
	
	before() {
		contentType = "text/html"
	}
	
	get("/"){
		mustache("main", "home_section" -> "active" )
	}
	
	get("/home"){
		mustache("main", "home_section" -> "active")
	}
		
	get("/enviroments"){
		mustache("enviroments", "configuration_section" -> "active")
	}
}